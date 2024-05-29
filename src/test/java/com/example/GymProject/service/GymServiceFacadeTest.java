package com.example.GymProject.service;


import com.example.GymProject.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})public class GymServiceFacadeTest {
    @Autowired
    private GymServiceFacade gymServiceFacade;

    @Test
   public void getTraineeService() {
        assertNotNull(gymServiceFacade.getTraineeService());
    }

    @Test
   public void getTrainerService() {
        assertNotNull(gymServiceFacade.getTrainerService());
    }

    @Test
   public void getTrainingService() {
        assertNotNull(gymServiceFacade.getTrainingService());
    }
}
