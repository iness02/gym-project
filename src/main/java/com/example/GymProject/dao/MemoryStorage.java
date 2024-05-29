package com.example.GymProject.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryStorage {

    private TrainerDao trainerRepository;
    private TraineeDao traineeRepository;
    private TrainingDao trainingRepository;
    private final List<String> usernames = new ArrayList<>();

    public TrainerDao getTrainerRepository() {
        return trainerRepository;
    }

    @Autowired
    public void setTrainerRepository(TrainerDao trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public TraineeDao getTraineeDao() {
        return traineeRepository;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
    public TrainingDao getTrainingDao() {
        return trainingRepository;
    }

    @Autowired
    public void setTrainingDao(TrainingDao trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    public List<String> getUsernames(){
        trainerRepository.findAll().forEach(trainer -> usernames.add(trainer.getUsername()));
        traineeRepository.findAll().forEach(trainee -> usernames.add(trainee.getUsername()));
        return usernames;
    }

}
