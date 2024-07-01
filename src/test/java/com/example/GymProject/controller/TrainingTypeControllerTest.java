package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.service.TrainingTypeService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TrainingTypeControllerTest {
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Mock
    private UserService userService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTrainingTypes() {

        UserPassRequestDto request = new UserPassRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);

        List<TrainingTypeDto> trainingTypes = Arrays.asList(new TrainingTypeDto(), new TrainingTypeDto());
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        ResponseEntity<?> response = trainingTypeController.getAllTrainingTypes(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainingTypes, response.getBody());

        verify(userService, times(1)).checkUsernameAndPassword("username", "password");
        verify(trainingTypeService, times(1)).getAllTrainingTypes();
    }

    @Test
    public void testGetAllTrainingTypesUnauthorized() {

        UserPassRequestDto request = new UserPassRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(false);

        ResponseEntity<?> response = trainingTypeController.getAllTrainingTypes(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Authentication failed for user:username", response.getBody());

        verify(userService, times(1)).checkUsernameAndPassword("username", "password");
        verify(trainingTypeService, never()).getAllTrainingTypes();
    }
}
