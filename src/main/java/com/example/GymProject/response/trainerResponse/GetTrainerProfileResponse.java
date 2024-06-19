package com.example.GymProject.response.trainerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerProfileResponse {
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Set<TraineeForTrainerResponse> trainees;
}
