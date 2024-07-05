package com.example.GymProject.controller;

import com.example.GymProject.dto.request.ChangePasswordRequestDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.dto.respone.AuthResponseDto;
import com.example.GymProject.security.jwt.JwtTokenProvider;
import com.example.GymProject.service.UserService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Counted(value = "login.attempts", description = "Counts login attempts")
    @Timed(value = "login.time", description = "Time taken for login method execution")
    @GetMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @Counted(value = "password.change.attempts", description = "Counts password change attempts")
    @Timed(value = "password.change.time", description = "Time taken for changePassword method execution")
    @GetMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        logger.info("Password change attempt for username: {}", request.getUsername());
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getOldPassword())) {
            boolean isPasswordChanged = userService.changePassword(request.getUsername(), request.getNewPassword(), request.getOldPassword());
            if (isPasswordChanged) {
                logger.info("Password change successful for username: {}", request.getUsername());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                logger.warn("Password change failed for username: {}", request.getUsername());
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }
}
