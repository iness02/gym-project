package com.example.GymProject.dto.respone;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileResponseDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private Set<TraineeForTrainerResponseDto> trainees;
}