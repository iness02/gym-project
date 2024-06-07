package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
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
    public void getTrainerRepositoryTest() {
        assertEquals(memoryStorage.getTrainerRepository(), trainerDao);
    }

    @Test
    public void getTraineeRepositoryTest() {
        assertEquals(memoryStorage.getTraineeDao(), traineeDao);
    }

    @Test
    public void getTrainingRepositoryTest() {
        assertEquals(memoryStorage.getTrainingDao(), trainingDao);
    }

    @Test
    public void getUsernames_findAll() {
        for (Trainee trainee : traineeDao.findAll()) {
            traineeDao.delete(trainee.getUserId());
        }
        for (Trainer trainer : trainerDao.findAll()) {
            trainerDao.delete(trainer.getUserId());
        }

        assertEquals(32, memoryStorage.getUsernames().size());
    }
}
