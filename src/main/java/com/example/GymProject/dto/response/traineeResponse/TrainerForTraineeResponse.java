package com.example.GymProject.dto.response.traineeResponse;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainerForTraineeResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
}
