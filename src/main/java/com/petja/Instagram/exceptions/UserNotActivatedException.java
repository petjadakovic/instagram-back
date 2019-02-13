package com.petja.Instagram.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException{

	public UserNotActivatedException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
