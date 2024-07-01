package com.example.GymProject.dto.respone;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainerForTraineeResponseDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
}