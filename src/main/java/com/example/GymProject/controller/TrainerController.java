package com.example.GymProject.controller;


import com.example.GymProject.dto.request.TrainerRegistrationRequestDto;
import com.example.GymProject.dto.request.TrainerTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTrainerProfileRequestDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.dto.respone.GetTrainingResponseDto;
import com.example.GymProject.dto.respone.TrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UpdateTrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UserPassResponseDto;
import com.example.GymProject.service.TrainerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    @Autowired
    private TrainerService trainerService;
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @PostMapping("/register")
    public ResponseEntity<UserPassResponseDto> createTrainer(@Valid @RequestBody TrainerRegistrationRequestDto registrationRequest) {
        logger.info("Received request to create trainer: {}", registrationRequest);
        UserPassResponseDto response = trainerService.createTrainer(registrationRequest);
        logger.info("Trainer created successfully: {}", response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/trainer")
    public ResponseEntity<?> getTrainerByUsername(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to get trainer by username: {}", request.getUsername());
        TrainerProfileResponseDto response = trainerService.getTrainerByUsername(request.getUsername());
        logger.info("Retrieved trainer profile: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/trainer")
    public ResponseEntity<?> updateTrainer(
            @Valid @RequestBody UpdateTrainerProfileRequestDto newData) {
        logger.info("Received request to update trainer profile: {}", newData);
        UpdateTrainerProfileResponseDto response = trainerService.updateTrainer(newData);
        logger.info("Updated trainer profile: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{trainerId}/trainings")
    public ResponseEntity<?> getTrainingList(
            @PathVariable Long trainerId,
            @Valid @RequestBody TrainerTrainingsRequestDto request) {
        logger.info("Received request to get training list for trainer: {}", request);

        List<GetTrainingResponseDto> response = trainerService.getTrainerTrainings(request);
        logger.info("Retrieved training list: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainer(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to activate trainer: {}", request);

        boolean isActivated = trainerService.activate(request.getUsername(), request.getPassword());
        if (isActivated) {
            logger.info("Trainer activated successfully: {}", request.getUsername());
            return ResponseEntity.ok("Trainee activated successfully");
        } else {
            logger.warn("Unauthorized attempt to activate trainer: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateTrainer(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to deactivate trainer: {}", request);

        boolean isDeactivated = trainerService.deactivate(request.getUsername(), request.getPassword());
        if (isDeactivated) {
            logger.info("Trainer deactivated successfully: {}", request.getUsername());
            return ResponseEntity.ok("Trainee deactivated successfully");
        } else {
            logger.warn("Unauthorized attempt to deactivate trainer: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

    }

}
