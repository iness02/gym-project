package com.example.GymProject.response.trainerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private Set<TraineeForTrainerResponse> trainees;
}