package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
class TraineeServiceTest{

    @Mock
    private TraineeDao traineeDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        TraineeDto traineeDTO = new TraineeDto();
        Trainee trainee = new Trainee();
        UserDto userDTO = new UserDto();
        userDTO.setUsername("testUser");

//        when(EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO)).thenReturn(trainee);
//        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(traineeDAO.createTrainee(trainee)).thenReturn(trainee);

        TraineeDto result = traineeService.createTrainee(traineeDTO);
        assertNotNull(result);

        verify(traineeDAO, times(1)).createTrainee(trainee);
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "testUser";
        String password = "testPass";
        Trainee trainee = new Trainee();
        TraineeDto traineeDTO = new TraineeDto();

        when(traineeDAO.getTraineeByUsername(username)).thenReturn(trainee);
//        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        TraineeDto result = traineeService.getTraineeByUsername(username, password);
        assertNotNull(result);

        verify(traineeDAO, times(1)).getTraineeByUsername(username);
    }

    @Test
    void testUpdateTrainee() {
        TraineeDto traineeDTO = new TraineeDto();
        String password = "testPass";
        Trainee trainee = new Trainee();

//        when(EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO)).thenReturn(trainee);
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);
//        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(userService.matchUsernameAndPassword(traineeDTO.getUserDTO().getUsername(), password)).thenReturn(true);

        TraineeDto result = traineeService.updateTrainee(traineeDTO, password);
        assertNotNull(result);

        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }

    @Test
    void testDeleteTraineeByUsername() {
        String username = "testUser";
        String password = "testPass";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        traineeService.deleteTraineeByUsername(username, password);

        verify(traineeDAO, times(1)).deleteTraineeByUsername(username);
    }

    @Test
    void testChangeTraineePassword() {
        String username = "testUser";
        String newPassword = "newPass";
        String password = "testPass";
        UserDto userDTO = new UserDto();
        userDTO.setUsername(username);

        when(userService.getUserByUsername(username)).thenReturn(userDTO);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        traineeService.changeTraineePassword(username, newPassword, password);

        verify(userService, times(1)).updateUser(userDTO);
    }

    @Test
    void testActivateDeactivateTrainee() {
        String username = "testUser";
        boolean isActive = true;
        String password = "testPass";
        Trainee trainee = new Trainee();

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(traineeDAO.getTraineeByUsername(username)).thenReturn(trainee);

        traineeService.activateDeactivateTrainee(username, isActive, password);

        verify(traineeDAO, times(1)).getTraineeByUsername(username);
//        verify(userService, times(1)).updateUser(EntityMapper.INSTANCE.userToUserDTO(trainee.getUser()));
    }

    @Test
    void testUpdateTraineeTrainers() {
        String username = "testUser";
        String password = "testPass";
        Set<TrainerDto> trainerDtos = new HashSet<>();
        Trainee trainee = new Trainee();

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(traineeDAO.getTraineeByUsername(username)).thenReturn(trainee);

        traineeService.updateTraineeTrainers(username, trainerDtos, password);

        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }
}