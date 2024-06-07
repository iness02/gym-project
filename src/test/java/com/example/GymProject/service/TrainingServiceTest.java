package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dto.TrainingDTO;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTraining() {
        TrainingDTO trainingDTO = new TrainingDTO();
        Training training = new Training();

        when(EntityMapper.INSTANCE.trainingDTOToTraining(trainingDTO)).thenReturn(training);
        when(trainingDAO.addTraining(training)).thenReturn(training);
        when(EntityMapper.INSTANCE.trainingToTrainingDTO(training)).thenReturn(trainingDTO);

        TrainingDTO result = trainingService.addTraining(trainingDTO);
        assertNotNull(result);
        verify(trainingDAO, times(1)).addTraining(training);
    }

    @Test
    void testGetAllTrainings() {
        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> trainings = Arrays.asList(training1, training2);
        TrainingDTO trainingDTO1 = new TrainingDTO();
        TrainingDTO trainingDTO2 = new TrainingDTO();

        when(trainingDAO.getAllTrainings()).thenReturn(trainings);
        when(EntityMapper.INSTANCE.trainingToTrainingDTO(training1)).thenReturn(trainingDTO1);
        when(EntityMapper.INSTANCE.trainingToTrainingDTO(training2)).thenReturn(trainingDTO2);

        List<TrainingDTO> result = trainingService.getAllTrainings();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trainingDAO, times(1)).getAllTrainings();
    }

    @Test
    void testUpdateTraining() {
        String username = "testUser";
        String password = "testPass";
        TrainingDTO trainingDTO = new TrainingDTO();
        Training training = new Training();

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(EntityMapper.INSTANCE.trainingDTOToTraining(trainingDTO)).thenReturn(training);
        when(trainingDAO.updateTraining(training)).thenReturn(training);
        when(EntityMapper.INSTANCE.trainingToTrainingDTO(training)).thenReturn(trainingDTO);

        TrainingDTO result = trainingService.updateTraining(trainingDTO, username, password);
        assertNotNull(result);
        verify(trainingDAO, times(1)).updateTraining(training);
        verify(userService, times(1)).matchUsernameAndPassword(username, password);
    }

    @Test
    void authenticate() {
        String username = "testUser";
        String password = "testPass";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        // If authenticate passes without exception, the test is successful
        assertDoesNotThrow(() -> trainingService.updateTraining(new TrainingDTO(), username, password));
        verify(userService, times(1)).matchUsernameAndPassword(username, password);
    }
}
