package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dto.TrainerDTO;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "testUser";
        String password = "testPass";
        Trainer trainer = new Trainer();
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerDAO.getTrainerByUsername(username)).thenReturn(trainer);
        when(EntityMapper.INSTANCE.trainerToTrainerDTO(trainer)).thenReturn(trainerDTO);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        TrainerDTO result = trainerService.getTrainerByUsername(username, password);
        assertNotNull(result);

        verify(trainerDAO, times(1)).getTrainerByUsername(username);
    }

    @Test
    void testUpdateTrainer() {
        TrainerDTO trainerDTO = new TrainerDTO();
        String password = "testPass";
        Trainer trainer = new Trainer();

        when(EntityMapper.INSTANCE.trainerDTOToTrainer(trainerDTO)).thenReturn(trainer);
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);
        when(EntityMapper.INSTANCE.trainerToTrainerDTO(trainer)).thenReturn(trainerDTO);
        when(userService.matchUsernameAndPassword(trainerDTO.getUser().getUsername(), password)).thenReturn(true);

        TrainerDTO result = trainerService.updateTrainer(trainerDTO, password);
        assertNotNull(result);

        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }

    @Test
    void deleteTrainerByUsername() {
        String username = "testUser";
        String password = "testPass";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        trainerService.deleteTraineeByUsername(username, password);

        verify(trainerDAO, times(1)).deleteTrainerByUsername(username);
    }

    @Test
    void testChangeTrainerPassword() {
        String username = "testUser";
        String newPassword = "newPass";
        String password = "testPass";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);

        when(userService.getUserByUsername(username)).thenReturn(userDTO);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        trainerService.changeTrainerPassword(username, newPassword, password);

        verify(userService, times(1)).updateUser(userDTO);
    }

    @Test
    void testActivateDeactivateTrainer() {
        String username = "testUser";
        boolean isActive = true;
        String password = "testPass";
        Trainer trainer = new Trainer();

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(trainerDAO.getTrainerByUsername(username)).thenReturn(trainer);

        trainerService.activateDeactivateTrainer(username, isActive, password);

        verify(trainerDAO, times(1)).getTrainerByUsername(username);
        verify(userService, times(1)).updateUser(EntityMapper.INSTANCE.userToUserDTO(trainer.getUser()));
    }
}
