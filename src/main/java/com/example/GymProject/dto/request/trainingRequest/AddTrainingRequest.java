package com.example.GymProject.dto.request.trainingRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTrainingRequest {
    @NotBlank
    private String traineeUsername;
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainerPassword;
    @NotBlank
    private String name;
    @NotBlank
    private Date date;
    @NotBlank
    private Integer duration;
}
