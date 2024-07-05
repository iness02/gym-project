package com.example.GymProject.service;

import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.TrainerRegistrationRequestDto;
import com.example.GymProject.dto.request.TrainerTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTrainerProfileRequestDto;
import com.example.GymProject.dto.respone.GetTrainingResponseDto;
import com.example.GymProject.dto.respone.TrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UpdateTrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UserPassResponseDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import com.example.GymProject.repository.TraineeRepository;
import com.example.GymProject.repository.TrainerRepository;
import com.example.GymProject.repository.TrainingRepository;
import com.example.GymProject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TrainerServiceTest {
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainer() {
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto("John", "Doe","password", "Specialization");
        String username = "John.Doe";
        Trainer trainer = new Trainer();

        when(userService.generateUniqueUserName(anyString(), anyString())).thenReturn(username);
        when(traineeRepository.existsByUserUsername(username)).thenReturn(false);
        when(trainerRepository.existsByUserUsername(username)).thenReturn(false);
        when(entityMapper.toTrainer(any(TrainerRegistrationRequestDto.class))).thenReturn(trainer);
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        UserPassResponseDto responseDto = trainerService.createTrainer(requestDto);


        assertNotNull(responseDto, "ResponseDto is null");
        assertEquals(trainer.getId(), responseDto.getId());
        assertEquals(username, responseDto.getUsername());

        verify(userService).generateUniqueUserName(requestDto.getFirstName(), requestDto.getLastName());
        verify(traineeRepository).existsByUserUsername(username);
        verify(trainerRepository).existsByUserUsername(username);
        verify(userRepository).save(any(User.class));
        verify(trainerRepository).save(any(Trainer.class));
        verify(entityMapper).toTrainer(requestDto);
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "existingUser";
        Trainer trainer = new Trainer();
        trainer.setUser(new User());
        TrainerDto trainerDto = new TrainerDto();
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        trainerDto.setUserDto(userDto);

        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(trainer);

        when(entityMapper.toTrainerDto(trainer)).thenReturn(trainerDto);
        when(entityMapper.toTrainerProfileResponseDto(trainerDto)).thenReturn(new TrainerProfileResponseDto());

        TrainerProfileResponseDto profileDto = trainerService.getTrainerByUsername(username);

        assertNotNull(profileDto);

        verify(trainerRepository).getTrainerByUserUsername(username);
        verify(entityMapper).toTrainerDto(trainer);
        verify(entityMapper).toTrainerProfileResponseDto(trainerDto);
    }

    @Test
    void testGetTrainerByUsername_NotFound() {
        String username = "nonExistingUser";

        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> trainerService.getTrainerByUsername(username));

        verify(trainerRepository).getTrainerByUserUsername(username);
        verifyNoMoreInteractions(entityMapper);
    }

    @Test
    void testUpdateTrainer() {
        UpdateTrainerProfileRequestDto requestDto = new UpdateTrainerProfileRequestDto("John.Doe", "password", "John", "Doe", "Specialization", true);
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setFirstName("John");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(user);
        when(trainerRepository.findByUser(user)).thenReturn(trainer);

        UpdateTrainerProfileResponseDto expectedResponse = new UpdateTrainerProfileResponseDto();
        expectedResponse.setUserName(requestDto.getUsername());
        expectedResponse.setLastName(requestDto.getLastName());
        expectedResponse.setSpecialization(requestDto.getSpecialization());

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        when(entityMapper.toUpdateTrainerProfileResponseDto(any(Trainer.class))).thenReturn(expectedResponse);

        UpdateTrainerProfileResponseDto responseDto = trainerService.updateTrainer(requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getUsername(), responseDto.getUserName());
        assertEquals(requestDto.getLastName(), responseDto.getLastName());
        assertEquals(requestDto.getSpecialization(), responseDto.getSpecialization());

        verify(userRepository).findByUsername(requestDto.getUsername());
        verify(trainerRepository).findByUser(user);
        verify(userRepository).save(any(User.class));
        verify(trainerRepository).save(any(Trainer.class));
        verify(entityMapper).toUpdateTrainerProfileResponseDto(trainer);
    }

    @Test
    void testUpdateTrainer_UserNotFound() {
        UpdateTrainerProfileRequestDto requestDto = new UpdateTrainerProfileRequestDto();
        requestDto.setUsername("nonExistingUser");

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> trainerService.updateTrainer(requestDto));

        verify(userRepository).findByUsername(requestDto.getUsername());
        verifyNoMoreInteractions(trainerRepository);
        verifyNoMoreInteractions(entityMapper);
    }

    @Test
    void testDeleteTrainerByUsername() {
        String username = "johndoe";
        String password = "password";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        trainer.setUser(user);
        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(trainer);

        assertTrue(trainerService.deleteTrainerByUsername(username, password));
    }

    @Test
    void testActivate() {
        String username = "John.Doe";
        String password = "password";
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);

        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(trainer);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = trainerService.activate(username, password);

        assertTrue(result);
        assertTrue(user.getIsActive());
        verify(trainerRepository).getTrainerByUserUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testActivate_TraineeNotFound() {
        String username = "John.Doe";
        String password = "password";

        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            trainerService.activate(username, password);
        });

        verify(trainerRepository).getTrainerByUserUsername(username);
        verify(userService, never()).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate() {
        String username = "John.Doe";
        String password = "password";
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);

        when(trainerRepository.getTrainerByUserUsername(username)).thenReturn(trainer);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = trainerService.deactivate(username, password);

        assertTrue(result);
        assertFalse(user.getIsActive());
        verify(trainerRepository).getTrainerByUserUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate_NotFound() {
        when(trainerRepository.getTrainerByUserUsername("username")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            trainerService.deactivate("username", "password");
        });
    }

    @Test
    void testGetTrainerTrainings() {
        TrainerTrainingsRequestDto requestDto = new TrainerTrainingsRequestDto();
        requestDto.setUsername("trainer1");
        requestDto.setPeriodFrom(LocalDate.of(2023, 1, 1));
        requestDto.setPeriodTo(LocalDate.of(2023, 12, 31));
        requestDto.setTraineeName("trainee1");

        Training training = new Training();
        List<Training> trainingList = Collections.singletonList(training);

        TrainingDto trainingDto = new TrainingDto();
        GetTrainingResponseDto getTrainingResponseDto = new GetTrainingResponseDto();

        when(trainingRepository.findTrainerTrainings(anyString(), any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(trainingList);
        when(entityMapper.toTrainingDto(training)).thenReturn(trainingDto);
        when(entityMapper.toGetTrainingResponseDto(trainingDto)).thenReturn(getTrainingResponseDto);

        List<GetTrainingResponseDto> responseDtos = trainerService.getTrainerTrainings(requestDto);

        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals(getTrainingResponseDto, responseDtos.get(0));

        verify(trainingRepository).findTrainerTrainings(requestDto.getUsername(), requestDto.getPeriodFrom(), requestDto.getPeriodTo(), requestDto.getTraineeName());
        verify(entityMapper).toTrainingDto(training);
        verify(entityMapper).toGetTrainingResponseDto(trainingDto);
    }

}



