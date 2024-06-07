package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dto.TraineeDTO;
import com.example.GymProject.dto.TrainerDTO;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.mapper.EntityMapper;
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
        TraineeDTO traineeDTO = new TraineeDTO();
        Trainee trainee = new Trainee();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");

        when(EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO)).thenReturn(trainee);
        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(traineeDAO.createTrainee(trainee)).thenReturn(trainee);

        TraineeDTO result = traineeService.createTrainee(traineeDTO);
        assertNotNull(result);

        verify(traineeDAO, times(1)).createTrainee(trainee);
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "testUser";
        String password = "testPass";
        Trainee trainee = new Trainee();
        TraineeDTO traineeDTO = new TraineeDTO();

        when(traineeDAO.getTraineeByUsername(username)).thenReturn(trainee);
        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        TraineeDTO result = traineeService.getTraineeByUsername(username, password);
        assertNotNull(result);

        verify(traineeDAO, times(1)).getTraineeByUsername(username);
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        String password = "testPass";
        Trainee trainee = new Trainee();

        when(EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO)).thenReturn(trainee);
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);
        when(EntityMapper.INSTANCE.traineeToTraineeDTO(trainee)).thenReturn(traineeDTO);
        when(userService.matchUsernameAndPassword(traineeDTO.getUser().getUsername(), password)).thenReturn(true);

        TraineeDTO result = traineeService.updateTrainee(traineeDTO, password);
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
        UserDTO userDTO = new UserDTO();
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
        verify(userService, times(1)).updateUser(EntityMapper.INSTANCE.userToUserDTO(trainee.getUser()));
    }

    @Test
    void testUpdateTraineeTrainers() {
        String username = "testUser";
        String password = "testPass";
        Set<TrainerDTO> trainerDTOs = new HashSet<>();
        Trainee trainee = new Trainee();

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(traineeDAO.getTraineeByUsername(username)).thenReturn(trainee);

        traineeService.updateTraineeTrainers(username, trainerDTOs, password);

        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }
}