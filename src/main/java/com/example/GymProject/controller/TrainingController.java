package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    TrainingService trainingService;
    @Autowired
    EntityMapper entityMapper;

    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);


    @PostMapping("/training")
    public ResponseEntity<String> addTraining(@Valid @RequestBody AddTrainingRequest request) {
        logger.info("Received request to add training: {}", request);
        TrainingDto trainingDto = entityMapper.toTrainingDto(request);
        trainingService.addTraining(trainingDto);
        logger.info("Training added successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
