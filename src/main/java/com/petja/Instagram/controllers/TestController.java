package com.petja.Instagram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.petja.Instagram.entities.User;
import com.petja.Instagram.services.EmailService;
import com.petja.Instagram.services.PostService;
import com.petja.Instagram.services.UserService;

@RestController
public class TestController {
	
	 @Autowired
	 public EmailService emailService;
	 
	 @Autowired
	 public PostService postService;
	
    @RequestMapping("/test")
    public String test() {
		//emailService.sendMail("petjadakovic@gmail.com", "Test", "this is test email");
    	postService.getFeedPosts("");
		return "something";
    }
    @PostMapping(value="/test" )
    public String test2() {
		//emailService.sendMail("petjadakovic@gmail.com", "Test", "this is test email");
		return "mail sent";
    }
//    
//    @RequestMapping("/test")
//    public RedirectView redirectWithUsingRedirectView(
//      RedirectAttributes attributes) {
//        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
//        attributes.addAttribute("attribute", "redirectWithRedirectView");
//        return new RedirectView("http://localhost:4201/login");
//    }
}
