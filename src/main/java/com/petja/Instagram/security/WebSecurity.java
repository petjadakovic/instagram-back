package com.petja.Instagram.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.petja.Instagram.services.UserService;

import static com.petja.Instagram.security.SecurityConstants.SIGN_UP_URL;

import java.util.Arrays;

import static com.petja.Instagram.security.SecurityConstants.CONFIRM_TOKEN;
import static com.petja.Instagram.security.SecurityConstants.CHECK_EMAIL;
import static com.petja.Instagram.security.SecurityConstants.CHECK_USERNAME;
import static com.petja.Instagram.security.SecurityConstants.PHOTO_URL;
import static com.petja.Instagram.security.SecurityConstants.PHOTO_SMALL_URL;
import static com.petja.Instagram.security.SecurityConstants.PHOTO_CIRCLE_URL;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/test").permitAll()
                .antMatchers(CONFIRM_TOKEN).permitAll()
                .antMatchers(PHOTO_URL).permitAll()
                .antMatchers(PHOTO_SMALL_URL).permitAll()
                .antMatchers(PHOTO_CIRCLE_URL).permitAll()
                .antMatchers(CHECK_EMAIL).permitAll()
                .antMatchers(CHECK_USERNAME).permitAll()
                .antMatchers(SIGN_UP_URL).permitAll() //kazemo cemu svaki korisnik moze pristupiti
                //.antMatchers("/test").permitAll() 
                // .antMatchers("/admin/**").hasRole("ADMIN") //ovo bi znacilo da moze pristupiti samo korisnik koji ima rolu ADMIN
                .anyRequest().authenticated() //na sve druge rute koji nisu navedene dovoljno je da korisnik bude autentifikovan
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // sesije su samo STATELESS
        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        configuration.setMaxAge((long) 3600);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
   

}