package com.example.GymProject.controller;

import com.example.GymProject.dto.request.ChangePasswordRequestDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.dto.respone.AuthResponseDto;
import com.example.GymProject.security.LoginAttemptService;
import com.example.GymProject.security.jwt.JwtTokenProvider;
import com.example.GymProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class LoginControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private LoginController loginController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        doNothing().when(loginAttemptService).loginSucceeded("username");

        ResponseEntity<?> response = loginController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new AuthResponseDto("jwt-token"), response.getBody());
    }

    @Test
    void testLogin_Unsuccessful() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        doNothing().when(loginAttemptService).loginFailed("username");

        ResponseEntity<?> response = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testLogin_UserBlocked() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");
        when(loginAttemptService.isBlocked("username")).thenReturn(true);

        ResponseEntity<?> response = loginController.login(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is blocked. Try again later.", response.getBody());
    }

    @Test
    void testChangePassword_Successful() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");

        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(true);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_Authentication() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");


        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_PasswordChange() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");

        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(false);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogout_Successful() {
        SecurityContextHolder.setContext(new SecurityContextImpl());
        ResponseEntity<String> response = loginController.logout();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged out successfully", response.getBody());
        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());
    }
}