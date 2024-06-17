package com.example.GymProject.response.trainerResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeForTrainerResponse {
    private String userName;
    private String firstName;
    private String lastName;
}
