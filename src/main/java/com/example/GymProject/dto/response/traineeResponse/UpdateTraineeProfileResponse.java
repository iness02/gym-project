package com.example.GymProject.dto.response.traineeResponse;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeProfileResponse {
    private String userName;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private Boolean isActive;
    private Set<TrainerForTraineeResponse> trainers;
}
