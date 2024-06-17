package com.example.GymProject.request.trainerRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingsRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
}