package com.example.GymProject.controller;

import com.example.GymProject.dto.request.ChangePasswordRequestDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class LoginControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);

        ResponseEntity<?> response = loginController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogin_Unsuccessful() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(false);

        ResponseEntity<?> response = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testChangePassword_Successful() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(true);
        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(true);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_Authentication() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(false);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_PasswordChange() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(true);
        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(false);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}