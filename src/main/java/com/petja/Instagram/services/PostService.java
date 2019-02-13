package com.petja.Instagram.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.petja.Instagram.entities.Post;
import com.petja.Instagram.entities.User;
import com.petja.Instagram.repository.FollowRepository;
import com.petja.Instagram.repository.PhotoRepository;
import com.petja.Instagram.repository.PostRepository;
import com.petja.Instagram.repository.UserRepository;
import com.petja.Instagram.repository.VerificationTokenRepository;

@Service
public class PostService {
	private UserRepository userRepository;
	private PhotoRepository photoRepository;
	private FollowRepository followRepository;
	private PostRepository postRepository;
	
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private ApplicationEventPublisher eventPublisher;
    
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public PostService(UserRepository userRepository,
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
    
    public List<Post> getFeedPosts(String email){
    	User user = userRepository.findByEmail(email);
    	return postRepository.findAllByUserFollowing(user);
    	//return postRepository.findAllByUserFollowers(user);
    }
}
