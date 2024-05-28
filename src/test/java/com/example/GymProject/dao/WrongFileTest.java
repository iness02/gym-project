package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class WrongFileTest {
    @Mock
    private BufferedReader bufferedReader;
    @InjectMocks
    private TraineeDao traineeDao;
    @InjectMocks
    private TrainerDao trainerDao;
    @InjectMocks
    private TrainingDao trainingDao;

    @Test
    void traineeDaoTest() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> traineeDao.init());
    }

    @Test
    void trainerDaoTest() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> trainerDao.init());
    }

    @Test
    void trainingDaoTest() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> trainingDao.init());
    }
}