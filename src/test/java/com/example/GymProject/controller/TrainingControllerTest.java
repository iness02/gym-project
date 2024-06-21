package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.dto.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.service.TrainingService;
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
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTraining() {
        AddTrainingRequest request = new AddTrainingRequest();
        TrainingDto trainingDto = new TrainingDto();
        when(entityMapper.toTrainingDto(request)).thenReturn(trainingDto);

        ResponseEntity<String> responseEntity = trainingController.addTraining(request);

        verify(entityMapper, times(1)).toTrainingDto(request);
        verify(trainingService, times(1)).addTraining(trainingDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
