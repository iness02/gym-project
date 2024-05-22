package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemoryStorageTest {
    @Autowired
    private MemoryStorage memoryStorage;
    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private TrainingDao trainingDao;


    @Test
    void getTrainerRepositoryTest() {
        assertEquals(memoryStorage.getTrainerRepository(), trainerDao);
    }

    @Test
    void getTraineeRepositoryTest() {
        assertEquals(memoryStorage.getTraineeRepository(), traineeDao);
    }

    @Test
    void getTrainingRepositoryTest() {
        assertEquals(memoryStorage.getTrainingRepository(), trainingDao);
    }

    @Test
    void getUsernamesTest() {
        for(Trainee trainee : traineeDao.findAll()){
            traineeDao.delete(trainee.getUserId());
        }
        for(Trainer trainer : trainerDao.findAll()){
            trainerDao.delete(trainer.getUserId());
        }

        assertEquals(0, memoryStorage.getUsernames().size());
    }
}
