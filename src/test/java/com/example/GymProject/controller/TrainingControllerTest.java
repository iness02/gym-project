package com.example.GymProject.controller;

import com.example.GymProject.dto.request.AddTrainingRequestDto;
import com.example.GymProject.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTraining() {
        AddTrainingRequestDto request = new AddTrainingRequestDto("traineeUsername", "trainerUsername", "trainerPassword", "name", LocalDate.now(), 60);

        ResponseEntity<String> result = trainingController.addTraining(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(trainingService, times(1)).addTraining(any());
    }
}
