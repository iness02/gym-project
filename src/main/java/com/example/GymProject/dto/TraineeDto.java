package com.example.GymProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private UserDto userDto;
    private Set<TrainerDto> trainers;
}