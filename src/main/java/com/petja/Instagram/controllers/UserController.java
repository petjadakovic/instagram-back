package com.petja.Instagram.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.IOUtils;

import javax.activation.FileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.yaml.snakeyaml.reader.StreamReader;

import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.Post;
import com.petja.Instagram.entities.User;
import com.petja.Instagram.entities.UserInfo;
import com.petja.Instagram.entities.VerificationToken;
import com.petja.Instagram.exceptions.SignUpException;
import com.petja.Instagram.exceptions.UserConfirmException;
import com.petja.Instagram.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	Path path;

    @Autowired
	private UserService userService;
    
    @Autowired
    ServletContext servletContext;

    @PostMapping("/register")
    public String signUp(@RequestBody User user, WebRequest request) throws SignUpException{
    	String appUrl = request.getContextPath();
    	if(userService.signUpUser(user, appUrl)) {
    		return "{\"success\": true}";
    	} {
    		return "{\"success\": false}";
    	}
    }
    
    @RequestMapping("/confirm")
    public RedirectView redirectWithUsingRedirectView(
      RedirectAttributes attributes, @RequestParam String token) throws UserConfirmException {
    	userService.confirmUser(token);
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("attribute", "redirectWithRedirectView");
        return new RedirectView("http://myrafinstagram.ddns.net:80/login");
    }
    
    @RequestMapping("/check-email")
    public String checkEmail(@RequestParam String email) {
    	return userService.checkEmail(email);
    }
    
    @RequestMapping("/check-username")
    public String checkUsername(@RequestParam String username) {
    	return userService.checkUsername(username);
    }
    
    @PostMapping("/change-email")
    public String changeEmail(@RequestBody User user, Principal principal) {
    	String token = userService.changeEmail(principal.getName(), user.getEmail());
    	return "{\"token\":\"" + token + "\"}";
    }
    
    @PostMapping("/change-password")
    public void changePassword(@RequestBody User user, Principal principal) {
    	userService.changePassword(principal.getName(), user.getPassword());
    }
    
    
    @PostMapping("/upload-profile")
    public String handleFileUploadProfile(@RequestParam("photo") MultipartFile file,
            RedirectAttributes redirectAttributes, Principal principal) throws IOException{
    	Photo photo = userService.addPhoto(principal.getName() , file);
    	userService.setProfilePhoto(principal.getName(), photo);
        return "{\"message\":\"Profile picture uploaded successfully\"}";
    }
    
    
    @RequestMapping(value = "/photo", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response,  @RequestParam String id) throws IOException {
        File file = new File("storage/" + id + ".jpg");
        FileInputStream fio = new FileInputStream(file);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(fio, response.getOutputStream());
    }
    
    @RequestMapping(value = "/photo-small", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void getSmallImage(HttpServletResponse response,  @RequestParam String id) throws IOException {
        File file = new File("storage/" + id + "-small.jpg");
        FileInputStream fio = new FileInputStream(file);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(fio, response.getOutputStream());
    }
    
    @RequestMapping(value = "/photo-circle", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public void getCircleImage(HttpServletResponse response,  @RequestParam String id) throws IOException {
        File file = new File("storage/" + id + "-circle.png");
        FileInputStream fio = new FileInputStream(file);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(fio, response.getOutputStream());
    }
    
    @RequestMapping("/like")
    public List<User> getUsersLike(@RequestParam String username) {
    	return userService.getUsersLike(username);
    }
    
    @RequestMapping("profile-photo")
    public String getProfilePhotoId(Principal principal) {
        return userService.getProfilePictureId(principal.getName());
    }
    
    @RequestMapping("profile-photo/{id}")
    public String getProfilePhotoById(Principal principal, @PathVariable String id) {
        return userService.getProfilePictureById(id);
    }
    
    @RequestMapping("user-info/{id}")
    public UserInfo getUserInfo(Principal principal, @PathVariable String id) {
        return userService.getUserInfo(id, principal.getName());
    }
    
    @RequestMapping("follow/{id}")
    public String follow(Principal principal, @PathVariable String id) {
        return userService.follow(id, principal.getName());
    }
    
    @RequestMapping("unfollow/{id}")
    public String unFollow(Principal principal, @PathVariable String id) {
        return userService.unFollow(id, principal.getName());
    }

    
}
