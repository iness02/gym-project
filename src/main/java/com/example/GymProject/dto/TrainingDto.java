package com.example.GymProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
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
    private Date trainingDate;
    @NotNull
    private Integer trainingDuration;

}