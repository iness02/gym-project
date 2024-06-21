package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
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
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.service.TraineeService;
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

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private EntityMapper entityMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeRegistrationRequest registrationRequest = new TraineeRegistrationRequest();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setDateOfBirth(new Date(2002, Calendar.OCTOBER,22));
        registrationRequest.setAddress("123 Main St");

        UserDto userDto = new UserDto("John", "Doe");
        TraineeDto traineeDto = new TraineeDto(null, new Date(2002, Calendar.OCTOBER,22), "123 Main St", userDto, null);

        UserPassResponse userPassResponse = new UserPassResponse(1L,"John.Doe", "password123");

        when(traineeService.createTrainee(any(TraineeDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponse> responseEntity = traineeController.createTrainee(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(traineeService, times(1)).createTrainee(any(TraineeDto.class));
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "JohnDoe";
        GetTraineeProfileResponse response = new GetTraineeProfileResponse();

        when(traineeService.getTraineeByUsername(username)).thenReturn(response);

        ResponseEntity<GetTraineeProfileResponse> responseEntity = traineeController.getTraineeByUsername(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(traineeService, times(1)).getTraineeByUsername(username);
    }

    @Test
    public void testUpdateTrainee() {
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest();
        UpdateTraineeProfileResponse response = new UpdateTraineeProfileResponse();

        when(entityMapper.toTraineeDao(request)).thenReturn(new TraineeDto());
        when(traineeService.updateTrainee(any(TraineeDto.class))).thenReturn(response);

        ResponseEntity<UpdateTraineeProfileResponse> responseEntity = traineeController.updateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(traineeService, times(1)).updateTrainee(any(TraineeDto.class));
    }

    @Test
    public void testDeleteTrainee() {
        DeleteRequest request = new DeleteRequest();
        request.setUsername("JohnDoe");
        request.setPassword("password123");

        when(traineeService.deleteTraineeByUsername(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> responseEntity = traineeController.deleteTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee deleted successfully", responseEntity.getBody());
        verify(traineeService, times(1)).deleteTraineeByUsername(request.getUsername(), request.getPassword());
    }

    @Test
    public void testDeleteTrainee_Unauthorized() {
        DeleteRequest request = new DeleteRequest();
        request.setUsername("JohnDoe");
        request.setPassword("wrongpassword");

        when(traineeService.deleteTraineeByUsername(request.getUsername(), request.getPassword())).thenReturn(false);

        ResponseEntity<String> responseEntity = traineeController.deleteTrainee(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(traineeService, times(1)).deleteTraineeByUsername(request.getUsername(), request.getPassword());
    }



    @Test
    public void testUpdateTraineeTrainers() {
        Long traineeId = 1L;
        UpdateTraineeTrainersRequest requestDto = new UpdateTraineeTrainersRequest("username", "password", Set.of("trainer1", "trainer2"));
        List<TrainerForTraineeResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new TrainerForTraineeResponse("user1","joe","dao","trainer1"));
        expectedResponse.add(new TrainerForTraineeResponse("user2","jone","brown","trainer2"));

        when(traineeService.updateTraineeTrainers(anyString(), anyString(), anySet())).thenReturn(expectedResponse);

        ResponseEntity<List<TrainerForTraineeResponse>> responseEntity = traineeController.updateTraineeTrainers(traineeId, requestDto);

        verify(traineeService, times(1)).updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testGetTrainingList() {
        Long traineeId = 1L;
        GetTraineeTrainingsRequest request = new GetTraineeTrainingsRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        List<GetTrainingResponse> expectedResponse = new ArrayList<>();
        when(traineeService.getTraineeTrainings(any(GetTraineeTrainingsRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<List<GetTrainingResponse>> responseEntity = traineeController.getTrainingList(traineeId, request);

        verify(traineeService, times(1)).getTraineeTrainings(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    @Test
    public void testActivateTrainee_Unauthorized() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("wrongpassword");

        when(traineeService.activate(request.getUsername(), request.getPassword())).thenReturn(false);

        ResponseEntity<String> responseEntity = traineeController.activateTrainee(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(traineeService, times(1)).activate(request.getUsername(), request.getPassword());
    }

    @Test
    public void testDeactivateTrainee() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("password123");

        when(traineeService.deactivate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> responseEntity = traineeController.deactivateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee deactivated successfully", responseEntity.getBody());
        verify(traineeService, times(1)).deactivate(request.getUsername(), request.getPassword());
    }

    @Test
    public void testDeactivateTrainee_Unauthorized() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("wrongpassword");

        when(traineeService.deactivate(request.getUsername(), request.getPassword())).thenReturn(false);

        ResponseEntity<String> responseEntity = traineeController.deactivateTrainee(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(traineeService, times(1)).deactivate(request.getUsername(), request.getPassword());
    }
}