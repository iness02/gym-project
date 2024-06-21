package com.example.GymProject.controller;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.dto.request.UserPassRequest;
import com.example.GymProject.dto.request.trainerRequest.GetTrainerTrainingsRequest;
import com.example.GymProject.dto.request.trainerRequest.TrainerRegistrationRequest;
import com.example.GymProject.dto.request.trainerRequest.UpdateTrainerProfileRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.trainerResponse.GetTrainerProfileResponse;
import com.example.GymProject.dto.response.trainerResponse.UpdateTrainerProfileResponse;
import com.example.GymProject.service.TrainerService;
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

import java.time.LocalDate;
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

        UserPassResponse userPassResponse = new UserPassResponse(1L,"John.Doe", "password123");

        when(trainerService.createTrainer(any(TrainerDto.class))).thenReturn(userPassResponse);

        ResponseEntity<UserPassResponse> responseEntity = trainerController.createTrainee(registrationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userPassResponse, responseEntity.getBody());
        verify(trainerService, times(1)).createTrainer(any(TrainerDto.class));
    }

    @Test
    public void testGetTrainerByUsername() {
        String username = "JohnDoe";
        GetTrainerProfileResponse response = new GetTrainerProfileResponse();

        when(trainerService.getTrainerByUsername(username)).thenReturn(new TrainerDto());
        when(entityMapper.toGetTrainerProfileResponse(any(TrainerDto.class))).thenReturn(response);

        ResponseEntity<GetTrainerProfileResponse> responseEntity = trainerController.getTraineeByUsername(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(trainerService, times(1)).getTrainerByUsername(username);
    }

    @Test
    public void testUpdateTrainer() {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();
        UpdateTrainerProfileResponse response = new UpdateTrainerProfileResponse();

        when(entityMapper.toTrainerDao(request)).thenReturn(new TrainerDto());
        when(trainerService.updateTrainer(any(TrainerDto.class))).thenReturn(response);

        ResponseEntity<UpdateTrainerProfileResponse> responseEntity = trainerController.updateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
        verify(trainerService, times(1)).updateTrainer(any(TrainerDto.class));
    }

    @Test
    public void testGetTrainingList() {
        Long trainerId = 1L;
        GetTrainerTrainingsRequest request = new GetTrainerTrainingsRequest("username", "password", new Date(), new Date(), "trainerName");
        List<GetTrainingResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(new GetTrainingResponse("Training1", LocalDate.now(), "Type1", 60, "trainerName"));
        expectedResponse.add(new GetTrainingResponse("Training2", LocalDate.now().plusDays(1), "Type2", 45, "trainerName"));

        when(trainerService.getTrainerTrainings(any(GetTrainerTrainingsRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<List<GetTrainingResponse>> responseEntity = trainerController.getTrainingList(trainerId, request);

        verify(trainerService, times(1)).getTrainerTrainings(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testActivateTrainer() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("password123");

        when(trainerService.activate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> responseEntity = trainerController.activateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee activated successfully", responseEntity.getBody());
        verify(trainerService, times(1)).activate(request.getUsername(), request.getPassword());
    }

    @Test
    public void testActivateTrainer_Unauthorized() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("wrongpassword");

        when(trainerService.activate(request.getUsername(), request.getPassword())).thenReturn(false);

        ResponseEntity<String> responseEntity = trainerController.activateTrainee(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(trainerService, times(1)).activate(request.getUsername(), request.getPassword());
    }

    @Test
    public void testDeactivateTrainer() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("password123");

        when(trainerService.deactivate(request.getUsername(), request.getPassword())).thenReturn(true);

        ResponseEntity<String> responseEntity = trainerController.deactivateTrainee(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Trainee deactivated successfully", responseEntity.getBody());
        verify(trainerService, times(1)).deactivate(request.getUsername(), request.getPassword());
    }

    @Test
    public void testDeactivateTrainer_Unauthorized() {
        UserPassRequest request = new UserPassRequest();
        request.setUsername("JohnDoe");
        request.setPassword("wrongpassword");

        when(trainerService.deactivate(request.getUsername(), request.getPassword())).thenReturn(false);

        ResponseEntity<String> responseEntity = trainerController.deactivateTrainee(request);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(trainerService, times(1)).deactivate(request.getUsername(), request.getPassword());
    }
}
