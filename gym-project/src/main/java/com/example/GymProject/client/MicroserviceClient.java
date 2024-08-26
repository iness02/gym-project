package com.example.GymProject.client;

import com.example.GymProject.dto.request.TrainingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MICROSERVICE", fallback = MicroserviceFallback.class)
public interface MicroserviceClient {

    @PostMapping("/workload")
    void actionTraining(@RequestBody TrainingRequest trainingRequest,
                        @RequestHeader("Transaction-ID") String transactionId,
                        @RequestHeader("Authorization") String Authorization);
}