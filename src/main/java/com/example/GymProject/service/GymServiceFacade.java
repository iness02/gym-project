package com.example.GymProject.service;

import org.springframework.stereotype.Service;

@Service
public class GymServiceFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymServiceFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }
    public TraineeService getTraineeService() {
        return traineeService;
    }

    public TrainerService getTrainerService() {
        return trainerService;
    }

    public TrainingService getTrainingService() {
        return trainingService;
    }
}
