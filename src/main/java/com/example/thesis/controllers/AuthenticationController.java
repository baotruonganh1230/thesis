package com.example.thesis.controllers;

import com.example.thesis.config.JWTTokenHelper;
import com.example.thesis.requests.AuthenticationRequest;
import com.example.thesis.responses.LoginResponse;
import com.example.thesis.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private AccountService accountService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws InvalidKeySpecException, NoSuchAlgorithmException {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken=jwtTokenHelper.generateToken(authentication.getName());

        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setToken(jwtToken);

        Cookie cookie = new Cookie("jwt_token", jwtToken);
        cookie.setMaxAge(5 * 60); // expires in 5 minutes
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // global cookie accessible every where

        //add cookie to response
        response.addCookie(cookie);
        response.addHeader("Body", "username=" + authenticationRequest.getUsername() +
                        "; password=" + authenticationRequest.getPassword());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
