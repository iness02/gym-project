package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.request.ChangePasswordRequest;
import com.example.GymProject.request.UserPassRequest;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})

public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    @Test
    public void testLogin_Success() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("testuser");
        request.setPassword("testpassword");

        when(userService.matchUsernameAndPassword("testuser", "testpassword")).thenReturn(true);

        ResponseEntity<String> responseEntity = loginController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).matchUsernameAndPassword("testuser", "testpassword");
    }

    @Test
    public void testLogin_Unauthorized() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        when(userService.matchUsernameAndPassword("testuser", "wrongpassword")).thenReturn(false);

        ResponseEntity<String> responseEntity = loginController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, times(1)).matchUsernameAndPassword("testuser", "wrongpassword");
    }

    @Test
    public void testChangePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUsername("testuser");
        request.setNewPassword("newpassword");
        request.setOldPassword("oldpassword");

        when(userService.changePassword("testuser", "newpassword", "oldpassword")).thenReturn(true);

        ResponseEntity<String> responseEntity = loginController.changePassword(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).changePassword("testuser", "newpassword", "oldpassword");
    }

    @Test
    public void testChangePassword_Unauthorized() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUsername("testuser");
        request.setNewPassword("newpassword");
        request.setOldPassword("wrongpassword");

        when(userService.changePassword("testuser", "newpassword", "wrongpassword")).thenReturn(false);

        ResponseEntity<String> responseEntity = loginController.changePassword(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(userService, times(1)).changePassword("testuser", "newpassword", "wrongpassword");
    }
}
