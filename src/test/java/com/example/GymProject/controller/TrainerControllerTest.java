package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.UserPassRequest;
import com.example.GymProject.dto.request.trainerRequest.GetTrainerTrainingsRequest;
import com.example.GymProject.dto.request.trainerRequest.TrainerRegistrationRequest;
import com.example.GymProject.dto.request.trainerRequest.UpdateTrainerProfileRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.trainerResponse.GetTrainerProfileResponse;
import com.example.GymProject.dto.response.trainerResponse.UpdateTrainerProfileResponse;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.service.TrainerService;
import com.example.GymProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})

public class TrainerControllerTest {
    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        TrainerRegistrationRequest registrationRequest = new TrainerRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setSpecialization("Fitness");

        UserDto userDto = new UserDto("John", "Doe");
        TrainerDto trainerDto = new TrainerDto(null, "Fitness", userDto, null);

        UserPassResponse userPassResponse = new UserPassResponse(1L, "John.Doe", "password123");

        when(trainerService.createTrainer(any(TrainerDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponse> responseEntity = trainerController.createTrainee(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(trainerService, times(1)).createTrainer(any(TrainerDto.class));
    }

    @Test
    void testGetTrainerByUsername() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("username");
        request.setPassword("password");

        GetTrainerProfileResponse expectedResponse = new GetTrainerProfileResponse();

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(entityMapper.toGetTrainerProfileResponse(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.getTraineeByUsername(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(entityMapper, times(1)).toGetTrainerProfileResponse(any());
    }


    @Test
    void testUpdateTrainer() {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();
        request.setUsername("username");
        request.setPassword("password");

        UpdateTrainerProfileResponse expectedResponse = new UpdateTrainerProfileResponse();

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.updateTrainer(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.updateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(trainerService, times(1)).updateTrainer(any());
    }


    @Test
    public void testGetTrainingList() {
        Long trainerId = 1L;
        GetTrainerTrainingsRequest request = new GetTrainerTrainingsRequest("username", "password", new Date(), new Date(), "trainerName");
        List<GetTrainingResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new GetTrainingResponse("Training1", new Date(), "Type1", 60, "trainerName"));
        expectedResponse.add(new GetTrainingResponse("Training2", new Date(), "Type2", 45, "trainerName"));

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(trainerService.getTrainerTrainings(any(GetTrainerTrainingsRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.getTrainingList(trainerId, request);

        verify(trainerService, times(1)).getTrainerTrainings(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }


    @Test
    public void testActivateTrainee() {
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.activate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> result = trainerController.activateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee activated successfully", result.getBody());
    }

    @Test
    public void testDeactivateTrainee() {
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.deactivate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> result = trainerController.deactivateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deactivated successfully", result.getBody());
    }
}
