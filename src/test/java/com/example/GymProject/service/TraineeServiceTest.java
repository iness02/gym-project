package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.InvalidCredentialsException;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.User;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})

class TraineeServiceTest {
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TraineeService traineeService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    @BeforeEach
    public void setUp() {
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
    public void testCreateTraineeThrowsExceptionWhenTraineeDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            traineeService.createTrainee(null);
        });
    }

    @Test
    public void testCreateTraineeThrowsExceptionWhenUserAlreadyExists() {
        TraineeDto traineeDto = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        traineeDto.setUserDto(userDto);

        when(userService.generateUniqueUserName("John", "Doe")).thenReturn("John.Doe0");
        when(traineeDao.existsByUsername("John.Doe0")).thenReturn(true);

        assertThrows(UserAlreadyRegisteredException.class, () -> {
            traineeService.createTrainee(traineeDto);
        });

        verify(userService, times(1)).generateUniqueUserName("John", "Doe");
        verify(traineeDao, times(1)).existsByUsername("John.Doe0");
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

/*
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
*/


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
