package com.example.GymProject.controller;


import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.dto.request.UserPassRequestDto;
import com.example.GymProject.service.TrainingTypeService;
import com.example.GymProject.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainingType")
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeController.class);


    @GetMapping
    public ResponseEntity<?> getAllTrainingTypes(@Valid @RequestBody UserPassRequestDto request) {
        logger.info("Received request to get all training types");
        if (userService.checkUsernameAndPassword(request.getUsername(), request.getPassword())) {
            List<TrainingTypeDto> trainingTypes = trainingTypeService.getAllTrainingTypes();
            logger.info("Returning {} training types", trainingTypes.size());
            return new ResponseEntity<>(trainingTypes, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user:" + request.getUsername());
    }
}
