package com.petja.Instagram.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petja.Instagram.entities.User;
import com.petja.Instagram.exceptions.UserNotActivatedException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


import static com.petja.Instagram.security.SecurityConstants.EXPIRATION_TIME;
import static com.petja.Instagram.security.SecurityConstants.HEADER_STRING;
import static com.petja.Instagram.security.SecurityConstants.SECRET;
import static com.petja.Instagram.security.SecurityConstants.TOKEN_PREFIX;

/**
 * Sluzi da da JSON Web Token user-u koji pokusava da pristupi (user salje username i password).
 *
 * Ova klasa nasledjuje UsernamePasswordAuthenticationFilter koja nasledjuje AbstractAuthenticationProcessingFilter
 * Ocekuje da u login formi imamo dva parametra: username i password
 * Ukoliko hocemo da promenimo ove default vrednosti moramo da uradimo override settera za parametre: usernameParameter i passwordParameter
 * Po defaultu ocekuje da je URL /login
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //interfejs a nama vazna metoda je: authenticate
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            /*
            Dobra praksa je da se koristi samo sa AuthenticationManager interfejsom.
             */
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        }catch (UserNotActivatedException ex) {
			throw ex;
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
    
    public static String getNewToken(User user) {
    	Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
		return  TOKEN_PREFIX + token;
    }
}