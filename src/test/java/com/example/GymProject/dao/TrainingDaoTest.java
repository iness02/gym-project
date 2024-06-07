package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TrainingDaoTest {
    @InjectMocks
    private TrainingDao trainingDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;
    @Mock
    private Query<Training> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testAddTraining() {
        Training training = new Training();
        training.setId(1L);

        Training addedTraining = trainingDao.addTraining(training);
        assertEquals(training.getId(), addedTraining.getId());
        verify(session, times(1)).persist(training);
        verify(transaction, times(1)).commit();
    }

    @Test
    public void testGetAllTrainings() {
        Training training1 = new Training();
        training1.setId(1L);
        Training training2 = new Training();
        training2.setId(2L);

        List<Training> trainingList = Arrays.asList(training1, training2);

        when(session.createQuery("FROM Training", Training.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainingList);

        List<Training> result = trainingDao.getAllTrainings();
        assertEquals(2, result.size());
        verify(session, times(1)).close();
    }

    @Test
    public void testUpdateTraining() {
        Training training = new Training();
        training.setTrainingName("Test Training");

        Training updatedTraining = trainingDao.updateTraining(training);
        assertEquals(training.getTrainingName(), updatedTraining.getTrainingName());
        verify(session, times(1)).merge(training);
        verify(transaction, times(1)).commit();
    }
}
