package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@EnableTransactionManagement
@EnableWebMvc

public class TrainerDaoTest {
    @InjectMocks
    private TrainerDao trainerDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainer> trainerQuery;

    @Mock
    private Query<Training> trainingQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        doNothing().when(session).persist(trainer);

        Trainer result = trainerDao.createTrainer(trainer);

        assertNotNull(result);
        assertEquals(trainer.getUser().getUsername(), result.getUser().getUsername());
        verify(session, times(1)).persist(trainer);
    }

    @Test
    public void testGetTrainerByUsername() {
        String username = "testuser";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        trainer.setUser(user);

        when(session.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);

        Trainer result = trainerDao.getTrainerByUsername(username);
        assertNotNull(result);
        assertEquals(username, result.getUser().getUsername());
        verify(session, times(1)).createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class);
    }

    @Test
    public void testGetTrainerByUsernameNotFound() {
        String username = "testuser";

        when(session.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(null);

        Trainer result = trainerDao.getTrainerByUsername(username);
        assertNull(result);
        verify(session, times(1)).createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class);
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        when(session.merge(trainer)).thenReturn(trainer);

        Trainer result = trainerDao.updateTrainer(trainer);
        assertNotNull(result);
        assertEquals(trainer.getUser().getUsername(), result.getUser().getUsername());
        verify(session, times(1)).merge(trainer);
    }

    @Test
    public void testDeleteTrainerByUsername() {
        String username = "testuser";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        trainer.setUser(user);

        when(session.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);
        doNothing().when(session).remove(trainer);
        doNothing().when(session).remove(user);

        trainerDao.deleteTrainerByUsername(username);

        verify(session, times(1)).remove(trainer);
        verify(session, times(1)).remove(user);
    }

    @Test
    public void testGetTrainerTrainings() {
        String username = "trainer";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee";
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());

        when(session.createQuery("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                "AND t.trainee.user.username = :traineeName", Training.class)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("fromDate", fromDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("toDate", toDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("traineeName", traineeName)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(trainings);

        List<Training> result = trainerDao.getTrainerTrainings(username, fromDate, toDate, traineeName);

        assertEquals(1, result.size());
        verify(session, times(1)).createQuery("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                "AND t.trainee.user.username = :traineeName", Training.class);
    }

    @Test
    public void testGetAllTrainers() {

        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(session.createQuery("SELECT t FROM Trainer t", Trainer.class))
                .thenReturn(mock(org.hibernate.query.Query.class));
        when(session.createQuery("SELECT t FROM Trainer t", Trainer.class).getResultList())
                .thenReturn(trainers);

        List<Trainer> result = trainerDao.getAllTrainers();

        assertEquals(trainers, result);
    }
}
