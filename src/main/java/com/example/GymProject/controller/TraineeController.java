package com.example.GymProject.controller;

import com.example.GymProject.dto.request.*;
import com.example.GymProject.dto.respone.*;
import com.example.GymProject.service.TraineeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/trainees")
public class TraineeController {
    @Autowired
    private TraineeService traineeService;
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);

    @PostMapping("/register")
    public ResponseEntity<UserPassResponseDto> createTrainee(@Valid @RequestBody TraineeRegistrationRequestDto registrationRequest) {
        logger.info("Received request to create trainee: {}", registrationRequest);
        UserPassResponseDto response = traineeService.createTrainee(registrationRequest);
        logger.info("Trainee created successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/trainee")
    public ResponseEntity<?> getTraineeByUsername(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to get trainee by username: {}", request.getUsername());
        TraineeProfileResponseDto response = traineeService.getTraineeByUsername(request.getUsername());
        logger.info("Retrieved trainee profile: {}", response);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/trainee")
    public ResponseEntity<?> updateTrainee(
            @Valid @RequestBody UpdateTraineeProfileRequestDto newData) {
        logger.info("Received request to update trainee profile: {}", newData);
        UpdateTraineeProfileResponseDto response = traineeService.updateTrainee(newData);
        logger.info("Updated trainee profile: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/trainee")
    public ResponseEntity<String> deleteTrainee(
            @Valid @RequestBody DeleteRequestDto request) {
        logger.info("Received request to delete trainee: {}", request);
        boolean isDeleted = traineeService.deleteTraineeByUsername(request.getUsername(), request.getPassword());
        if (isDeleted) {
            logger.info("Trainee deleted successfully: {}", request.getUsername());
            return ResponseEntity.ok("Trainee deleted successfully");
        } else {
            logger.warn("Unauthorized attempt to delete trainee: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PutMapping("/{traineeId}/trainers")
    public ResponseEntity<?> updateTraineeTrainers(
            @PathVariable Long traineeId,
            @Valid @RequestBody UpdateTraineeTrainersRequestDto requestDto) {
        logger.info("Received request to update trainee trainers list: {}", requestDto);
        List<TrainerForTraineeResponseDto> response = traineeService.
                updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
        logger.info("Updated trainee trainers list: {}", response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{traineeId}/trainings")
    public ResponseEntity<?> getTrainingList(
            @PathVariable Long traineeId,
            @Valid @RequestBody TraineeTrainingsRequestDto request) {
        logger.info("Received request to get training list for trainee: {}", request);
        List<GetTrainingResponseDto> response = traineeService.getTraineeTrainings(request);
        logger.info("Retrieved training list: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{traineeId}/unassignedTrainers")
    public ResponseEntity<?> getUnassignedTrainers(@PathVariable Long traineeId,
                                                   @Valid @RequestBody UserPassRequestDto request) {

        logger.info("Received request to get unassigned trainers for trainee: {}", request);

        List<UnassignedTrainerResponseDto> trainerDtos = traineeService.getUnassignedTrainers(request.getUsername(), request.getPassword());
        logger.info("Retrieved unassigned trainers list: {}", trainerDtos);
        return ResponseEntity.ok(trainerDtos);
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainee(@Valid @RequestBody UserPassRequestDto request) {

        logger.info("Received request to activate trainee: {}", request);
        boolean isActivated = traineeService.activate(request.getUsername(), request.getPassword());
        if (isActivated) {
            logger.info("Trainee activated successfully: {}", request.getUsername());
            return ResponseEntity.ok("Trainee activated successfully");
        } else {
            logger.warn("Unauthorized attempt to activate trainee: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateTrainee(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to deactivate trainee: {}", request);

        boolean isDeactivated = traineeService.deactivate(request.getUsername(), request.getPassword());
        if (isDeactivated) {
            logger.info("Trainee deactivated successfully: {}", request.getUsername());
            return ResponseEntity.ok("Trainee deactivated successfully");
        } else {
            logger.warn("Unauthorized attempt to deactivate trainee: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

}
