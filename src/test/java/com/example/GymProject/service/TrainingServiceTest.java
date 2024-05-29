package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.TrainingType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TrainingServiceTest {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private GymServiceFacade gymServiceFacade;

    @Test
    public void createTrainingTest() {
        gymServiceFacade.getTraineeService().createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        gymServiceFacade.getTrainerService().createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        assertDoesNotThrow(() -> trainingService.createTraining("inesa123", "mels123", "myFirstTraining",
                TrainingType.FITNESS, LocalDate.now(), 5));
    }

    @Test
    public void selectTrainingTest() {
        gymServiceFacade.getTraineeService().createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        gymServiceFacade.getTrainerService().createTrainer("Mels", "Hakobyan", true,
                TrainingType.FITNESS, "mels123");

        trainingService.createTraining("inesa123", "mels123○", "myFirstTraining",
                TrainingType.FITNESS, LocalDate.now(), 5);

        assertEquals("myFirstTraining", trainingService.selectTraining("myFirstTraining").getTrainingName());
    }
}