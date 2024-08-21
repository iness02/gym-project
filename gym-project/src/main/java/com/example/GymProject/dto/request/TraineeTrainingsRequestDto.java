package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequestDto {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private String trainingType;
}
