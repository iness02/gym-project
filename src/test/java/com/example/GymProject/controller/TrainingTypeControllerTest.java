package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.model.Trainings;
import com.example.GymProject.service.TrainingTypeService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TrainingTypeControllerTest {
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Mock
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTrainingTypes() {
        TrainingTypeDto trainingType1 = new TrainingTypeDto();
        trainingType1.setTrainingTypeName(Trainings.FITNESS);
        TrainingTypeDto trainingType2 = new TrainingTypeDto();
        trainingType2.setTrainingTypeName(Trainings.CYCLE);

        List<TrainingTypeDto> trainingTypes = Arrays.asList(trainingType1, trainingType2);

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        ResponseEntity<List<TrainingTypeDto>> response = trainingTypeController.getAllTrainingTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainingTypes, response.getBody());
    }
}
