package com.example.GymProject.file;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.TrainingDao;
import org.junit.Test;
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
    public void traineeDao_init_fileNotFound_throwsException_Test() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> traineeDao.init());
    }

    @Test
    public void trainerDao_init_fileNotFound_throwsException_Test() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> trainerDao.init());
    }

    @Test
   public void trainingDao_init_fileNotFound_throwsException_Test() throws IOException {
        Mockito.when(bufferedReader.readLine()).thenThrow(IOException.class);

        assertThrows(FileNotFoundException.class, () -> trainingDao.init());
    }
}