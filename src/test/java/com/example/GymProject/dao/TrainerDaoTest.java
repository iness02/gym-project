package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainer;
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
public class TrainerDaoTest {
    @InjectMocks
    private TrainerDao trainerDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;
    @Mock
    private Query<Trainer> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        Trainer createdTrainer = trainerDao.createTrainer(trainer);
        assertEquals(trainer.getUser().getUsername(), createdTrainer.getUser().getUsername());
        verify(session, times(1)).persist(trainer);
        verify(transaction, times(1)).commit();
    }

    @Test
    public void testGetTrainerByUsername() {
        String username = "testuser";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        trainer.setUser(user);

        when(session.createQuery("FROM Trainer WHERE user.username = :username", Trainer.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainer);

        Trainer result = trainerDao.getTrainerByUsername(username);
        assertEquals(username, result.getUser().getUsername());
        verify(session, times(1)).close();
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        Trainer updatedTrainer = trainerDao.updateTrainer(trainer);
        assertEquals(trainer.getUser().getUsername(), updatedTrainer.getUser().getUsername());
        verify(session, times(1)).merge(trainer);
        verify(transaction, times(1)).commit();
    }
}
