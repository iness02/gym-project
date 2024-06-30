package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.TrainerRegistrationRequestDto;
import com.example.GymProject.dto.request.TrainerTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTrainerProfileRequestDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.dto.respone.GetTrainingResponseDto;
import com.example.GymProject.dto.respone.TrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UpdateTrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UserPassResponseDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.service.TrainerService;
import com.example.GymProject.service.UserService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
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
        TrainerRegistrationRequestDto registrationRequest = new TrainerRegistrationRequestDto();
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setSpecialization("Fitness");

        UserDto userDto = new UserDto("John", "Doe");
        TrainerDto trainerDto = new TrainerDto(null, "Fitness", userDto, null);

        UserPassResponseDto userPassResponse = new UserPassResponseDto(1L, "John.Doe", "password123");

        when(trainerService.createTrainer(any(TrainerRegistrationRequestDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponseDto> responseEntity = trainerController.createTrainer(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(trainerService, times(1)).createTrainer(any(TrainerRegistrationRequestDto.class));
    }

    @Test
    void testGetTrainerByUsername() {
        UserPassRequestDto request = new UserPassRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        TrainerProfileResponseDto expectedResponse = new TrainerProfileResponseDto();

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(entityMapper.toTrainerProfileResponseDto(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.getTrainerByUsername(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testUpdateTrainer() {
        UpdateTrainerProfileRequestDto request = new UpdateTrainerProfileRequestDto();
        request.setUsername("username");
        request.setPassword("password");

        UpdateTrainerProfileResponseDto expectedResponse = new UpdateTrainerProfileResponseDto();

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.updateTrainer(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.updateTrainer(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(trainerService, times(1)).updateTrainer(any());
    }


    @Test
    public void testGetTrainingList() {
        Long trainerId = 1L;
        TrainerTrainingsRequestDto request = new TrainerTrainingsRequestDto("username", "password", LocalDate.now(), LocalDate.now(), "trainerName");
        List<GetTrainingResponseDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(new GetTrainingResponseDto("Training1", LocalDate.now(), "Type1", 60, "trainerName"));
        expectedResponse.add(new GetTrainingResponseDto("Training2", LocalDate.now(), "Type2", 45, "trainerName"));

        when(userService.checkUsernameAndPassword("username", "password")).thenReturn(true);
        when(trainerService.getTrainerTrainings(any(TrainerTrainingsRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = trainerController.getTrainingList(trainerId, request);

        verify(trainerService, times(1)).getTrainerTrainings(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }


    @Test
    public void testActivateTrainee() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.activate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> result = trainerController.activateTrainer(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee activated successfully", result.getBody());
    }

    @Test
    public void testDeactivateTrainee() {
        UserPassRequestDto request = new UserPassRequestDto("username", "password");

        when(userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(true);
        when(trainerService.deactivate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> result = trainerController.deactivateTrainer(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Trainee deactivated successfully", result.getBody());
    }
}
