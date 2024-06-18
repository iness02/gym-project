/*
package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.User;
import com.example.GymProject.request.UserPassRequest;
import com.example.GymProject.response.UserPassResponse;
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
@ContextConfiguration(classes = {AppConfig.class})
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
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("user.name");
        userDto.setPassword("testPass");
        traineeDto.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("user");
        user.setLastName("name");
        trainee.setUser(user);

        when(entityMapper.toTrainee(any(TraineeDto.class))).thenReturn(trainee);
        when(entityMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userService.generateUniqueUserName(anyString(), anyString())).thenReturn("user.name");
        when(traineeDao.createTrainee(any(Trainee.class))).thenReturn(trainee);

        UserPassResponse result = traineeService.createTrainee(traineeDto);

        verify(userDao).createUser(any(User.class));
        verify(traineeDao).createTrainee(any(Trainee.class));

        assertEquals("user.name", result.getUsername());
        assertEquals("testPass", result.getPassword());
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

  */
/*  @Test
    public void testUpdateTrainee() {
        TraineeDto traineeDTO = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("InesaHak");
        traineeDTO.setUserDto(userDto);

        Trainee trainee = new Trainee();
        User user = new User();
        Trainee updatedTrainee = new Trainee();
        TraineeDto updatedTraineeDto = new TraineeDto();
        UpdateTraineeProfileResponse response = new UpdateTraineeProfileResponse();

        String password = "password";

        when(entityMapper.toTrainee(traineeDTO)).thenReturn(trainee);
        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(entityMapper.toTraineeDto(updatedTrainee)).thenReturn(updatedTraineeDto);
        when(entityMapper.toUpdateTraineeProfileResponse(updatedTraineeDto)).thenReturn(response);
        when(traineeDao.updateTrainee(trainee)).thenReturn(updatedTrainee);
        when(userService.matchUsernameAndPassword(userDto.getUsername(), password)).thenReturn(true);

        UpdateTraineeProfileResponse result = traineeService.updateTrainee(traineeDTO);

        verify(userService, times(1)).matchUsernameAndPassword(userDto.getUsername(), password);
        verify(traineeDao, times(1)).updateTrainee(trainee);
        verify(entityMapper, times(1)).toTraineeDto(updatedTrainee);

        assertEquals(response, result);
        assertEquals(user, trainee.getUser());
    }
*//*

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
    public void testActivateUser() {
        String username = "testuser";
        String password = "testpass";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        user.setIsActive(false);
        trainee.setUser(user);

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());
        when(traineeService.isAuthenticated(username,password)).thenReturn(true);
        traineeService.activate(username, password);

        verify(userService).updateUser(any(UserDto.class));
        assertTrue(user.getIsActive());
    }

    @Test
    public void testDeactivateUser() {
        String username = "testuser";
        String password = "testpass";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        user.setIsActive(true);
        trainee.setUser(user);

        when(traineeDao.getTraineeByUsername(username)).thenReturn(trainee);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());
        when(traineeService.isAuthenticated(username,password)).thenReturn(true);

        traineeService.activate(username, password);

        verify(userService).updateUser(any(UserDto.class));
        assertTrue(user.getIsActive());
    }

  */
/*  @Test
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
*//*

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

    @Test
    void testIsAuthenticated_ValidCredentials() {
        String username = "validUser";
        String password = "validPassword";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        assertTrue(traineeService.isAuthenticated(username, password));
    }

    @Test
    void testIsAuthenticated_InvalidCredentials() {
        String username = "invalidUser";
        String password = "invalidPassword";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(false);
        assertFalse(traineeService.isAuthenticated(username, password));
    }
}
*/
