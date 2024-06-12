package com.example.GymProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    private Long id;
    private TraineeDto trainee;
    private TrainerDto trainer;
    @NotNull
    private String trainingName;
    @NotNull
    private TrainingTypeDto trainingType;
    @NotNull
    private LocalDate trainingDate;
    @NotNull
    private Integer trainingDuration;

}