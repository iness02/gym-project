package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainee;
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
public class TraineeDaoTest {
    @Autowired
    private TraineeDao traineeDao;

    @BeforeEach
    void deleteDataFromDaoTest() {
        for (Trainee trainee : traineeDao.findAll()) {
            if (traineeDao.containsKey(trainee.getUserId())) {
                traineeDao.delete(trainee.getUserId());
            }
        }
    }

    @Test
    public void containsTraineeTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");

        traineeDao.create(trainee);

        assertTrue(traineeDao.containsKey("inesa123"));
    }

    @Test
    public void selectTraineeTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");


        traineeDao.create(trainee);

        Trainee result = traineeDao.select("inesa123");
        assertNotNull(result);
        assertEquals(trainee, result);
    }

    @Test
    public void selectTraineeFailTest() {
        assertThrows(IllegalArgumentException.class, () -> traineeDao.select("test"));
    }

    @Test
    public void selectAllTraineesTest() {
        Trainee trainee1 = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");

        Trainee trainee2 = new Trainee("Elen", "Poghosyan",
                true, LocalDate.of(2002, 8, 8),
                "Yerevan", "elen123");

        traineeDao.create(trainee1);
        traineeDao.create(trainee2);

        assertEquals(2, traineeDao.findAll().size());
    }

    @Test
    public void createTraineeTest() {
        Trainee trainee1 = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");


        traineeDao.create(trainee1);

        Trainee trainee2 = traineeDao.select("inesa123");

        assertEquals(trainee1.getAddress(), trainee2.getAddress());
        assertEquals(trainee1.getFirstName(), trainee2.getFirstName());
        assertEquals(trainee1.getLastName(), trainee2.getLastName());
        assertEquals(trainee1.getActive(), trainee2.getActive());
        assertEquals(trainee1.getDob(), trainee2.getDob());
        assertEquals(trainee1.getUserId(), trainee2.getUserId());
    }

    @Test
    public void createTraineeWithNullUserIdFailTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", null);

        assertThrows(IllegalArgumentException.class, () -> traineeDao.create(trainee));
    }

    @Test
    public void deleteTraineeFromDaoTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");


        traineeDao.create(trainee);

        traineeDao.delete("inesa123");

        assertEquals(1, traineeDao.findAll().size());
    }

    @Test
    public void deleteNonExistedTraineeFailTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");


        traineeDao.create(trainee);

        assertThrows(IllegalArgumentException.class, () -> traineeDao.delete("inesa1234"));
    }

    @Test
    public void updateTraineeTest() {
        Trainee trainee = new Trainee("Inesa", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");

        Trainee newTrainee = new Trainee("Nune", "Karapetyan", "Nune.Karapetyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan", "inesa123");

        traineeDao.create(trainee);
        traineeDao.update("inesa123", newTrainee);

        assertNotEquals(trainee.getFirstName(), traineeDao.select("inesa123").getFirstName());
        assertNotEquals(trainee.getLastName(), traineeDao.select("inesa123").getLastName());
        assertEquals(trainee.getDob(), traineeDao.select("inesa123").getDob());
    }

    @Test
    public void updateNonExistedTraineeFailTest() {
        Trainee trainee = new Trainee("Nare", "Hakobyan", "Inesa.Hakobyan",
                "password", true, LocalDate.of(2002, 9, 22),
                "Yerevan");


        assertThrows(IllegalArgumentException.class, () -> traineeDao.update(trainee.getUserId(), trainee));
    }

}