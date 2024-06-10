package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeDto traineeDTO = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("test");
        userDto.setLastName("user");
        traineeDTO.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();
        Trainee createdTrainee = new Trainee();
        TraineeDto createdTraineeDto = new TraineeDto();

        when(entityMapper.toTrainee(traineeDTO)).thenReturn(trainee);
        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(userService.generateUniqueUserName(user.getFirstName(), user.getLastName())).thenReturn("test.user");
        when(entityMapper.toTraineeDto(createdTrainee)).thenReturn(createdTraineeDto);
        when(traineeDao.createTrainee(any(Trainee.class))).thenReturn(createdTrainee);

        TraineeDto result = traineeService.createTrainee(traineeDTO);

        verify(userDao, times(1)).createUser(user);
        verify(traineeDao, times(1)).createTrainee(trainee);
        verify(entityMapper, times(1)).toTraineeDto(createdTrainee);

        assertEquals(createdTraineeDto, result);
        assertEquals("test.user", user.getUsername());
        assertEquals(true, user.getIsActive());
        assertEquals(user, trainee.getUser());
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        TraineeDto traineeDto = new TraineeDto();
        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toTraineeDto(trainee)).thenReturn(traineeDto);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        TraineeDto result = traineeService.getTraineeByUsername(username, password);

        assertNotNull(result);
        assertEquals(traineeDto, result);
    }

    @Test
    public void testUpdateTrainee() {
        TraineeDto traineeDTO = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("InesaHak");
        traineeDTO.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();
        Trainee updatedTrainee = new Trainee();
        TraineeDto updatedTraineeDto = new TraineeDto();
        String password = "password";

        when(entityMapper.toTrainee(traineeDTO)).thenReturn(trainee);
        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(entityMapper.toTraineeDto(updatedTrainee)).thenReturn(updatedTraineeDto);
        when(traineeDao.updateTrainee(trainee)).thenReturn(updatedTrainee);
        when(userService.matchUsernameAndPassword(userDto.getUsername(), password)).thenReturn(true);

        TraineeDto result = traineeService.updateTrainee(traineeDTO, password);

        verify(userService, times(1)).matchUsernameAndPassword(userDto.getUsername(), password);
        verify(traineeDao, times(1)).updateTrainee(trainee);
        verify(entityMapper, times(1)).toTraineeDto(updatedTrainee);

        assertEquals(updatedTraineeDto, result);
        assertEquals(user, trainee.getUser());
    }

    @Test
    public void testDeleteTraineeByUsername() {
        String username = "testUser";
        String password = "testPassword";
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        traineeService.deleteTraineeByUsername(username, password);

        verify(traineeDao, times(1)).deleteTraineeByUsername(username);
    }

    @Test
    public void testMatchUsernameAndPassword() {
        String username = "testUser";
        String password = "testPassword";
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        boolean result = traineeService.matchUsernameAndPassword(username, password);

        assertTrue(result);
    }

    @Test
    public void testChangeTraineePassword() {
        String username = "testUser";
        String password = "testPassword";
        String newPassword = "newPassword";
        UserDto userDto = new UserDto();
        userDto.setPassword(password);
        when(userService.getUserByUsername(username)).thenReturn(userDto);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        traineeService.changeTraineePassword(username, newPassword, password);

        verify(userService, times(1)).updateUser(any(UserDto.class));
    }

    @Test
    void activateDeactivateTrainer() {
        String username = "john.doe";
        String password = "password";
        boolean isActive = true;

        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);
        UserDto userDto = new UserDto();

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(entityMapper.toUserDto(user)).thenReturn(userDto);

        traineeService.activateDeactivateTrainee(username, isActive, password);

        verify(userService, times(1)).matchUsernameAndPassword(username, password);
        verify(traineeDao, times(1)).getTraineeByUsername(username);
        verify(userService, times(1)).updateUser(userDto);
    }

    @Test
    public void testGetTraineeTrainings() {
        String username = "testUser";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingType = "type";
        String password = "password";
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        List<TrainingDto> trainingDtos = Arrays.asList(new TrainingDto(), new TrainingDto());
        when(traineeDao.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType)).thenReturn(trainings);
        when(entityMapper.toTrainingDto(any(Training.class))).thenReturn(new TrainingDto());
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        List<TrainingDto> result = traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType, password);

        assertNotNull(result);
        assertEquals(trainingDtos.size(), result.size());
    }

    @Test
    public void testUpdateTraineeTrainers() {
        String username = "testUser";
        Set<TrainerDto> trainerDtos = new HashSet<>(Arrays.asList(new TrainerDto(), new TrainerDto()));
        String password = "password";
        Trainee trainee = new Trainee();
        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        traineeService.updateTraineeTrainers(username, trainerDtos, password);

        verify(traineeDao, times(1)).updateTrainee(any(Trainee.class));
    }
}
