package com.example.GymProject.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);


    @PostMapping("/register")
    public ResponseEntity<UserPassResponse> createTrainee(@Valid @RequestBody TraineeRegistrationRequest registrationRequest) {
        logger.info("Received request to create trainee: {}", registrationRequest);
        UserDto userDto = new UserDto(registrationRequest.getFirstName(), registrationRequest.getLastName());
        TraineeDto traineeDto = new TraineeDto(null, registrationRequest.getDateOfBirth(), registrationRequest.getAddress(), userDto, null);
        UserPassResponse response = traineeService.createTrainee(traineeDto);
        logger.info("Trainee created successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/trainee")
    public ResponseEntity<?> getTraineeByUsername(@Valid @RequestBody UserPassRequest request) {
        logger.info("Received request to get trainee by username: {}", request.getUsername());
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            GetTraineeProfileResponse response = traineeService.getTraineeByUsername(request.getUsername());
            logger.info("Retrieved trainee profile: {}", response);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }

    @PutMapping("/trainee")
    public ResponseEntity<?> updateTrainee(
            @Valid @RequestBody UpdateTraineeProfileRequest newData) {
        logger.info("Received request to update trainee profile: {}", newData);
        if (userService.checkUsernameAndPassword(newData.getUsername(), newData.getPassword())) {
            UpdateTraineeProfileResponse response = traineeService.updateTrainee(entityMapper.toTraineeDto(newData));
            logger.info("Updated trainee profile: {}", response);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + newData.getUsername());

    }

    @DeleteMapping("/trainee")
    public ResponseEntity<String> deleteTrainee(
            @Valid @RequestBody DeleteRequest request) {
        logger.info("Received request to delete trainee: {}", request);
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            boolean isDeleted = traineeService.deleteTraineeByUsername(request.getUsername(), request.getPassword());
            if (isDeleted) {
                logger.info("Trainee deleted successfully: {}", request.getUsername());
                return ResponseEntity.ok("Trainee deleted successfully");
            } else {
                logger.warn("Unauthorized attempt to delete trainee: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }

    @PutMapping("/{traineeId}/trainers")
    public ResponseEntity<?> updateTraineeTrainers(
            @PathVariable Long traineeId,
            @Valid @RequestBody UpdateTraineeTrainersRequest requestDto) {
        logger.info("Received request to update trainee trainers list: {}", requestDto);
        if (userService.checkUsernameAndPassword(requestDto.getUsername(), requestDto.getPassword())) {
            List<TrainerForTraineeResponse> response = traineeService.
                    updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
            logger.info("Updated trainee trainers list: {}", response);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + requestDto.getUsername());

    }


    @GetMapping("/{traineeId}/trainings")
    public ResponseEntity<?> getTrainingList(
            @PathVariable Long traineeId,
            @Valid @RequestBody GetTraineeTrainingsRequest request) {
        logger.info("Received request to get training list for trainee: {}", request);
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            List<GetTrainingResponse> response = traineeService.getTraineeTrainings(request);
            logger.info("Retrieved training list: {}", response);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }

    @GetMapping("/{traineeId}/unassignedTrainers")
    public ResponseEntity<?> getUnassignedTrainers(@PathVariable Long traineeId,
                                                   @Valid @RequestBody UserPassRequest request) {

        logger.info("Received request to get unassigned trainers for trainee: {}", request);
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            List<UnassignedTrainerResponse> trainerDtos = traineeService.getUnassignedTrainers(request.getUsername(), request.getPassword());
            logger.info("Retrieved unassigned trainers list: {}", trainerDtos);
            return ResponseEntity.ok(trainerDtos);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainee(@Valid @RequestBody UserPassRequest request) {

        logger.info("Received request to activate trainee: {}", request);
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            boolean isActivated = traineeService.activate(request.getUsername(), request.getPassword());
            if (isActivated) {
                logger.info("Trainee activated successfully: {}", request.getUsername());
                return ResponseEntity.ok("Trainee activated successfully");
            } else {
                logger.warn("Unauthorized attempt to activate trainee: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());

    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateTrainee(@Valid @RequestBody UserPassRequest request) {
        logger.info("Received request to deactivate trainee: {}", request);
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            boolean isDeactivated = traineeService.deactivate(request.getUsername(), request.getPassword());
            if (isDeactivated) {
                logger.info("Trainee deactivated successfully: {}", request.getUsername());
                return ResponseEntity.ok("Trainee deactivated successfully");
            } else {
                logger.warn("Unauthorized attempt to deactivate trainee: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }

}
