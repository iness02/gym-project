package com.example.GymProject.dto;

import com.example.GymProject.model.Trainings;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto {
        private Long id;
        @NotNull
        private Trainings trainingTypeName;
}