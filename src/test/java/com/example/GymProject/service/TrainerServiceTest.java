package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainerServiceTest {
    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTrainer() {
        TrainerDto trainerDto = new TrainerDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        trainerDto.setUserDto(userDto);

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(entityMapper.toTrainer(trainerDto)).thenReturn(trainer);
        when(entityMapper.toUser(trainerDto.getUserDto())).thenReturn(user);
        when(userService.generateUniqueUserName("John", "Doe")).thenReturn("John.Doe");
        doNothing().when(userDao).createUser(any(User.class));
        when(trainerDao.createTrainer(any(Trainer.class))).thenReturn(trainer);
        when(entityMapper.toTrainerDto(any(Trainer.class))).thenReturn(trainerDto);

        TrainerDto result = trainerService.createTrainer(trainerDto);

        assertNotNull(result);
        verify(userDao, times(1)).createUser(any(User.class));
        verify(trainerDao, times(1)).createTrainer(any(Trainer.class));
    }

    @Test
    void getTrainerByUsername() {
        String username = "John.Doe";
        String password = "password";

        when(trainerDao.getTrainerByUsername(username)).thenReturn(new Trainer());
        when(entityMapper.toUserDto(any(User.class))).thenReturn(new UserDto());
        when(entityMapper.toTrainerDto(any(Trainer.class))).thenReturn(new TrainerDto());
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        assertNotNull(trainerService.getTrainerByUsername(username, password));

        verify(trainerDao, times(1)).getTrainerByUsername(username);
    }

    @Test
    void updateTrainer() {
        TrainerDto trainerDto = new TrainerDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("John.Doe");
        trainerDto.setUserDto(userDto);

        when(entityMapper.toTrainer(trainerDto)).thenReturn(new Trainer());
        when(entityMapper.toUser(trainerDto.getUserDto())).thenReturn(new User());
        when(trainerDao.updateTrainer(any(Trainer.class))).thenReturn(new Trainer());
        when(entityMapper.toTrainerDto(any(Trainer.class))).thenReturn(trainerDto);
        when(userService.matchUsernameAndPassword(userDto.getUsername(), "password")).thenReturn(true);

        assertNotNull(trainerService.updateTrainer(trainerDto, "password"));

        verify(trainerDao, times(1)).updateTrainer(any(Trainer.class));
    }

    @Test
    void changeTrainerPassword() {
        String username = "John.Doe";
        String newPassword = "newPassword";
        String password = "password";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);

        when(userService.getUserByUsername(username)).thenReturn(userDto);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        trainerService.changeTrainerPassword(username, newPassword, password);

        assertEquals(newPassword, userDto.getPassword());
        verify(userService, times(1)).updateUser(userDto);
    }

    @Test
    void deleteTrainerByUsername() {
        String username = "John.Doe";
        String password = "password";

        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        trainerService.deleteTrainerByUsername(username, password);

        verify(trainerDao, times(1)).deleteTrainerByUsername(username);
    }

    @Test
    void activateDeactivateTrainer() {
        String username = "john.doe";
        String password = "password";
        boolean isActive = true;

        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);
        UserDto userDto = new UserDto();

        when(trainerDao.getTrainerByUsername(username)).thenReturn(trainer);
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);
        when(entityMapper.toUserDto(user)).thenReturn(userDto);

        trainerService.activateDeactivateTrainer(username, isActive, password);

        verify(userService, times(1)).matchUsernameAndPassword(username, password);
        verify(trainerDao, times(1)).getTrainerByUsername(username);
        verify(userService, times(1)).updateUser(userDto);
    }

    @Test
    void getTrainerTrainings() {
        String username = "John.Doe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "Jane.Doe";
        String password = "password";

        List<Training> trainings = Collections.singletonList(new Training());

        when(trainerDao.getTrainerTrainings(username, fromDate, toDate, traineeName)).thenReturn(trainings);
        when(entityMapper.toTrainingDto(any(Training.class))).thenReturn(new TrainingDto());
        when(userService.matchUsernameAndPassword(username, password)).thenReturn(true);

        List<TrainingDto> result = trainerService.getTrainerTrainings(username, fromDate, toDate, traineeName, password);

        assertEquals(1, result.size());
        verify(trainerDao, times(1)).getTrainerTrainings(username, fromDate, toDate, traineeName);
    }
}