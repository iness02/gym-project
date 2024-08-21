package com.example.GymProject.service;

import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.TrainingType;
import com.example.GymProject.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeService {

    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @Autowired
    EntityMapper entityMapper;

    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream()
                .map(entityMapper::toTrainingTypeDto)
                .collect(Collectors.toList());
    }
}
