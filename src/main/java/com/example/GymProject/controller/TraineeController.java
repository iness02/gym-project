package com.example.GymProject.controller;

import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.request.TraineeRequest;
import com.example.GymProject.request.UserLogin;
import com.example.GymProject.service.TraineeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<UserLogin> createTrainee(@Valid @RequestBody TraineeRequest traineeRequest){
        UserDto userDto = new UserDto(traineeRequest.getFirstName(),traineeRequest.getLastName());
        TraineeDto traineeDto = new TraineeDto(null,traineeRequest.getDateOfBirth(),traineeRequest.getAddress(),userDto,null);
        UserLogin response = traineeService.createTrainee(traineeDto);
        return ResponseEntity.ok(response);
    }
}
