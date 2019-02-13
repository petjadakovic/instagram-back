package com.petja.Instagram.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.Post;
import com.petja.Instagram.services.PostService;
import com.petja.Instagram.services.UserService;

@RestController
@RequestMapping("/posts")
public class PostController {
	
	Path path;

    @Autowired
	private UserService userService;
    
    @Autowired
   	private PostService postService;
    
    @Autowired
    ServletContext servletContext;
	
	
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("photo") MultipartFile file,
        RedirectAttributes redirectAttributes, Principal principal) throws IOException{
    	Photo photo = userService.addPostPhoto(principal.getName() , file);
    	userService.addPost(photo, principal.getName());
        return "{\"message\":\"Post uploaded successfully\"}";
    }
    
    @RequestMapping("/feed")
    public List<Post> get(Principal principal) throws IOException{
    	return postService.getFeedPosts(principal.getName());
    }
}
