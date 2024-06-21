package com.example.GymProject.dto.response.trainerResponse;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
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
