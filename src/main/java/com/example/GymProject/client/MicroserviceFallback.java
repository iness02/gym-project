package com.example.GymProject.client;

import com.example.GymProject.dto.request.TrainingRequest;
import org.springframework.stereotype.Component;

@Component
public class MicroserviceFallback implements MicroserviceClient {

    @Override
    public void actionTraining(TrainingRequest trainingRequest, String transactionId, String Authorization) {
        System.out.println("Microservice is not available");
    }
}