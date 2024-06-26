package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dao.TrainingTypeDao;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class TrainingServiceTest {
    @Mock
    private TrainingDao trainingDao;
    @Mock
    private UserService userService;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private TrainingTypeDao trainingTypeDao;
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTraining() {
        TrainingDto trainingDto = new TrainingDto();
        Training training = new Training();
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("username");
        User user1 = new User();
        trainer.setUser(user1);
        trainee.setUser(user);
        TrainingType trainingType = new TrainingType();

        when(entityMapper.toTraining(trainingDto)).thenReturn(training);
        when(entityMapper.toTrainee(trainingDto.getTrainee())).thenReturn(trainee);
        when(entityMapper.toTrainer(trainingDto.getTrainer())).thenReturn(trainer);
        when(entityMapper.toTrainingType(trainingDto.getTrainingType())).thenReturn(trainingType);
        when(trainingDao.addTraining(training)).thenReturn(training);
        when(entityMapper.toTrainingDto(training)).thenReturn(trainingDto);

        TrainingDto result = trainingService.addTraining(trainingDto);

        assertEquals(trainingDto, result);
        verify(entityMapper).toTraining(trainingDto);
        verify(entityMapper).toTrainee(trainingDto.getTrainee());
        verify(entityMapper).toTrainer(trainingDto.getTrainer());
        verify(entityMapper).toTrainingType(trainingDto.getTrainingType());
        verify(trainingDao).addTraining(training);
        verify(entityMapper).toTrainingDto(training);
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        List<TrainingDto> trainingDtos = Arrays.asList(new TrainingDto(), new TrainingDto());

        when(trainingDao.getAllTrainings()).thenReturn(trainings);
        when(entityMapper.toTrainingDto(any(Training.class)))
                .thenReturn(trainingDtos.get(0), trainingDtos.get(1));

        List<TrainingDto> result = trainingService.getAllTrainings();


        verify(trainingDao, times(1)).getAllTrainings();
        verify(entityMapper, times(2)).toTrainingDto(any(Training.class));  // Expected to be called twice
        assertEquals(trainingDtos, result);
    }

    @Test
    public void testUpdateTraining() {
        String username = "user1";
        String password = "password";
        TrainingDto trainingDto = new TrainingDto();
        Training training = new Training();

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(true);
        when(entityMapper.toTraining(trainingDto)).thenReturn(training);
        when(trainingDao.updateTraining(training)).thenReturn(training);
        when(entityMapper.toTrainingDto(training)).thenReturn(trainingDto);

        TrainingDto result = trainingService.updateTraining(trainingDto, username, password);

        assertEquals(trainingDto, result);
        verify(userService).checkUsernameAndPassword(username, password);
        verify(entityMapper).toTraining(trainingDto);
        verify(trainingDao).updateTraining(training);
        verify(entityMapper).toTrainingDto(training);
    }

    @Test
    public void testUpdateTrainingAuthenticationFailed() {
        String username = "user1";
        String password = "password";
        TrainingDto trainingDto = new TrainingDto();

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(false);


        assertEquals(null, trainingService.updateTraining(trainingDto, username, password));
        verify(userService).checkUsernameAndPassword(username, password);
        verify(entityMapper, never()).toTraining(trainingDto);
        verify(trainingDao, never()).updateTraining(any(Training.class));
        verify(entityMapper, never()).toTrainingDto(any(Training.class));
    }

    @Test
    void testIsAuthenticated_ValidCredentials() {
        String username = "validUser";
        String password = "validPassword";

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(true);
        assertTrue(trainingService.isAuthenticated(username, password));
    }

    @Test
    void testIsAuthenticated_InvalidCredentials() {
        String username = "invalidUser";
        String password = "invalidPassword";

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(false);
        assertFalse(trainingService.isAuthenticated(username, password));
    }
}
