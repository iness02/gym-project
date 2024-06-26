package com.example.GymProject.dao;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.model.TrainingType;
import com.example.GymProject.model.Trainings;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
public class TrainingTypeDaoTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<TrainingType> query;

    @InjectMocks
    private TrainingTypeDao trainingTypeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTrainingType() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        doNothing().when(session).persist(trainingType);

        TrainingType result = trainingTypeDao.addTrainingType(trainingType);

        assertEquals(trainingType, result);
        verify(session, times(1)).persist(trainingType);
    }

    @Test
    void testFindTrainingByName() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainingType);

        TrainingType result = trainingTypeDao.findTrainingByName(Trainings.FITNESS);

        assertEquals(trainingType, result);
        verify(session, times(1)).createQuery(anyString(), eq(TrainingType.class));
        verify(query, times(1)).setParameter(anyString(), any());
        verify(query, times(1)).uniqueResult();
    }

    @Test
    void testGetTrainingTypeByName() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainingType);

        TrainingType result = trainingTypeDao.getTrainingTypeByName("Fitness");

        assertEquals(trainingType, result);
        verify(session, times(1)).createQuery(anyString(), eq(TrainingType.class));
        verify(query, times(1)).setParameter(anyString(), any());
        verify(query, times(1)).uniqueResult();
    }

    @Test
    void testGetAllTrainingTypes() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType());
        trainingTypes.add(new TrainingType());

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
        when(query.list()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeDao.getAllTrainingTypes();

        assertEquals(trainingTypes.size(), result.size());
        verify(session, times(1)).createQuery(anyString(), eq(TrainingType.class));
        verify(query, times(1)).list();
    }
}
