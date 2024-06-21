package com.example.GymProject.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private EntityMapper entityMapper;
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);


    @PostMapping("/register")
    public ResponseEntity<UserPassResponse> createTrainee(@Valid @RequestBody TrainerRegistrationRequest registrationRequest) {
        logger.info("Received request to create trainer: {}", registrationRequest);
        UserDto userDto = new UserDto(registrationRequest.getFirstName(), registrationRequest.getLastName());
        TrainerDto traineeDto = new TrainerDto(null, registrationRequest.getSpecialization(), userDto, null);
        UserPassResponse response = trainerService.createTrainer(traineeDto);
        logger.info("Trainer created successfully: {}", response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/trainer")
    public ResponseEntity<GetTrainerProfileResponse> getTraineeByUsername(@RequestParam("username") String username) {
        logger.info("Received request to get trainer by username: {}", username);
        GetTrainerProfileResponse response = entityMapper.toGetTrainerProfileResponse(trainerService.getTrainerByUsername(username));
        logger.info("Retrieved trainer profile: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/trainer")
    public ResponseEntity<UpdateTrainerProfileResponse> updateTrainee(
            @Valid @RequestBody UpdateTrainerProfileRequest newData) {
        logger.info("Received request to update trainer profile: {}", newData);
        UpdateTrainerProfileResponse response = trainerService.updateTrainer(entityMapper.toTrainerDao(newData));
        logger.info("Updated trainer profile: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{trainerId}/trainings")
    public ResponseEntity<List<GetTrainingResponse>> getTrainingList(
            @PathVariable Long trainerId,
            @Valid @RequestBody GetTrainerTrainingsRequest request) {
        logger.info("Received request to get training list for trainer: {}", request);
        List<GetTrainingResponse> response = trainerService.getTrainerTrainings(request);
        logger.info("Retrieved training list: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainee(@Valid @RequestBody UserPassRequest request) {
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
    public ResponseEntity<String> deactivateTrainee(@Valid @RequestBody UserPassRequest request) {
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

