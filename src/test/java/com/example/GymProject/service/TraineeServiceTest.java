package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import com.example.GymProject.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class TraineeServiceTest {
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserService userService;

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
    public void testCreateTrainee() {
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        traineeDto.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();
        Trainee savedTrainee = new Trainee();
        savedTrainee.setId(1L);
        user.setUsername("John.Doe0");
        user.setPassword(Utils.generatePassword());
        savedTrainee.setUser(user);

        when(userService.generateUniqueUserName("John", "Doe")).thenReturn("John.Doe0");
        when(traineeDao.existsByUsername("John.Doe0")).thenReturn(false);
        when(trainerDao.existsByUsername("John.Doe0")).thenReturn(false);
        when(entityMapper.toTrainee(traineeDto)).thenReturn(trainee);
        when(entityMapper.toUser(traineeDto.getUserDto())).thenReturn(user);
        doNothing().when(userDao).createUser(user);
        when(traineeDao.createTrainee(trainee)).thenReturn(savedTrainee);

        UserPassResponse response = traineeService.createTrainee(traineeDto);

        verify(userService, times(1)).generateUniqueUserName("John", "Doe");
        verify(traineeDao, times(1)).existsByUsername("John.Doe0");
        verify(trainerDao, times(1)).existsByUsername("John.Doe0");
        verify(entityMapper, times(1)).toTrainee(traineeDto);
        verify(entityMapper, times(1)).toUser(traineeDto.getUserDto());
        verify(userDao, times(1)).createUser(user);
        verify(traineeDao, times(1)).createTrainee(trainee);

        assertNotNull(response);
        assertEquals(savedTrainee.getId(), response.getId());
        assertEquals(savedTrainee.getUser().getUsername(), response.getUsername());
        assertEquals(savedTrainee.getUser().getPassword(), response.getPassword());
    }

    @Test
    void testCreateTrainee_UserAlreadyRegistered() {
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        traineeDto.setUserDto(userDto);

        String username = "John.Doe";

        when(userService.generateUniqueUserName(userDto.getFirstName(), userDto.getLastName())).thenReturn(username);
        when(traineeDao.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyRegisteredException.class, () -> {
            traineeService.createTrainee(traineeDto);
        });

        verify(userService).generateUniqueUserName(userDto.getFirstName(), userDto.getLastName());
        verify(traineeDao).existsByUsername(username);
        verify(trainerDao, never()).existsByUsername(username);
        verify(userDao, never()).createUser(any(User.class));
        verify(traineeDao, never()).createTrainee(any(Trainee.class));
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "John.Doe";
        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        TraineeDto traineeDto = new TraineeDto();

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toTraineeDto(trainee)).thenReturn(traineeDto);
        when(entityMapper.toGetTraineeProfileResponse(traineeDto)).thenReturn(new GetTraineeProfileResponse());

        GetTraineeProfileResponse response = traineeService.getTraineeByUsername(username);

        assertNotNull(response);
        verify(traineeDao).getTraineeByUsername(username);
        verify(entityMapper).toTraineeDto(trainee);
        verify(entityMapper).toGetTraineeProfileResponse(traineeDto);
    }

    @Test
    void testGetTraineeByUsername_NotFound() {
        String username = "John.Doe";

        when(traineeDao.getTraineeByUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            traineeService.getTraineeByUsername(username);
        });

        verify(traineeDao).getTraineeByUsername(username);
        verify(entityMapper, never()).toTraineeDto(any(Trainee.class));
        verify(entityMapper, never()).toGetTraineeProfileResponse(any(TraineeDto.class));
    }

    @Test
    void testUpdateTrainee() {
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        traineeDto.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();

        when(entityMapper.toTrainee(traineeDto)).thenReturn(trainee);
        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(userDao.updateUser(user)).thenReturn(user);
        when(traineeDao.updateTrainee(trainee)).thenReturn(trainee);
        when(entityMapper.toTraineeDto(trainee)).thenReturn(traineeDto);
        when(entityMapper.toUpdateTraineeProfileResponse(traineeDto)).thenReturn(new UpdateTraineeProfileResponse());

        UpdateTraineeProfileResponse response = traineeService.updateTrainee(traineeDto);

        assertNotNull(response);
        verify(entityMapper).toTrainee(traineeDto);
        verify(entityMapper).toUser(userDto);
        verify(userDao).updateUser(user);
        verify(traineeDao).updateTrainee(trainee);
        verify(entityMapper).toTraineeDto(trainee);
        verify(entityMapper).toUpdateTraineeProfileResponse(traineeDto);
    }
/*
    @Test
    void testDeleteTraineeByUsername() {
        String username = "John.Doe";
        String password = "password";

        boolean result = traineeService.deleteTraineeByUsername(username, password);

        assertTrue(result);
        verify(traineeDao).deleteTraineeByUsername(username);
    }*/

    @Test
    void testActivate() {
        String username = "John.Doe";
        String password = "password";
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = traineeService.activate(username, password);

        assertTrue(result);
        assertTrue(user.getIsActive());
        verify(traineeDao).getTraineeByUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testActivate_TraineeNotFound() {
        String username = "John.Doe";
        String password = "password";

        when(traineeDao.getTraineeByUsername(username)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            traineeService.activate(username, password);
        });

        verify(traineeDao).getTraineeByUsername(username);
        verify(userService, never()).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate() {
        String username = "John.Doe";
        String password = "password";
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        boolean result = traineeService.deactivate(username, password);

        assertTrue(result);
        assertFalse(user.getIsActive());
        verify(traineeDao).getTraineeByUsername(username);
        verify(userService).updateUser(any(UserDto.class));
    }

    @Test
    void testDeactivate_NotFound() {
        when(traineeDao.getTraineeByUsername("username")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            traineeService.deactivate("username", "password");
        });
    }

    @Test
    void testGetUnassignedTrainers() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());
        when(traineeDao.getTraineeByUsername("username")).thenReturn(trainee);

        List<Trainer> allTrainers = new LinkedList<>();
        allTrainers.add(new Trainer());
        when(trainerDao.getAllTrainers()).thenReturn(allTrainers);

        assertEquals(1, traineeService.getUnassignedTrainers("username", "password").size());
    }

    @Test
    public void testGetTraineeTrainings() {
        GetTraineeTrainingsRequest request = new GetTraineeTrainingsRequest("username", "password", new Date(), new Date(), "trainerName", "trainingType");
        List<Training> trainings = new ArrayList<>();
        when(traineeDao.getTraineeTrainings(anyString(), any(Date.class), any(Date.class), anyString(), anyString())).thenReturn(trainings);
        when(entityMapper.toGetTrainingResponse(any())).thenReturn(new GetTrainingResponse());
        List<GetTrainingResponse> responses = traineeService.getTraineeTrainings(request);
        assertNotNull(responses);
        assertEquals(0, responses.size());
    }

}
