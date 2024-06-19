package com.example.GymProject.controller;


import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.service.TrainingTypeService;
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

    @GetMapping("/trainingType")
    public ResponseEntity<List<TrainingTypeDto>> getAllTrainingTypes() {

        return new ResponseEntity<>(trainingTypeService.getAllTrainingTypes(), HttpStatus.OK);
    }
}

