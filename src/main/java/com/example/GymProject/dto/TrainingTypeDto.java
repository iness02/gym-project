package com.example.GymProject.dto;

import com.example.GymProject.model.Trainings;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainingTypeDto {
    private Long id;
    @NotNull
    private Trainings trainingTypeName;
}