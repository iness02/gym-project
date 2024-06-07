package com.example.GymProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    private Long id;
    private TraineeDTO trainee;
    private TrainerDTO trainer;
    @NotNull
    private String trainingName;
    @NotNull
    private TrainingTypeDTO trainingType;
    @NotNull
    private Date trainingDate;
    @NotNull
    private Integer trainingDuration;
}