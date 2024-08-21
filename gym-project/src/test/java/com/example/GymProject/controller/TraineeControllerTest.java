package com.example.GymProject.controller;

import com.example.GymProject.dto.request.*;
import com.example.GymProject.dto.respone.*;
import com.example.GymProject.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        TraineeRegistrationRequestDto registrationRequest = new TraineeRegistrationRequestDto();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        registrationRequest.setAddress("123 Main St");

        UserPassResponseDto userPassResponse = new UserPassResponseDto(1L, "John.Doe");

        when(traineeService.createTrainee(any(TraineeRegistrationRequestDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponseDto> responseEntity = traineeController.createTrainee(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(traineeService, times(1)).createTrainee(any(TraineeRegistrationRequestDto.class));
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "username";
        String password = "password";

        TraineeProfileResponseDto expectedResponse = new TraineeProfileResponseDto();

        UserPassRequestDto request = new UserPassRequestDto();
        request.setUsername(username);
        request.setPassword(password);

        when(traineeService.getTraineeByUsername(username)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.getTraineeByUsername(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(traineeService, times(1)).getTraineeByUsername(username);
    }


    @Test
    void testUpdateTrainee() {
        UpdateTraineeProfileRequestDto request = new UpdateTraineeProfileRequestDto();
        request.setUsername("username");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.now());
        request.setAddress("123 Main St");
        request.setIsActive(true);

        UpdateTraineeProfileResponseDto expectedResponse = new UpdateTraineeProfileResponseDto();

        when(traineeService.updateTrainee(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.updateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(traineeService, times(1)).updateTrainee(any());
    }


    @Test
    void testDeleteTrainee() {
        DeleteRequestDto request = new DeleteRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        when(traineeService.deleteTraineeByUsername("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.deleteTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deleted successfully", result.getBody());
    }


    @Test
    void testGetTrainingList() {
        Long traineeId = 1L;
        TraineeTrainingsRequestDto request = new TraineeTrainingsRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        List<GetTrainingResponseDto> expectedResponse = new ArrayList<>();
        when(traineeService.getTraineeTrainings(request)).thenReturn(expectedResponse);

        ResponseEntity<?> result = traineeController.getTrainingList(traineeId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void testGetUnassignedTrainers() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");
        List<UnassignedTrainerResponseDto> response = Collections.singletonList(new UnassignedTrainerResponseDto());

        when(traineeService.getUnassignedTrainers("username", "password")).thenReturn(response);

        ResponseEntity<?> result = traineeController.getUnassignedTrainers(11L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testActivateTrainee() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(traineeService.activate("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.activateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee activated successfully", result.getBody());
    }

    @Test
    void testDeactivateTrainee() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(traineeService.deactivate("username", "password")).thenReturn(true);

        ResponseEntity<String> result = traineeController.deactivateTrainee(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deactivated successfully", result.getBody());
    }

    @Test
    public void testUpdateTraineeTrainers() {
        Long traineeId = 1L;
        UpdateTraineeTrainersRequestDto requestDto = new UpdateTraineeTrainersRequestDto("username", "password", Set.of("trainer1", "trainer2"));
        List<TrainerForTraineeResponseDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(new TrainerForTraineeResponseDto("user1", "joe", "dao", "trainer1"));
        expectedResponse.add(new TrainerForTraineeResponseDto("user2", "jone", "brown", "trainer2"));
        when(traineeService.updateTraineeTrainers(anyString(), anyString(), anySet())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = traineeController.updateTraineeTrainers(traineeId, requestDto);

        verify(traineeService, times(1)).updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
