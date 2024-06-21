package com.example.GymProject.dto.response.trainerResponse;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerProfileResponse {
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Set<TraineeForTrainerResponse> trainees;
}
