package com.example.trainer_workload.controller;

import com.example.trainer_workload.dto.TrainingRequest;
import com.example.trainer_workload.service.TrainingWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor
public class TrainingWorkController {
    private final TrainingWorkService trainingWorkService;

    @PostMapping
    public ResponseEntity<String> actionTraining(@RequestBody TrainingRequest trainingRequest) {
        trainingWorkService.acceptTrainerWork(trainingRequest);
        return new ResponseEntity<>(trainingRequest.getAction() + " Action Completed successfully", HttpStatus.OK);
    }
}