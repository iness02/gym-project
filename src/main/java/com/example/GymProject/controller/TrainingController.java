package com.example.GymProject.controller;

import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.service.TrainingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    TrainingService trainingService;
    @Autowired
    EntityMapper entityMapper;

    @PostMapping("/training")
    public ResponseEntity<String> addTraining(@Valid @RequestBody AddTrainingRequest request){
        TrainingDto trainingDto = entityMapper.toTrainingDto(request);

      if(trainingService.addTraining(trainingDto) == null) {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(HttpStatus.OK);
    }

}
