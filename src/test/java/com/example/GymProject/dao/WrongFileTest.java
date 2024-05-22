package com.example.GymProject.dao;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
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