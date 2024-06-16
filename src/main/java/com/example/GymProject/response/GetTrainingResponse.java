package com.example.GymProject.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainingResponse {
    private String name;
    private LocalDate date;
    private String type;
    private Integer duration;
    private String trainerName;
}
