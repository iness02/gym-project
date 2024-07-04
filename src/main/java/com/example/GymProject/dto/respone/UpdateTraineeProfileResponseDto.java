package com.example.GymProject.dto.respone;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeProfileResponseDto {
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private Set<TrainerForTraineeResponseDto> trainers;
}