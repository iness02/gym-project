package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TraineeDaoTest {
    @InjectMocks
    private TraineeDao traineeDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;
    @Mock
    private Query<Trainee> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("testuser");
        trainee.setUser(user);
        Trainee createdTrainee = traineeDao.createTrainee(trainee);
        assertEquals(trainee.getUser().getUsername(), createdTrainee.getUser().getUsername());
        verify(session, times(1)).persist(trainee);
        verify(transaction, times(1)).commit();
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);

        when(session.createQuery("FROM Trainee WHERE user.username = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        Trainee result = traineeDao.getTraineeByUsername(username);
        assertEquals(username, result.getUser().getUsername());
        verify(session, times(1)).close();
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("testuser");
        trainee.setUser(user);

        Trainee updatedTrainee = traineeDao.updateTrainee(trainee);
        assertEquals(trainee.getUser().getUsername(), updatedTrainee.getUser().getUsername());
        verify(session, times(1)).merge(trainee);
        verify(transaction, times(1)).commit();
    }
}
