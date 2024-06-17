package com.example.GymProject.response.traineeResponse;


import com.example.GymProject.model.Trainings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerForTraineeResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
}
