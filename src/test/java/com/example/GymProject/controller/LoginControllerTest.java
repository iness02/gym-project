package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.request.ChangePasswordRequest;
import com.example.GymProject.dto.request.UserPassRequest;
import com.example.GymProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})

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
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);

        ResponseEntity<String> response = loginController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLogin_Unsuccessful() {
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(false);

        ResponseEntity<String> response = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testChangePassword_Successful() {
        ChangePasswordRequest request = new ChangePasswordRequest("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(true);
        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(true);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_Authentication() {
        ChangePasswordRequest request = new ChangePasswordRequest("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(false);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testChangePassword_Unsuccessful_PasswordChange() {
        ChangePasswordRequest request = new ChangePasswordRequest("username", "oldPassword", "newPassword");

        when(userService.checkUsernameAndPassword("username", "oldPassword")).thenReturn(true);
        when(userService.changePassword("username", "newPassword", "oldPassword")).thenReturn(false);

        ResponseEntity<String> response = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
