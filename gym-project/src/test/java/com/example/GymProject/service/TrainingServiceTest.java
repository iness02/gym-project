package com.example.GymProject.service;

import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.dto.request.AddTrainingRequestDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.*;
import com.example.GymProject.repository.TraineeRepository;
import com.example.GymProject.repository.TrainerRepository;
import com.example.GymProject.repository.TrainingRepository;
import com.example.GymProject.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TrainingService trainingService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTraining() {
        AddTrainingRequestDto requestDto = new AddTrainingRequestDto("trainee1", "trainer1", "password", "TrainingName", LocalDate.now(), 60);

        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(Trainings.FITNESS);

        Training training = new Training();
        training.setId(1L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(requestDto.getName());
        training.setTrainingDate(requestDto.getDate());
        training.setTrainingDuration(requestDto.getDuration());
        training.setTrainingType(trainingType);

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(1L);
        trainingDto.setTrainingDate(LocalDate.now());
        trainingDto.setTrainingType(new TrainingTypeDto());
        trainingDto.setTrainingName("training name");
        trainingDto.setTrainingDuration(100);
        trainingDto.setTrainee(new TraineeDto());
        trainingDto.setTrainer(new TrainerDto());

        when(entityMapper.toTrainingDto(any(AddTrainingRequestDto.class))).thenReturn(trainingDto);
        when(entityMapper.toTraining(any(TrainingDto.class))).thenReturn(training);
        when(entityMapper.toTrainee(any(TraineeDto.class))).thenReturn(trainee);
        when(entityMapper.toTrainer(any(TrainerDto.class))).thenReturn(trainer);
        when(entityMapper.toTrainingDto(any(Training.class))).thenReturn(trainingDto);
        when(entityMapper.toTrainingType(any(TrainingTypeDto.class))).thenReturn(trainingType);

        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(any())).thenReturn(null);
        when(traineeRepository.getTraineeByUserUsername(anyString())).thenReturn(trainee);
        when(trainerRepository.getTrainerByUserUsername(anyString())).thenReturn(trainer);
        when(trainingRepository.save(any(Training.class))).thenReturn(training);


        TrainingDto result = trainingService.addTraining(requestDto);

        assertNotNull(result);

        assertEquals(trainingDto.getId(), result.getId(), "Expected TrainingDto ID to match");

        verify(entityMapper).toTrainingDto(any(AddTrainingRequestDto.class));
        verify(entityMapper).toTraining(any(TrainingDto.class));
        verify(entityMapper).toTrainee(any(TraineeDto.class));
        verify(entityMapper).toTrainer(any(TrainerDto.class));
        verify(entityMapper).toTrainingType(any(TrainingTypeDto.class));
        verify(trainingRepository).save(any(Training.class));
    }


    @Test
    public void testGetAllTrainings() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        List<TrainingDto> trainingDtos = Arrays.asList(new TrainingDto(), new TrainingDto());

        when(trainingRepository.findAll()).thenReturn(trainings);
        when(entityMapper.toTrainingDto(any(Training.class)))
                .thenReturn(trainingDtos.get(0), trainingDtos.get(1));

        List<TrainingDto> result = trainingService.getAllTrainings();


        verify(trainingRepository, times(1)).findAll();
        verify(entityMapper, times(2)).toTrainingDto(any(Training.class));  // Expected to be called twice
        assertEquals(trainingDtos, result);
    }

    @Test
    public void testUpdateTraining() {
        String username = "user1";
        String password = "password";
        TrainingDto trainingDto = new TrainingDto();
        Training training = new Training();

        when(entityMapper.toTraining(trainingDto)).thenReturn(training);
        when(trainingRepository.save(training)).thenReturn(training);
        when(entityMapper.toTrainingDto(training)).thenReturn(trainingDto);

        TrainingDto result = trainingService.updateTraining(trainingDto, username, password);

        assertEquals(trainingDto, result);
        verify(entityMapper).toTraining(trainingDto);
        verify(trainingRepository).save(training);
        verify(entityMapper).toTrainingDto(training);
    }


}
