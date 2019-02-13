package com.petja.Instagram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserConfirmException extends Exception{

	public UserConfirmException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
