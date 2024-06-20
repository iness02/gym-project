package com.example.GymProject.dao;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.model.Trainee;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
class TraineeDaoTest {
    @InjectMocks
    private TraineeDao traineeDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainee> traineeQuery;

    @Mock
    private Query<Training> trainingQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }


    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("testuser");
        trainee.setUser(user);

        doNothing().when(session).persist(trainee);

        Trainee result = traineeDao.createTrainee(trainee);

        assertNotNull(result);
        assertEquals(trainee.getUser().getUsername(), result.getUser().getUsername());
        verify(session, times(1)).persist(trainee);
    }
    @Test
    public void testDeleteTraineeByUsername() {
        String username = "testuser";

        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);

        List<Training> trainings = new ArrayList<>();

        when(session.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);

        when(session.createQuery("Select t FROM Training t WHERE t.trainee.user.username = :username", Training.class))
                .thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.list()).thenReturn(trainings);

        doNothing().when(session).remove(any(Training.class));
        doNothing().when(session).remove(trainee);
        doNothing().when(session).remove(user);

        traineeDao.deleteTraineeByUsername(username);

        verify(session, times(1)).createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class);
        verify(traineeQuery, times(1)).setParameter("username", username);
        verify(traineeQuery, times(1)).uniqueResult();

        verify(session, times(1)).createQuery("Select t FROM Training t WHERE t.trainee.user.username = :username", Training.class);
        verify(trainingQuery, times(1)).setParameter("username", username);
        verify(trainingQuery, times(1)).list();

        verify(session, times(trainings.size())).remove(any(Training.class));
        verify(session, times(1)).remove(trainee);
        verify(session, times(1)).remove(user);
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);

        when(session.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", username)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);

        Trainee result = traineeDao.getTraineeByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUser().getUsername());
        verify(session, times(1)).createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class);
        verify(traineeQuery, times(1)).setParameter("username", username);
        verify(traineeQuery, times(1)).uniqueResult();
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("testuser");
        trainee.setUser(user);

        when(session.merge(trainee)).thenReturn(trainee);

        Trainee result = traineeDao.updateTrainee(trainee);

        assertNotNull(result);
        assertEquals(trainee.getUser().getUsername(), result.getUser().getUsername());
        verify(session, times(1)).merge(trainee);
    }
 @Test
    public void testGetTraineeTrainings() {
        String username = "testuser";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "testtrainer";
        String trainingType = "testtype";

        when(session.createQuery("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                "AND t.trainer.user.username = :trainerName " +
                "AND t.trainingType.trainingTypeName = :trainingType", Training.class))
                .thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", username)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("fromDate", fromDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("toDate", toDate)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("trainerName", trainerName)).thenReturn(trainingQuery);
        when(trainingQuery.setParameter("trainingType", trainingType)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(new ArrayList<>());

        List<Training> result = traineeDao.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(session, times(1)).createQuery("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
                "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                "AND t.trainer.user.username = :trainerName " +
                "AND t.trainingType.trainingTypeName = :trainingType", Training.class);
        verify(trainingQuery, times(1)).setParameter("username", username);
        verify(trainingQuery, times(1)).setParameter("fromDate", fromDate);
        verify(trainingQuery, times(1)).setParameter("toDate", toDate);
        verify(trainingQuery, times(1)).setParameter("trainerName", trainerName);
        verify(trainingQuery, times(1)).setParameter("trainingType", trainingType);
        verify(trainingQuery, times(1)).getResultList();
    }


}
