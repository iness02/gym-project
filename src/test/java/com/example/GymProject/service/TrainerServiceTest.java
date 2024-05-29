package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TrainerServiceTest {
    @Autowired
    private TrainerService trainerService;

    @Test
    public  void selectTrainerTest(){
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        assertEquals("Inesa", trainerService.selectTrainer("inesa123").getFirstName());
    }
    @Test
    public void createTrainerWithSimilarUsernameTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa1234");

        assertEquals("Inesa.Hakobyan0", trainerService.selectTrainer("inesa1234").getUsername());
    }
    @Test
    public  void createTrainerWithDifferentUsernameTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerService.createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        assertNotEquals(trainerService.selectTrainer("inesa123").getUsername(),
                trainerService.selectTrainer("mels123").getUsername());
    }

    @Test
    public void updateTrainerTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        Trainer newTrainee = new Trainer("Mels", "Hakobyan", "Mels.Hakobyan", "password", true,
                TrainingType.CARDIO);

        trainerService.updateTrainer("inesa123", newTrainee);

        assertEquals("Mels", trainerService.selectTrainer("inesa123").getFirstName());
    }
    @Test
    public void updateNonExistedTrainerFailTest(){
        assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainer("test", new Trainer()));
    }
}