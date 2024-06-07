package com.example.GymProject.service;

import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dto.TrainingDTO;
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

    public TrainingDTO addTraining(TrainingDTO trainingDTO) {
        Assert.notNull(trainingDTO, "TrainingDto cannot be null");
        Training training = EntityMapper.INSTANCE.trainingDTOToTraining(trainingDTO);
        return EntityMapper.INSTANCE.trainingToTrainingDTO(trainingDAO.addTraining(training));
    }

    public List<TrainingDTO> getAllTrainings() {
        List<Training> trainings = trainingDAO.getAllTrainings();
        return trainings.stream()
                .map(EntityMapper.INSTANCE::trainingToTrainingDTO)
                .collect(Collectors.toList());
    }

    public TrainingDTO updateTraining(TrainingDTO trainingDTO, String username, String password) {
        authenticate(username, password);
        Assert.notNull(trainingDTO, "TrainingDto cannot be null");
        Training training = EntityMapper.INSTANCE.trainingDTOToTraining(trainingDTO);
        return EntityMapper.INSTANCE.trainingToTrainingDTO(trainingDAO.updateTraining(training));
    }

    private void authenticate(String username, String password) {
        if (!userService.matchUsernameAndPassword(username, password)) {
            throw new RuntimeException("Authentication failed for user " + username);
        }
    }
}