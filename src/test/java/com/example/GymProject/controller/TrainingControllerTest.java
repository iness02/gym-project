package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.service.TrainingService;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;

    @Mock
    private EntityMapper entityMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTraining() {
        AddTrainingRequest request = new AddTrainingRequest("traineeUsername", "trainerUsername", "trainerPassword", "name", new Date(), 60);

        when(userService.checkUsernameAndPassword(request.getTrainerUsername(), request.getTrainerPassword())).thenReturn(true);

        ResponseEntity<String> result = trainingController.addTraining(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(trainingService, times(1)).addTraining(any());
    }

    @Test
    public void testAddTrainingAuthenticationFailure() {
        AddTrainingRequest request = new AddTrainingRequest("traineeUsername", "trainerUsername", "trainerPassword", "name", new Date(), 60);

        when(userService.checkUsernameAndPassword(request.getTrainerUsername(), request.getTrainerPassword())).thenReturn(false);

        ResponseEntity<String> result = trainingController.addTraining(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authentication failed for user:" + request.getTrainerUsername(), result.getBody());
        verify(trainingService, never()).addTraining(any()); // Verify that the addTraining method was never called
    }

}
