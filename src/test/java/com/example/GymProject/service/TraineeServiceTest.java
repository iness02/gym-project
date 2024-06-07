package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TraineeServiceTest {
    @Autowired
    private TraineeService traineeService;

    @Test
    public void selectTraineeTest() {
        traineeService.createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        assertEquals("Inesa", traineeService.selectTrainee("inesa123").getFirstName());
    }

    @Test
    public void createTraineeWithSimilarUsernameTest() {
        traineeService.createTrainee("Karen", "Hakobyan", true,
                LocalDate.now(), "Armenia", "karen123");
        traineeService.createTrainee("Karen", "Hakobyan", true,
                LocalDate.now(), "Armenia", "karen1234");

        assertEquals("Karen.Hakobyan0",
                traineeService.selectTrainee("karen1234").getUsername());
    }

    @Test
    public void createTraineeWithDifferentUsernameTest() {
        traineeService.createTrainee("Nune", "Hakobyan", true,
                LocalDate.now(), "Armenia", "nune123");
        traineeService.createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa1234");

        assertNotEquals(traineeService.selectTrainee("nune123").getUsername(),
                traineeService.selectTrainee("inesa1234").getUsername());
    }

    @Test
    public void updateTraineeTest() {
        traineeService.createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        Trainee newTrainee = new Trainee("Hayk", "Hakobyan", "Hayk.Hakobyan", "password", true,
                LocalDate.now(), "Russia");

        traineeService.updateTrainee("inesa123", newTrainee);

        assertEquals("Hayk", traineeService.selectTrainee("inesa123").getFirstName());
    }

    @Test
    public void updateNonExistedTraineeFailTest() {
        assertThrows(NoSuchElementException.class, () -> traineeService.updateTrainee("test", new Trainee()));
    }

    @Test
    public void deleteTraineeTest() {
        traineeService.createTrainee("Inesa", "Hakobyan", true,
                LocalDate.now(), "Armenia", "inesa123");

        traineeService.deleteTrainee("inesa123");

        assertThrows(IllegalArgumentException.class, () -> traineeService.selectTrainee("sarah123"));
    }
}
