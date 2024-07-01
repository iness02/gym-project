package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTrainingRequestDto {
    @Nonnull
    private String traineeUsername;
    @Nonnull
    private String trainerUsername;
    @Nonnull
    private String trainerPassword;
    @Nonnull
    private String name;
    @Nonnull
    private LocalDate date;
    @Nonnull
    private Integer duration;
}