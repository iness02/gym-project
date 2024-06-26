package com.example.GymProject.dao;

import com.example.GymProject.config.TestConfig;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
public class TrainerDaoTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainer> trainerQuery;

    @Mock
    private Query<Long> countQuery;

    @Mock
    private Query<Training> trainingQuery;

    @InjectMocks
    private TrainerDao trainerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void testCreateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        doNothing().when(session).persist(trainer);

        Trainer result = trainerDao.createTrainer(trainer);

        assertNotNull(result);
        verify(session).persist(trainer);
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "testuser";
        Trainer trainer = new Trainer();
        when(session.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);

        Trainer result = trainerDao.getTrainerByUsername(username);

        assertNotNull(result);
        verify(trainerQuery).setParameter("username", username);
        verify(trainerQuery).uniqueResult();
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("testuser");
        trainer.setUser(user);

        when(session.createQuery("select t from Trainer t where t.user.username = :username"))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", user.getUsername())).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);

        Trainer result = trainerDao.updateTrainer(trainer);

        assertNotNull(result);
        verify(session).update(trainer);
    }

    @Test
    void testDeleteTrainerByUsername() {
        String username = "testuser";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        trainer.setUser(user);

        when(session.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", username)).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);

        doNothing().when(session).remove(trainer);

        trainerDao.deleteTrainerByUsername(username);

        verify(session).remove(trainer);
        verify(session).createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class);
        verify(trainerQuery).setParameter("username", username);
        verify(trainerQuery).uniqueResult();
    }

    @Test
    void testExistsByUsername() {
        String username = "testuser";
        String hql = "SELECT COUNT(t) FROM Trainer t WHERE t.user.username = :username";

        when(session.createQuery(hql)).thenReturn(countQuery);
        when(countQuery.setParameter("username", username)).thenReturn(countQuery);
        when(countQuery.uniqueResult()).thenReturn(1L);

        boolean exists = trainerDao.existsByUsername(username);

        assertTrue(exists);
        verify(session).createQuery(hql);
        verify(countQuery).setParameter("username", username);
        verify(countQuery).uniqueResult();
    }

    @Test
    void testGetAllTrainers() {
        when(session.createQuery("SELECT t FROM Trainer t", Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(new Trainer()));

        List<Trainer> result = trainerDao.getAllTrainers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(trainerQuery).getResultList();
    }

    @Test
    void testGetTrainerTrainings() {
        String username = "testuser";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "trainee";

        when(session.createQuery("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
                "OR t.trainingDate BETWEEN :fromDate AND :toDate " +
                "OR t.trainee.user.username = :traineeName", Training.class))
                .thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("fromDate", fromDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("toDate", toDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("traineeName", traineeName)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(List.of(new Training()));

        List<Training> result = trainerDao.getTrainerTrainings(username, fromDate, toDate, traineeName);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(trainingQuery).setParameter("username", username);
        verify(trainingQuery).setParameter("fromDate", fromDate);
        verify(trainingQuery).setParameter("toDate", toDate);
        verify(trainingQuery).setParameter("traineeName", traineeName);
        verify(trainingQuery).getResultList();
    }
}
