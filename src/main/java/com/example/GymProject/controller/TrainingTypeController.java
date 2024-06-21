package com.example.GymProject.controller;


import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainingTypes")
public class TrainingTypeController {

    @Autowired
    TrainingTypeService trainingTypeService;

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeController.class);


    @GetMapping()
    public ResponseEntity<List<TrainingTypeDto>> getAllTrainingTypes() {
        logger.info("Received request to get all training types");

        List<TrainingTypeDto> trainingTypes = trainingTypeService.getAllTrainingTypes();

        logger.info("Returning {} training types", trainingTypes.size());
        return new ResponseEntity<>(trainingTypes, HttpStatus.OK);
    }
}

