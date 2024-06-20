package com.example.GymProject.controller;

import com.example.GymProject.request.ChangePasswordRequest;
import com.example.GymProject.request.UserPassRequest;
import com.example.GymProject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @GetMapping
    public ResponseEntity<String> login(@Valid @RequestBody UserPassRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        return userService.matchUsernameAndPassword(request.getUsername(), request.getPassword()) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        logger.info("Password change attempt for username: {}", request.getUsername());

        boolean isPasswordChanged = userService.changePassword(request.getUsername(), request.getNewPassword(), request.getOldPassword());
        if (isPasswordChanged) {
            logger.info("Password change successful for username: {}", request.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.warn("Password change failed for username: {}", request.getUsername());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
