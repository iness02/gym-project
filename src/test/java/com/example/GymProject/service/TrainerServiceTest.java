package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.trainerRequest.GetTrainerTrainingsRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.trainerResponse.UpdateTrainerProfileResponse;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
import com.example.GymProject.mapper.EntityMapper;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class TrainerServiceTest {
    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserService userService;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        TrainerDto trainerDto = new TrainerDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        trainerDto.setUserDto(userDto);

        Trainer trainer = new Trainer();
        User user = new User();
        Trainer savedTrainer = new Trainer();
        savedTrainer.setId(1L);
        user.setUsername("Jane.Smith0");
        user.setPassword(Utils.generatePassword());
        savedTrainer.setUser(user);

        when(userService.generateUniqueUserName("Jane", "Smith")).thenReturn("Jane.Smith0");
        when(trainerDao.existsByUsername("Jane.Smith0")).thenReturn(false);
        when(traineeDao.existsByUsername("Jane.Smith0")).thenReturn(false);
        when(entityMapper.toTrainer(trainerDto)).thenReturn(trainer);
        when(entityMapper.toUser(trainerDto.getUserDto())).thenReturn(user);
        doNothing().when(userDao).createUser(user);
        when(trainerDao.createTrainer(trainer)).thenReturn(savedTrainer);

        UserPassResponse response = trainerService.createTrainer(trainerDto);

        verify(userService, times(1)).generateUniqueUserName("Jane", "Smith");
        verify(trainerDao, times(1)).existsByUsername("Jane.Smith0");
        verify(traineeDao, times(1)).existsByUsername("Jane.Smith0");
        verify(entityMapper, times(1)).toTrainer(trainerDto);
        verify(entityMapper, times(1)).toUser(trainerDto.getUserDto());
        verify(userDao, times(1)).createUser(user);
        verify(trainerDao, times(1)).createTrainer(trainer);

        assertNotNull(response);
        assertEquals(savedTrainer.getId(), response.getId());
        assertEquals(savedTrainer.getUser().getUsername(), response.getUsername());
        assertEquals(savedTrainer.getUser().getPassword(), response.getPassword());
    }

    @Test
    public void testCreateTrainerThrowsExceptionWhenTrainerDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            trainerService.createTrainer(null);
        });
    }

    @Test
    public void testCreateTrainerThrowsExceptionWhenUserAlreadyExists() {
        TrainerDto trainerDto = new TrainerDto();
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        trainerDto.setUserDto(userDto);

        when(userService.generateUniqueUserName("Jane", "Smith")).thenReturn("Jane.Smith0");
        when(trainerDao.existsByUsername("Jane.Smith0")).thenReturn(true);

        assertThrows(UserAlreadyRegisteredException.class, () -> {
            trainerService.createTrainer(trainerDto);
        });

        verify(userService, times(1)).generateUniqueUserName("Jane", "Smith");
        verify(trainerDao, times(1)).existsByUsername("Jane.Smith0");
    }

    @Test
    void getTrainerByUsername() {
        String username = "John.Doe";
        String password = "password";

        when(trainerDao.getTrainerByUsername(username)).thenReturn(new Trainer());
        when(entityMapper.toUserDto(any(User.class))).thenReturn(new UserDto());
        when(entityMapper.toTrainerDto(any(Trainer.class))).thenReturn(new TrainerDto());
        when(userService.checkUsernameAndPassword(username, password)).thenReturn(true);

        assertNotNull(trainerService.getTrainerByUsername(username, password));
        assertNotNull((trainerService.getTrainerByUsername(username)));

        verify(trainerDao, times(2)).getTrainerByUsername(username);
    }


    @Test
    void testUpdateTrainer() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setId(1L);
        trainerDto.setUserDto(new UserDto("John", "Doe"));

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(new User());

        when(entityMapper.toTrainer(any(TrainerDto.class))).thenReturn(trainer);
        when(entityMapper.toUser(any(UserDto.class))).thenReturn(new User());
        when(trainerDao.updateTrainer(any(Trainer.class))).thenReturn(trainer);
        when(entityMapper.toTrainerDto(any(Trainer.class))).thenReturn(new TrainerDto());
        when(entityMapper.toUpdateTrainerProfileResponse(any(TrainerDto.class))).thenReturn(new UpdateTrainerProfileResponse());

        UpdateTrainerProfileResponse response = trainerService.updateTrainer(trainerDto);

        assertNotNull(response);
    }

    @Test
    void deleteTrainerByUsername() {
        String username = "John.Doe";
        String password = "password";

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(true);

        trainerService.deleteTrainerByUsername(username, password);

        verify(trainerDao, times(1)).deleteTrainerByUsername(username);
    }

    @Test
    void testActivate() {
        String username = "username";
        String password = "password";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        when(trainerDao.getTrainerByUsername(username)).thenReturn(trainer);
        when(entityMapper.toUserDto(any(User.class))).thenReturn(new UserDto());

        assertTrue(trainerService.activate(username, password));
    }

    @Test
    void testDeactivate() {
        String username = "username";
        String password = "password";

        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        when(trainerDao.getTrainerByUsername(username)).thenReturn(trainer);
        when(entityMapper.toUserDto(any(User.class))).thenReturn(new UserDto());

        assertTrue(trainerService.deactivate(username, password));
    }

    @Test
    void testGetTrainerTrainings() {
        GetTrainerTrainingsRequest request = new GetTrainerTrainingsRequest();
        request.setUsername("username");
        request.setPeriodFrom(new Date());
        request.setPeriodTo(new Date());
        request.setTrainerName("trainerName");

        List<Training> trainings = Arrays.asList(
                new Training(),
                new Training()
        );

        when(trainerDao.getTrainerTrainings(anyString(), any(Date.class), any(Date.class), anyString())).thenReturn(trainings);
        when(entityMapper.toTrainingDto(any(Training.class))).thenReturn(new TrainingDto());
        when(entityMapper.toGetTrainingResponse(any(TrainingDto.class))).thenReturn(new GetTrainingResponse());

        List<GetTrainingResponse> responses = trainerService.getTrainerTrainings(request);

        assertEquals(trainings.size(), responses.size());
    }


}
