package com.petja.Instagram.services;

import static java.util.Collections.emptyList;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.auth0.jwt.interfaces.Verification;
import com.petja.Instagram.entities.Follow;
import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.Post;
import com.petja.Instagram.entities.User;
import com.petja.Instagram.entities.UserInfo;
import com.petja.Instagram.entities.VerificationToken;
import com.petja.Instagram.event.OnRegisteredEvent;
import com.petja.Instagram.exceptions.SignUpException;
import com.petja.Instagram.exceptions.UserConfirmException;
import com.petja.Instagram.exceptions.UserNotActivatedException;
import com.petja.Instagram.repository.FollowRepository;
import com.petja.Instagram.repository.PhotoRepository;
import com.petja.Instagram.repository.PostRepository;
import com.petja.Instagram.repository.UserRepository;
import com.petja.Instagram.repository.VerificationTokenRepository;
import com.petja.Instagram.security.JWTAuthenticationFilter;
import com.sun.mail.handlers.image_gif;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {
	private UserRepository userRepository;
	private PhotoRepository photoRepository;
	private FollowRepository followRepository;
	private PostRepository postRepository;
	
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private ApplicationEventPublisher eventPublisher;
    
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationEventPublisher eventPublisher, VerificationTokenRepository verificationTokenRepository
                          ,PhotoRepository photoRepository, FollowRepository followRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.eventPublisher = eventPublisher;
        this.verificationTokenRepository = verificationTokenRepository;
        this.photoRepository = photoRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException,UserNotActivatedException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        } else if (!user.isEnabled()) {
			//throw new UserNotActivatedException("User is not activated");
		}
        System.out.println(user.toString());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(Arrays.asList("ROLE_USER")));
    }
    
    public boolean signUpUser(User user, String appUrl) throws SignUpException {
    	if(user == null) {
    		throw new SignUpException("User is null");
    	} else if(userRepository.findByUsername(user.getUsername()) != null) {
    		throw new SignUpException("Username already exists");
    	} else if(userRepository.findByEmail(user.getEmail()) != null) {
    		throw new SignUpException("Email already exists");
    	}
    	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        eventPublisher.publishEvent(new OnRegisteredEvent(user, appUrl));
        return true;
    }
    
    public void createVerificationToken(User user, String token) {
    	VerificationToken verificationToken = new VerificationToken(user, token);
    	verificationTokenRepository.save(verificationToken);
    }

	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	public void confirmUser(String token) throws UserConfirmException{
		// TODO Auto-generated method stub
		VerificationToken verificationToken = getVerificationToken(token);
    	if(token == null) {
    		throw new UserConfirmException("Token not found");
    	}
    	Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new UserConfirmException("Token expired");
        }
    	User user = verificationToken.getUser();
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
	
	public String checkEmail (String email) {
        if(userRepository.findByEmail(email) == null) {
        	return "{\"valid\":true}";
        }
        return "{\"valid\":false}";
    }
	
	public String checkUsername (String username) {
        if(userRepository.findByUsername(username) == null) {
        	return "{\"valid\":true}";
        }
        return "{\"valid\":false}";
    }

	public String changeEmail(String old_email, String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(old_email);
		System.out.println("old email "+ old_email);
		if(user != null) {
			user.setEmail(email);
			userRepository.save(user);
			return JWTAuthenticationFilter.getNewToken(user);
		}
		return null;
	}

	public void changePassword(String email, String password) {
		User user = userRepository.findByEmail(email);
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}
	
	public Photo addPostPhoto(String email, MultipartFile file) throws IOException{
		
		User user = userRepository.findByEmail(email);
		Photo photo = new Photo();
		photo.setUser(user);
		Photo saved_photo = photoRepository.save(photo);

		Image image = ImageIO.read(file.getInputStream());
		File outputfile = new File("storage\\" + saved_photo.getId() + ".jpg");
		ImageIO.write(Util.resizeImage(image, 800), "jpg", outputfile);
		saved_photo.setSrc("storage\\" + saved_photo.getId() + ".jpg");
		photoRepository.save(saved_photo);
		return saved_photo;
	}
	
	public Photo addPhoto(String email, MultipartFile file) throws IOException{
		
		User user = userRepository.findByEmail(email);
		Photo photo = new Photo();
		photo.setUser(user);
		Photo saved_photo = photoRepository.save(photo);

		Image image = ImageIO.read(file.getInputStream());
		File outputfile = new File("storage\\" + saved_photo.getId() + ".jpg");
		ImageIO.write(Util.resizeImage(image, 800), "jpg", outputfile);
		outputfile = new File("storage\\" + saved_photo.getId() + "-small.jpg");
		ImageIO.write(Util.resizeImage(image, 150), "jpg", outputfile);
		outputfile = new File("storage\\" + saved_photo.getId() + "-circle.png");
		ImageIO.write(Util.resizeImageCircle(image, 150), "png", outputfile);
	
		saved_photo.setSrc("storage\\" + saved_photo.getId() + ".jpg");
		photoRepository.save(saved_photo);
		return saved_photo;
	}
	
	public void setProfilePhoto(String email, Photo photo){
		System.out.println('1');
		User user = userRepository.findByEmail(email);
		user.setProfile_picture_id(photo.getId());
		userRepository.save(user);
		System.out.println('2');
	}

	public Resource getFile() {
		try {
			Path file = Paths.get("storage").resolve("1.jpg");
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public String getProfilePictureId(String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		String id = "0";
		if(user.getProfile_picture_id() != 0) {
			id  = user.getProfile_picture_id() + "";
		}
		return "{\"id\":\"" + id + "\"}";
	}
	
	public String getProfilePictureById(String id) {
		// TODO Auto-generated method stub
		Optional<User> op_user = userRepository.findById(Long.valueOf(id));
		User user = op_user.get();
		String img_id = "0";
		if(user != null && user.getProfile_picture_id() != 0) {
			img_id  = user.getProfile_picture_id() + "";
		}
		return "{\"id\":\"" + img_id + "\"}";
	}

	public List<User> getUsersLike(String username) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(0,50);
		List<User> users = userRepository.findByUsernameLikeAndEnabled("%" + username + "%", true, pageable);
		for (User user : users) {
			user.setEmail(null);
			user.setPassword(null);
		}
		return users;
	}

	public UserInfo getUserInfo(String id, String email) {
		// TODO Auto-generated method stub
		Optional<User> op_user = userRepository.findById(Long.valueOf(id));
		User user = op_user.get();
		User me = userRepository.findByEmail(email);
		
		UserInfo userInfo = null;
				
		if(user != null) {
			userInfo = new UserInfo();
			userInfo.username = user.getUsername();
			userInfo.profile_picture_id = user.getProfile_picture_id();
			List<Follow> followers = followRepository.findByUser2Id(user.getId());
			List<Follow> follows = followRepository.findByUser1Id(user.getId());
			userInfo.follow_count = followers.size();
			userInfo.following_count = follows.size();
			for(Follow follow: followers) {
				if(follow.getUser1().getId() == me.getId()) {
					userInfo.following_him = true;
				}
			}
			for(Follow follow: follows) {
				if(follow.getUser2().getId() == me.getId()) {
					userInfo.following_me = true;
				}
			}
			userInfo.posts = postRepository.findAllByUserId(user.getId());
			userInfo.is_me = user.getId() == me.getId();
		}
		
		return userInfo;
	}
	
	public String follow(String id, String email) {
		// TODO Auto-generated method stub
		
		Optional<User> op_user = userRepository.findById(Long.valueOf(id));

		User user = op_user.get();
		
		User me = userRepository.findByEmail(email);
		
		Follow follow = followRepository.findByUser1IdAndUser2Id(me.getId(), user.getId());
		
		if(follow == null) {
			follow = new Follow();
			follow.setUser1(me);
			follow.setUser2(user);
			followRepository.save(follow);
		}

		return "{\"following\":true}";
	}
	
	public String unFollow(String id, String email) {
		// TODO Auto-generated method stub
		Optional<User> op_user = userRepository.findById(Long.valueOf(id));
		User user = op_user.get();
		User me = userRepository.findByEmail(email);
		
		Follow follow = followRepository.findByUser1IdAndUser2Id(me.getId(), user.getId());
		
		System.out.println("deleeee " + follow);
		
		if(follow != null) {
			followRepository.delete(follow);
			System.out.println("deleeee done");
		}
		
		return "{\"following\":false}";
	}

	public void addPost(Photo photo, String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		Post post = new Post();
		post.setPhoto(photo);
		post.setUser(user);
		postRepository.save(post);
	}
}
