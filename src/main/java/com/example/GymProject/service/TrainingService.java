package com.example.GymProject.service;

import com.example.GymProject.dao.MemoryStorage;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainingService {
    private MemoryStorage memoryStorage;
    private TraineeService traineeService;
    private TrainerService trainerService;
    @Autowired
    public void setMemoryStorage(MemoryStorage memoryStorage) {
        this.memoryStorage = memoryStorage;
    }
    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }
    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }
    public void createTraining(String traineeId, String trainerId, String trainingName,
                               TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration){
        Trainee trainee = traineeService.selectTrainee(traineeId);
        Trainer trainer = trainerService.selectTrainer(trainerId);
        Training training = new Training(trainee, trainer, trainingName,
                trainingType, trainingDate, trainingDuration);
        memoryStorage.getTrainingRepository().create(training);
    }
    public Training selectTraining(String key){
        return memoryStorage.getTrainingRepository().select(key);
    }

}