package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.request.UserPassRequest;
import com.example.GymProject.request.trainerRequest.GetTrainerTrainingsRequest;
import com.example.GymProject.request.trainerRequest.TrainerRegistrationRequest;
import com.example.GymProject.request.trainerRequest.UpdateTrainerProfileRequest;
import com.example.GymProject.response.GetTrainingResponse;
import com.example.GymProject.response.UserPassResponse;
import com.example.GymProject.response.trainerResponse.GetTrainerProfileResponse;
import com.example.GymProject.response.trainerResponse.UpdateTrainerProfileResponse;
import com.example.GymProject.service.TrainerService;
import jakarta.validation.Valid;
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
    @Autowired
    private EntityMapper entityMapper;

    @PostMapping("/register")
    public ResponseEntity<UserPassResponse> createTrainee(@Valid @RequestBody TrainerRegistrationRequest registrationRequest) {
        UserDto userDto = new UserDto(registrationRequest.getFirstName(), registrationRequest.getLastName());
        TrainerDto traineeDto = new TrainerDto(null, registrationRequest.getSpecialization(), userDto, null);
        UserPassResponse response = trainerService.createTrainer(traineeDto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/trainer")
    public ResponseEntity<GetTrainerProfileResponse> getTraineeByUsername(@RequestParam("username") String username) {
        GetTrainerProfileResponse response = entityMapper.toGetTrainerProfileResponse(trainerService.getTrainerByUsername(username));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/trainer")
    public ResponseEntity<UpdateTrainerProfileResponse> updateTrainee(
            @Valid @RequestBody UpdateTrainerProfileRequest newData) {

        UpdateTrainerProfileResponse response = trainerService.updateTrainer(entityMapper.toTrainerDao(newData));
        return ResponseEntity.ok(response);

    }

    @GetMapping("/trainingList")
    public ResponseEntity<List<GetTrainingResponse>> getTrainingList(
            @Valid @RequestBody GetTrainerTrainingsRequest request) {

        List<GetTrainingResponse> response = trainerService.getTrainerTrainings(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainee(@Valid @RequestBody UserPassRequest request) {
        return trainerService.activate(request.getUsername(), request.getPassword())
                ? ResponseEntity.ok("Trainee activated successfully") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateTrainee(@Valid @RequestBody UserPassRequest request) {
        return trainerService.deactivate(request.getUsername(), request.getPassword())
                ? ResponseEntity.ok("Trainee deactivated successfully") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

}
