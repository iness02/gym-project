package com.example.GymProject.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GymServiceFacadeTest {
    @Autowired
    private GymServiceFacade gymServiceFacade;

    @Test
    void getTraineeService() {
        assertNotNull(gymServiceFacade.getTraineeService());
    }

    @Test
    void getTrainerService() {
        assertNotNull(gymServiceFacade.getTrainerService());
    }

    @Test
    void getTrainingService() {
        assertNotNull(gymServiceFacade.getTrainingService());
    }
}
