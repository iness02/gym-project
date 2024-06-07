package com.example.GymProject.service;

import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    @Autowired
    private TrainingDao trainingDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityMapper entityMapper;

    public TrainingDto addTraining(TrainingDto trainingDTO) {
        Assert.notNull(trainingDTO, "TrainingDto cannot be null");
        Training training = entityMapper.toTraining(trainingDTO);
        return entityMapper.toTrainingDto(trainingDAO.addTraining(training));
    }

    public List<TrainingDto> getAllTrainings() {
        List<Training> trainings = trainingDAO.getAllTrainings();
        return trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
    }

    public TrainingDto updateTraining(TrainingDto trainingDTO, String username, String password) {
        authenticate(username, password);
        Assert.notNull(trainingDTO, "TrainingDto cannot be null");
        Training training = entityMapper.toTraining(trainingDTO);
        return entityMapper.toTrainingDto(trainingDAO.updateTraining(training));
    }

    private void authenticate(String username, String password) {
        if (!userService.matchUsernameAndPassword(username, password)) {
            throw new RuntimeException("Authentication failed for user " + username);
        }
    }
}