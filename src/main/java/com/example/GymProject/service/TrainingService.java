package com.example.GymProject.service;

import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    @Autowired
    private TrainingDao trainingDao ;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityMapper entityMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public TrainingDto addTraining(TrainingDto trainingDTO) {
        Assert.notNull(trainingDTO, "TrainingDto cannot be null");
        System.out.println(trainingDTO);
        Training training = entityMapper.toTraining(trainingDTO);
        Trainee trainee = entityMapper.toTrainee(trainingDTO.getTrainee());
        Trainer trainer = entityMapper.toTrainer(trainingDTO.getTrainer());
        TrainingType trainingType = entityMapper.toTrainingType(trainingDTO.getTrainingType());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        return entityMapper.toTrainingDto(trainingDao.addTraining(training));
    }

    public List<TrainingDto> getAllTrainings() {
        List<Training> trainings = trainingDao.getAllTrainings();
        return trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
    }

    public TrainingDto updateTraining(TrainingDto trainingDTO, String username, String password) {
        if(isAuthenticated(username, password)) {
            Assert.notNull(trainingDTO, "TrainingDto cannot be null");
            Training training = entityMapper.toTraining(trainingDTO);
            return entityMapper.toTrainingDto(trainingDao.updateTraining(training));
        }
        logger.error("Authentication failed for trainee {}",username);
        return null;
    }

    public boolean isAuthenticated(String username,String password){
        return userService.matchUsernameAndPassword(username, password);
    }
}