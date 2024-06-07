package com.example.GymProject.service;

import com.example.GymProject.dao.MemoryStorage;
import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainingService {
    @Autowired
    private MemoryStorage memoryStorage;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingDao trainingDao;

    public void createTraining(String traineeId, String trainerId, String trainingName,
                               TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        Trainee trainee = traineeService.selectTrainee(traineeId);
        Trainer trainer = trainerService.selectTrainer(trainerId);
        Training training = new Training(trainee, trainer, trainingName,
                trainingType, trainingDate, trainingDuration);
        trainingDao.create(training);
    }

    public Training selectTraining(String key) {
        return memoryStorage.getTrainingDao().select(key);
    }

}