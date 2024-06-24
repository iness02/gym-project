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

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
class TraineeDaoTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainee> traineeQuery;

    @Mock
    private Query<Training> trainingQuery;

    @Mock
    private Query<Long> longQuery;

    @Mock
    private Query<Training> trainingListQuery;

    @InjectMocks
    private TraineeDao traineeDao;

    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);

        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");

        trainee = new Trainee();
        trainee.setUser(user);
    }

    @Test
    void testCreateTrainee() {
        doNothing().when(session).persist(trainee);

        Trainee result = traineeDao.createTrainee(trainee);

        verify(session, times(1)).persist(trainee);
        assertEquals(trainee, result);
    }

    @Test
    void testGetTraineeByUsername() {
        when(session.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "testuser")).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);

        Trainee result = traineeDao.getTraineeByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUser().getUsername());
    }

    @Test
    void testUpdateTrainee() {
        when(session.createQuery("select t from Trainee t where t.user.username = :username"))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "testuser")).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);

        Trainee updatedTrainee = new Trainee();
        User updatedUser = new User();
        updatedUser.setUsername("testuser");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedTrainee.setUser(updatedUser);
        updatedTrainee.setAddress("New Address");
        updatedTrainee.setDateOfBirth(new Date());
        updatedTrainee.setTrainers(null);

        Trainee result = traineeDao.updateTrainee(updatedTrainee);

        assertNotNull(result);
        assertEquals("testuser", result.getUser().getUsername());
        assertEquals("Updated", result.getUser().getFirstName());
        assertEquals("New Address", result.getAddress());
    }

    @Test
    void testUpdateTrainee_NotFound() {
        when(session.createQuery("select t from Trainee t where t.user.username = :username"))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "nonexistent")).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(null);

        Trainee updatedTrainee = new Trainee();
        User updatedUser = new User();
        updatedUser.setUsername("nonexistent");
        updatedTrainee.setUser(updatedUser);

        assertThrows(EntityNotFoundException.class, () -> traineeDao.updateTrainee(updatedTrainee));
    }

  /*  @Test
    void testDeleteTraineeByUsername() {
        when(session.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", "testuser")).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);

        doNothing().when(session).remove(any(Trainee.class));
        doNothing().when(session).remove(any(User.class));
        when(session.createQuery("Select t FROM Training t WHERE t.trainee.user.username = :username", Training.class))
                .thenReturn(trainingQuery);
        when(trainingQuery.setParameter("username", "testuser")).thenReturn(trainingQuery);
        when(trainingQuery.list()).thenReturn(List.of());

        traineeDao.deleteTraineeByUsername("testuser");

        verify(session, times(1)).remove(trainee);
        verify(session, times(1)).remove(user);
    }*/

    @Test
    void testGetTraineeTrainings() {
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingType = "type";

        when(session.createQuery("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
                "OR t.trainingDate BETWEEN :fromDate AND :toDate " +
                "OR t.trainer.user.username = :trainerName " +
                "OR t.trainingType.trainingTypeName = :trainingType", Training.class))
                .thenReturn(trainingListQuery);
        when(trainingListQuery.setParameter("username", "testuser")).thenReturn(trainingListQuery);
        when(trainingListQuery.setParameter("fromDate", fromDate)).thenReturn(trainingListQuery);
        when(trainingListQuery.setParameter("toDate", toDate)).thenReturn(trainingListQuery);
        when(trainingListQuery.setParameter("trainerName", trainerName)).thenReturn(trainingListQuery);
        when(trainingListQuery.setParameter("trainingType", trainingType)).thenReturn(trainingListQuery);
        when(trainingListQuery.getResultList()).thenReturn(List.of());

        List<Training> trainings = traineeDao.getTraineeTrainings("testuser", fromDate, toDate, trainerName, trainingType);

        assertNotNull(trainings);
    }

    @Test
    void testExistsByUsername() {
        String username = "testuser";
        String hql = "SELECT COUNT(t) FROM Trainee t WHERE t.user.username = :username";

        when(session.createQuery(hql)).thenReturn(longQuery);
        when(longQuery.setParameter("username", username)).thenReturn(longQuery);
        when(longQuery.uniqueResult()).thenReturn(1L);

        boolean exists = traineeDao.existsByUsername(username);

        assertTrue(exists);
        verify(session).createQuery(hql);
        verify(longQuery).setParameter("username", username);
        verify(longQuery).uniqueResult();
    }
}
