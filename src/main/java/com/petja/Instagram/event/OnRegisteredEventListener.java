package com.petja.Instagram.event;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.petja.Instagram.services.EmailService;
import com.petja.Instagram.services.UserService;

@Component
public class OnRegisteredEventListener implements ApplicationListener<OnRegisteredEvent>{

	@Autowired
    private UserService service;
  
  
    @Autowired
    private EmailService emailService;

	@Override
	public void onApplicationEvent(OnRegisteredEvent event) {
		 final String token = UUID.randomUUID().toString();
	        service.createVerificationToken(event.getUser(), token);
	        String to = event.getUser().getEmail();
	        String subject = "MyRafInstagram Registration";
	        String body = "You registation is almost complete. Please click on this link to complete registration." + "\n" +
	                "http://myrafinstagram.ddns.net:8080" + event.getAppUrl() + "/users/confirm?token=" + token;
	        emailService.sendMail(to, subject, body);
	}

}
