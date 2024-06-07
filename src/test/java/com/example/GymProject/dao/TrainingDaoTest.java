package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TrainingDaoTest {
    @Autowired
    private TrainingDao trainingDao;

    @BeforeEach
    void deleteDataFromDao() {
        for (Training training : trainingDao.findAll()) {
            if (trainingDao.containsKey(training.getTrainingName())) {
                trainingDao.delete(training.getTrainingName());
            }
        }
    }

    @Test
    public void containsTrainingTest() {
        Trainee trainee = new Trainee();
        trainee.setUserId("trainee");

        Trainer trainer = new Trainer();
        trainer.setUserId("trainer");
        Training training = new Training(trainee, trainer,
                "myFirstTraining", TrainingType.FITNESS, LocalDate.now(), 5);

        trainingDao.create(training);

        assertTrue(trainingDao.containsKey("myFirstTraining"));
    }

    @Test
    public void createTrainingTest() {
        Trainee trainee = new Trainee();
        trainee.setUserId("trainee");

        Trainer trainer = new Trainer();
        trainer.setUserId("trainer");
        Training training = new Training(trainee, trainer,
                "myFirstTraining", TrainingType.FITNESS, LocalDate.now(), 5);

        trainingDao.create(training);

        Training newTraining = trainingDao.select(training.getTrainingName());

        assertEquals(training.getTrainingName(), newTraining.getTrainingName());
        assertEquals(training.getTrainingDuration(), newTraining.getTrainingDuration());
        assertEquals(training.getTraineeId(), newTraining.getTraineeId());
        assertEquals(training.getTrainerId(), newTraining.getTrainerId());
    }

    @Test
    public void selectTrainingTest() {
        Trainee trainee = new Trainee();
        trainee.setUserId("trainee");

        Trainer trainer = new Trainer();
        trainer.setUserId("trainer");
        Training training = new Training(trainee, trainer,
                "myFirstTraining", TrainingType.FITNESS, LocalDate.now(), 5);

        trainingDao.create(training);

        Training expected = trainingDao.select(training.getTrainingName());

        assertNotNull(expected);
        assertEquals(expected, training);
    }

    @Test
    public void selectNonExistedTrainingFailTest() {
        assertThrows(IllegalArgumentException.class, () -> trainingDao.select("test"));
    }

    @Test
    public void selectAllTraining() {
        Trainee trainee = new Trainee();
        trainee.setUserId("trainee");

        Trainer trainer = new Trainer();
        trainer.setUserId("trainer");
        Training training1 = new Training(trainee, trainer,
                "myFirstTraining", TrainingType.FITNESS, LocalDate.now(), 5);
        Training training2 = new Training(trainee, trainer,
                "mySecondTraining", TrainingType.FITNESS, LocalDate.now(), 5);

        trainingDao.create(training1);
        trainingDao.create(training2);

        assertEquals(2, trainingDao.findAll().size());
    }

    @Test
    public void deleteNonExistedTrainingFailTest() {
        assertThrows(IllegalArgumentException.class, () -> trainingDao.delete("test"));
    }
}