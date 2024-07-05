package com.example.GymProject.service;

import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.TraineeRegistrationRequestDto;
import com.example.GymProject.dto.request.TraineeTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTraineeProfileRequestDto;
import com.example.GymProject.dto.respone.*;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TraineeServiceTest {
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
    private TrainerService trainerService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto("John", "Doe","password", LocalDate.now(), "Address");
        String username = "johndoe";
        Trainee trainee = new Trainee();

        when(userService.generateUniqueUserName(anyString(), anyString())).thenReturn(username);

        when(traineeRepository.existsByUserUsername(username)).thenReturn(false);
        when(trainerRepository.existsByUserUsername(username)).thenReturn(false);


        when(entityMapper.toTrainee(any(TraineeRegistrationRequestDto.class))).thenReturn(trainee);

        when(userRepository.save(any())).thenReturn(new User());
        when(traineeRepository.save(any())).thenReturn(trainee);

        UserPassResponseDto responseDto = traineeService.createTrainee(requestDto);

        assertNotNull(responseDto);
        assertEquals(trainee.getId(), responseDto.getId());
        assertEquals(username, responseDto.getUsername());

        verify(userService).generateUniqueUserName(requestDto.getFirstName(), requestDto.getLastName());
        verify(traineeRepository).existsByUserUsername(username);
        verify(trainerRepository).existsByUserUsername(username);
        verify(userRepository).save(any());
        verify(traineeRepository).save(any());
        verify(entityMapper).toTrainee(requestDto);
    }


    @Test
    void testGetTraineeByUsername() {
        String username = "existingUser";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        traineeDto.setUserDto(userDto);

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(trainee);

        when(entityMapper.toTraineeDto(trainee)).thenReturn(traineeDto);
        when(entityMapper.toTraineeProfileResponseDto(traineeDto)).thenReturn(new TraineeProfileResponseDto());

        TraineeProfileResponseDto profileDto = traineeService.getTraineeByUsername(username);

        assertNotNull(profileDto);

        verify(traineeRepository).getTraineeByUserUsername(username);
        verify(entityMapper).toTraineeDto(trainee);
        verify(entityMapper).toTraineeProfileResponseDto(traineeDto);
    }

    @Test
    void testGetTraineeByUsername_NotFound() {
        String username = "nonExistingUser";

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> traineeService.getTraineeByUsername(username));

        verify(traineeRepository).getTraineeByUserUsername(username);
        verifyNoMoreInteractions(entityMapper);
    }

    @Test
    void testUpdateTrainee() {
        UpdateTraineeProfileRequestDto requestDto = new UpdateTraineeProfileRequestDto();
        requestDto.setUsername("username");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setPassword("newPassword");
        requestDto.setIsActive(true);
        requestDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        requestDto.setAddress("New Address");

        User existingUser = new User();
        existingUser.setUsername(requestDto.getUsername());
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");
        existingUser.setPassword("oldPassword");
        existingUser.setIsActive(false);

        Trainee existingTrainee = new Trainee();
        existingTrainee.setUser(existingUser);
        existingTrainee.setDateOfBirth(LocalDate.of(1980, 1, 1));
        existingTrainee.setAddress("Old Address");

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(existingUser);
        when(traineeRepository.findByUser(existingUser)).thenReturn(existingTrainee);

        when(entityMapper.toUpdateTraineeProfileResponseDto(any())).thenReturn(new UpdateTraineeProfileResponseDto());

        UpdateTraineeProfileResponseDto responseDto = traineeService.updateTrainee(requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getUsername(), existingUser.getUsername());
        assertEquals(requestDto.getFirstName(), existingUser.getFirstName());
        assertEquals(requestDto.getLastName(), existingUser.getLastName());
        assertEquals(requestDto.getPassword(), existingUser.getPassword());
        assertEquals(requestDto.getIsActive(), existingUser.getIsActive());
        assertEquals(requestDto.getDateOfBirth(), existingTrainee.getDateOfBirth());
        assertEquals(requestDto.getAddress(), existingTrainee.getAddress());

        verify(userRepository).findByUsername(requestDto.getUsername());
        verify(traineeRepository).findByUser(existingUser);
        verify(userRepository).save(existingUser);
        verify(traineeRepository).save(existingTrainee);
        verify(entityMapper).toUpdateTraineeProfileResponseDto(any());
    }


    @Test
    void testUpdateTrainee_UserNotFound() {
        UpdateTraineeProfileRequestDto requestDto = new UpdateTraineeProfileRequestDto();
        requestDto.setUsername("nonExistingUser");

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> traineeService.updateTrainee(requestDto));

        verify(userRepository).findByUsername(requestDto.getUsername());
        verifyNoMoreInteractions(traineeRepository);
        verifyNoMoreInteractions(entityMapper);
    }

    @Test
    void testDeleteTraineeByUsername() {
        String username = "johndoe";
        String password = "password";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);
        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(trainee);

        assertTrue(traineeService.deleteTraineeByUsername(username, password));
    }


    @Test
    void testActivate() {
        String username = "John.Doe";
        String password = "password";
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = traineeService.activate(username, password);

        assertTrue(result);
        assertTrue(user.getIsActive());
        verify(traineeRepository).getTraineeByUserUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testActivate_TraineeNotFound() {
        String username = "John.Doe";
        String password = "password";

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            traineeService.activate(username, password);
        });

        verify(traineeRepository).getTraineeByUserUsername(username);
        verify(userService, never()).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate() {
        String username = "John.Doe";
        String password = "password";
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = traineeService.deactivate(username, password);

        assertTrue(result);
        assertFalse(user.getIsActive());
        verify(traineeRepository).getTraineeByUserUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate_NotFound() {
        when(traineeRepository.getTraineeByUserUsername("username")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            traineeService.deactivate("username", "password");
        });
    }

    @Test
    void testGetTraineeTrainings() {
        TraineeTrainingsRequestDto requestDto = new TraineeTrainingsRequestDto("johndoe", "password", LocalDate.now(), LocalDate.now(), "Trainer", "Type");

        List<Training> trainings = Collections.singletonList(new Training());
        when(trainingRepository.findTraineeTrainings(anyString(), any(), any(), any(), any())).thenReturn(trainings);
        when(entityMapper.toGetTrainingResponseDto(any())).thenReturn(new GetTrainingResponseDto());

        List<GetTrainingResponseDto> responseDtos = traineeService.getTraineeTrainings(requestDto);

        assertFalse(responseDtos.isEmpty());
    }

    @Test
    void testGetUnassignedTrainers() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());
        when(traineeRepository.getTraineeByUserUsername("username")).thenReturn(trainee);

        List<Trainer> allTrainers = new LinkedList<>();
        allTrainers.add(new Trainer());
        when(trainerRepository.findAll()).thenReturn(allTrainers);

        TrainerDto trainerDto = new TrainerDto();
        when(entityMapper.toTrainerDto(any())).thenReturn(trainerDto);

        List<UnassignedTrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainers("username", "password");

        assertEquals(1, unassignedTrainers.size());
        verify(traineeRepository).getTraineeByUserUsername("username");
        verify(trainerRepository).findAll();
        verify(entityMapper, times(allTrainers.size())).toTrainerDto(any());
    }

    @Test
    void testUpdateTraineeTrainers() {
        String username = "traineeUsername";
        String password = "password";
        Set<String> trainerUsernames = new HashSet<>();
        trainerUsernames.add("trainer1");
        trainerUsernames.add("trainer2");

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.setId(1L);
        trainee.getUser().setId(1L);

        TrainerDto trainerDto1 = new TrainerDto();
        UserDto userDto1 = new UserDto();
        userDto1.setUsername("trainer1");
        userDto1.setFirstName("Trainer");
        userDto1.setLastName("One");
        trainerDto1.setUserDto(userDto1);
        trainerDto1.setSpecialization("Specialization 1");

        TrainerDto trainerDto2 = new TrainerDto();
        UserDto userDto2 = new UserDto();
        userDto2.setUsername("trainer2");
        userDto2.setFirstName("Trainer");
        userDto2.setLastName("Two");
        trainerDto2.setUserDto(userDto2);
        trainerDto2.setSpecialization("Specialization 2");

        List<TrainerForTraineeResponseDto> expectedResponses = new LinkedList<>();
        TrainerForTraineeResponseDto response1 = new TrainerForTraineeResponseDto();
        response1.setUserName("trainer1");
        response1.setFirstName("Trainer");
        response1.setLastName("One");
        response1.setSpecialization("Specialization 1");
        expectedResponses.add(response1);

        TrainerForTraineeResponseDto response2 = new TrainerForTraineeResponseDto();
        response2.setUserName("trainer2");
        response2.setFirstName("Trainer");
        response2.setLastName("Two");
        response2.setSpecialization("Specialization 2");
        expectedResponses.add(response2);

        when(traineeRepository.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(trainerService.getTrainerDtoByUsername("trainer1")).thenReturn(trainerDto1);
        when(trainerService.getTrainerDtoByUsername("trainer2")).thenReturn(trainerDto2);
        when(entityMapper.toTrainer(trainerDto1)).thenReturn(new Trainer());
        when(entityMapper.toTrainer(trainerDto2)).thenReturn(new Trainer());

        List<TrainerForTraineeResponseDto> actualResponses = traineeService.updateTraineeTrainers(username, password, trainerUsernames);

        assertNotNull(actualResponses);
        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses.get(0).getUserName(), actualResponses.get(1).getUserName());
        assertEquals(expectedResponses.get(0).getFirstName(), actualResponses.get(1).getFirstName());
        assertEquals(expectedResponses.get(0).getLastName(), actualResponses.get(1).getLastName());
        assertEquals(expectedResponses.get(0).getSpecialization(), actualResponses.get(1).getSpecialization());

        assertEquals(expectedResponses.get(1).getUserName(), actualResponses.get(0).getUserName());
        assertEquals(expectedResponses.get(1).getFirstName(), actualResponses.get(0).getFirstName());
        assertEquals(expectedResponses.get(1).getLastName(), actualResponses.get(0).getLastName());
        assertEquals(expectedResponses.get(1).getSpecialization(), actualResponses.get(0).getSpecialization());

        verify(traineeRepository).getTraineeByUserUsername(username);
        verify(traineeRepository).save(trainee);
        verify(entityMapper, times(2)).toTrainer(any(TrainerDto.class));
    }
}
