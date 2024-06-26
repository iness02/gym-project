package com.example.GymProject.dto.response.traineeResponse;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UnassignedTrainerResponse {
    private String username;
    private String firstName;
    private String lastname;
    private String specialization;
}
