package com.example.GymProject.service;

import com.example.GymProject.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TrainingServiceTest {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private GymServiceFacade gymServiceFacade;

    @Test
    void createTrainingTest() {
        gymServiceFacade.getTraineeService().createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        gymServiceFacade.getTrainerService().createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        assertDoesNotThrow(() -> trainingService.createTraining("inesa123", "mels123", "myFirstTraining",
                TrainingType.FITNESS, LocalDate.now(), 5));
    }

    @Test
    void selectTrainingTest() {
        gymServiceFacade.getTraineeService().createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        gymServiceFacade.getTrainerService().createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        trainingService.createTraining("inesa123", "mels123○", "myFirstTraining",
                TrainingType.FITNESS, LocalDate.now(), 5);

        assertEquals("myFirstTraining", trainingService.selectTraining("myFirstTraining").getTrainingName());
    }
}