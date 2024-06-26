package com.example.GymProject.service;

import com.example.GymProject.dao.TrainingTypeDao;
import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeService {

    @Autowired
    TrainingTypeDao trainingTypeDao;

    @Autowired
    EntityMapper entityMapper;

    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeDao.getAllTrainingTypes();
        return trainingTypes.stream()
                .map(entityMapper::toTrainingTypeDto)
                .collect(Collectors.toList());
    }


}
