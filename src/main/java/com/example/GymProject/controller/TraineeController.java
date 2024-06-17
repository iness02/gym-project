package com.example.GymProject.controller;

import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.request.*;
import com.example.GymProject.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.request.traineerRquest.TraineeRegistrationRequest;
import com.example.GymProject.request.traineerRquest.UpdateTraineeProfileRequest;
import com.example.GymProject.request.traineerRquest.UpdateTraineeTrainersRequest;
import com.example.GymProject.response.UserPassResponse;
import com.example.GymProject.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.response.GetTrainingResponse;
import com.example.GymProject.response.traineeResponse.TrainerForTraineeResponse;
import com.example.GymProject.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.service.TraineeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;
    @Autowired
    private EntityMapper entityMapper;

    @PostMapping("/register")
    public ResponseEntity<UserPassResponse> createTrainee(@Valid @RequestBody TraineeRegistrationRequest registrationRequest) {
        UserDto userDto = new UserDto(registrationRequest.getFirstName(), registrationRequest.getLastName());
        TraineeDto traineeDto = new TraineeDto(null, registrationRequest.getDateOfBirth(), registrationRequest.getAddress(), userDto, null);
        UserPassResponse response = traineeService.createTrainee(traineeDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trainee")
    public ResponseEntity<GetTraineeProfileResponse> getTraineeByUsername(@RequestParam("username") String username) {
        GetTraineeProfileResponse response = traineeService.getTraineeByUsername(username);
        System.out.println(response);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/trainee")
    public ResponseEntity<UpdateTraineeProfileResponse> updateTrainee(
            @Valid @RequestBody UpdateTraineeProfileRequest newData) {

        TraineeDto traineeDto = entityMapper.toTraineeDao(newData);
        System.out.println("******" + newData.getDateOfBirth() + " trainee " + traineeDto.getDateOfBirth());
        UpdateTraineeProfileResponse response = traineeService.updateTrainee(traineeDto);
        System.out.println(response.getDateOfBirth());
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/trainee")
    public ResponseEntity<String> deleteTrainee(
            @Valid @RequestBody DeleteRequest request) {
        System.out.println(request);
        boolean isDeleted = traineeService.deleteTraineeByUsername(request.getUsername(), request.getPassword());
        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/trainersList")
    public ResponseEntity<List<TrainerForTraineeResponse>> updateTraineeTrainers(
            @Valid @RequestBody UpdateTraineeTrainersRequest requestDto) {

        List<TrainerForTraineeResponse> response = traineeService.
                updateTraineeTrainers(requestDto.getUsername(), requestDto.getPassword(), requestDto.getTrainerUsernames());
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/trainingList")
    public ResponseEntity<List<GetTrainingResponse>> getTrainingList(
            @Valid @RequestBody GetTraineeTrainingsRequest request) {
        List<GetTrainingResponse> response = traineeService.getTraineeTrainings(request);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    public ResponseEntity<String> activateTrainee(@Valid @RequestBody UserPassRequest request){
       return traineeService.activate(request.getUsername(), request.getPassword())
               ? new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateTrainee(@Valid @RequestBody UserPassRequest request){
       return traineeService.deactivate(request.getUsername(), request.getPassword())
               ? new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
