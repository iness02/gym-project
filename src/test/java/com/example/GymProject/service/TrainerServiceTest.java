package com.example.GymProject.service;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TrainerServiceTest {
    @Autowired
    private TrainerService trainerService;

    @Test
    void selectTrainerTest(){
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        assertEquals("Inesa", trainerService.selectTrainer("inesa123").getFirstName());
    }
    @Test
    void createTrainerWithSimilarUsernameTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa1234");

        assertEquals("Inesa.Hakobyan0", trainerService.selectTrainer("inesa1234").getUsername());
    }
    @Test
    void createTrainerWithDifferentUsernameTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerService.createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        assertNotEquals(trainerService.selectTrainer("inesa123").getUsername(),
                trainerService.selectTrainer("mels123").getUsername());
    }

    @Test
    void updateTrainerTest() {
        trainerService.createTrainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        Trainer newTrainee = new Trainer("Mels", "Hakobyan", "Mels.Hakobyan", "password", true,
                TrainingType.CARDIO);

        trainerService.updateTrainer("inesa123", newTrainee);

        assertEquals("Mels", trainerService.selectTrainer("inesa123").getFirstName());
    }
    @Test
    void updateTrainerFailTest(){
        assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainer("test", new Trainer()));
    }
}