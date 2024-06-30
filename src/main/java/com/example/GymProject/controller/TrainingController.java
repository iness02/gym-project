package com.example.GymProject.controller;

import com.example.GymProject.dto.request.AddTrainingRequestDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.service.TrainingService;
import com.example.GymProject.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);


    @PostMapping
    public ResponseEntity<String> addTraining(@Valid @RequestBody AddTrainingRequestDto request) {
        logger.info("Received request to add training: {}", request);
        if (userService.checkUsernameAndPassword(request.getTrainerUsername(), request.getTrainerPassword())) {
            trainingService.addTraining(request);
            logger.info("Training added successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getTrainerUsername());

    }

}
