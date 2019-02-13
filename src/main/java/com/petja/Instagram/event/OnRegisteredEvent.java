package com.petja.Instagram.event;

import org.springframework.context.ApplicationEvent;

import com.petja.Instagram.entities.User;

public class OnRegisteredEvent extends ApplicationEvent{
	private String appUrl;
    private User user;

    public OnRegisteredEvent(User user, final String appUrl) {
        super(user);
        this.appUrl = appUrl;
        this.user = user;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    
}
