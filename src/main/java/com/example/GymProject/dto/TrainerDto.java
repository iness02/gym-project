package com.example.GymProject.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
    private Long id;
    private String specialization;
    private UserDto userDto;
    private Set<TraineeDto> trainees;

}