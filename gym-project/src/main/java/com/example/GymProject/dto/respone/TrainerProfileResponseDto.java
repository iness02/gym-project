package com.example.GymProject.dto.respone;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponseDto {
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Set<TraineeForTrainerResponseDto> trainees;
}