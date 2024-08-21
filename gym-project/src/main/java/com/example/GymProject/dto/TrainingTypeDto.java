package com.example.GymProject.dto;

import com.example.GymProject.model.Trainings;
import jakarta.annotation.Nonnull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainingTypeDto {
    private Long id;
    @Nonnull
    private Trainings trainingTypeName;
}
