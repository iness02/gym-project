package com.example.GymProject.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private TraineeDto trainee;
    private TrainerDto trainer;
    @Nonnull
    private String trainingName;
    @Nonnull
    private TrainingTypeDto trainingType;
    @Nonnull
    private LocalDate trainingDate;
    @Nonnull
    private Integer trainingDuration;

}