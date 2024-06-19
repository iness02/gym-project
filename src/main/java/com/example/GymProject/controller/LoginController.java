package com.example.GymProject.controller;

import com.example.GymProject.request.ChangePasswordRequest;
import com.example.GymProject.request.UserPassRequest;
import com.example.GymProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<String> login(@Valid @RequestBody UserPassRequest request) {
        return userService.matchUsernameAndPassword(request.getUsername(), request.getPassword()) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request.getUsername(), request.getNewPassword(), request.getOldPassword()) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
