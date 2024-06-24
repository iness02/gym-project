package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.DeleteRequest;
import com.example.GymProject.dto.request.UserPassRequest;
import com.example.GymProject.dto.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.dto.request.traineerRquest.TraineeRegistrationRequest;
import com.example.GymProject.dto.request.traineerRquest.UpdateTraineeProfileRequest;
import com.example.GymProject.dto.request.traineerRquest.UpdateTraineeTrainersRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.dto.response.traineeResponse.TrainerForTraineeResponse;
import com.example.GymProject.dto.response.traineeResponse.UnassignedTrainerResponse;
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.service.TraineeService;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})

public class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;

    @Mock
    private EntityMapper entityMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeRegistrationRequest registrationRequest = new TraineeRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setDateOfBirth(new Date(2002, Calendar.OCTOBER, 22));
        registrationRequest.setAddress("123 Main St");

        UserDto userDto = new UserDto("John", "Doe");
        TraineeDto traineeDto = new TraineeDto(null, new Date(2002, Calendar.OCTOBER, 22), "123 Main St", userDto, null);

        UserPassResponse userPassResponse = new UserPassResponse(1L, "John.Doe", "password123");

        when(traineeService.createTrainee(any(TraineeDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponse> responseEntity = traineeController.createTrainee(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(traineeService, times(1)).createTrainee(any(TraineeDto.class));
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "username";
        String password = "password";

        GetTraineeProfileResponse expectedResponse = new GetTraineeProfileResponse();

        UserPassRequest request = new UserPassRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(userService.checkUsernameAndPassword(username, password)).thenReturn(true);
        when(traineeService.getTraineeByUsername(username)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.getTraineeByUsername(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(traineeService, times(1)).getTraineeByUsername(username);
    }


    @Test
    void testUpdateTrainee() {
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(new Date());
        request.setAddress("123 Main St");
        request.setIsActive(true);

        UpdateTraineeProfileResponse expectedResponse = new UpdateTraineeProfileResponse();

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(traineeService.updateTrainee(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.updateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(traineeService, times(1)).updateTrainee(any());
    }


    @Test
    void testDeleteTrainee() {
        DeleteRequest request = new DeleteRequest();
        request.setUsername("username");
        request.setPassword("password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.deleteTraineeByUsername("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.deleteTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deleted successfully", result.getBody());
    }


    @Test
    void testGetTrainingList() {
        Long traineeId = 1L;
        GetTraineeTrainingsRequest request = new GetTraineeTrainingsRequest();
        request.setUsername("username");
        request.setPassword("password");

        List<GetTrainingResponse> expectedResponse = new ArrayList<>();
        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.getTraineeTrainings(request)).thenReturn(expectedResponse);

        ResponseEntity<?> result = traineeController.getTrainingList(traineeId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testGetUnassignedTrainers() {
        UserPassRequest request = new UserPassRequest("username", "password");
        List<UnassignedTrainerResponse> response = Collections.singletonList(new UnassignedTrainerResponse());

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.getUnassignedTrainers("username", "password")).thenReturn(response);

        ResponseEntity<?> result = traineeController.getUnassignedTrainers(11L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testActivateTrainee() {
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.activate("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.activateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee activated successfully", result.getBody());
    }

    @Test
    void testDeactivateTrainee() {
        UserPassRequest request = new UserPassRequest("username", "password");

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.deactivate("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.deactivateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deactivated successfully", result.getBody());
    }

    @Test
    public void testUpdateTraineeTrainers() {
        Long traineeId = 1L;
        UpdateTraineeTrainersRequest requestDto = new UpdateTraineeTrainersRequest("username", "password", Set.of("trainer1", "trainer2"));
        List<TrainerForTraineeResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new TrainerForTraineeResponse("user1", "joe", "dao", "trainer1"));
        expectedResponse.add(new TrainerForTraineeResponse("user2", "jone", "brown", "trainer2"));
        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(traineeService.updateTraineeTrainers(anyString(), anyString(), anySet())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.updateTraineeTrainers(traineeId, requestDto);

        verify(traineeService, times(1)).updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}

