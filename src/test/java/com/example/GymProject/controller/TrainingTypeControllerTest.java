package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.service.TrainingTypeService;
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


        List<TrainingTypeDto> trainingTypes = Arrays.asList(new TrainingTypeDto(), new TrainingTypeDto());
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        ResponseEntity<?> response = trainingTypeController.getAllTrainingTypes(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trainingTypes, response.getBody());

        verify(trainingTypeService, times(1)).getAllTrainingTypes();
    }

}
