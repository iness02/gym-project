package com.example.GymProject.dto.respone;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UnassignedTrainerResponseDto {
    private String username;
    private String firstName;
    private String lastname;
    private String specialization;
}
