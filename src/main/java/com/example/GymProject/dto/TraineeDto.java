package com.example.GymProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private Long id;
    private Date dateOfBirth;
    private String address;
    private UserDto user;
    private Set<TrainerDto> trainers;
}
