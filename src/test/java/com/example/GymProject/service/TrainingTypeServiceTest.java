package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.TrainingTypeDao;
import com.example.GymProject.dto.TrainingTypeDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.TrainingType;
import com.example.GymProject.model.Trainings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrainingTypes() {

        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1L);
        trainingType1.setTrainingTypeName(Trainings.FITNESS);

        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2L);
        trainingType2.setTrainingTypeName(Trainings.CYCLE);

        List<TrainingType> trainingTypes = Arrays.asList(trainingType1, trainingType2);

        when(trainingTypeDao.getAllTrainingTypes()).thenReturn(trainingTypes);

        TrainingTypeDto trainingTypeDto1 = new TrainingTypeDto();
        trainingTypeDto1.setId(1L);
        trainingTypeDto1.setTrainingTypeName(Trainings.FITNESS);

        TrainingTypeDto trainingTypeDto2 = new TrainingTypeDto();
        trainingTypeDto2.setId(2L);
        trainingTypeDto2.setTrainingTypeName(Trainings.CYCLE);

        when(entityMapper.toTrainingTypeDto(trainingType1)).thenReturn(trainingTypeDto1);
        when(entityMapper.toTrainingTypeDto(trainingType2)).thenReturn(trainingTypeDto2);

        List<TrainingTypeDto> result = trainingTypeService.getAllTrainingTypes();

        assertEquals(2, result.size());
        assertEquals(Trainings.FITNESS, result.get(0).getTrainingTypeName());
        assertEquals(Trainings.CYCLE, result.get(1).getTrainingTypeName());

        verify(trainingTypeDao, times(1)).getAllTrainingTypes();
        verify(entityMapper, times(1)).toTrainingTypeDto(trainingType1);
        verify(entityMapper, times(1)).toTrainingTypeDto(trainingType2);
    }
}
