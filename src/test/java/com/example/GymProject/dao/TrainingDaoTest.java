package com.example.GymProject.dao;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
public class TrainingDaoTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;


    @Mock
    private Query<Training> query;

    @InjectMocks
    private TrainingDao trainingDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testAddTraining() {
        Training training = new Training();
        training.setId(1L);

        Training result = trainingDao.addTraining(training);

        verify(sessionFactory.getCurrentSession(), times(1)).persist(training);
        assertEquals(training, result);
    }


    @Test
    public void testGetAllTrainings() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(session.createQuery("Select t FROM Training t", Training.class)).thenReturn(query);
        when(query.list()).thenReturn(trainings);

        List<Training> result = trainingDao.getAllTrainings();

        verify(sessionFactory.getCurrentSession(), times(1)).createQuery("Select t FROM Training t", Training.class);
        verify(query, times(1)).list();
        assertEquals(trainings, result);
    }

    @Test
    public void testUpdateTraining() {
        Training training = new Training();
        training.setTrainingName("Yoga");

        Training result = trainingDao.updateTraining(training);

        verify(sessionFactory.getCurrentSession(), times(1)).merge(training);
        assertEquals(training, result);
    }
}
