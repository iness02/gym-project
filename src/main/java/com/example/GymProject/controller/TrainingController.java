package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.service.TrainingService;
import jakarta.validation.Valid;
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
    TrainingService trainingService;
    @Autowired
    EntityMapper entityMapper;

    @PostMapping("/training")
    public ResponseEntity<String> addTraining(@Valid @RequestBody AddTrainingRequest request) {
        TrainingDto trainingDto = entityMapper.toTrainingDto(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
