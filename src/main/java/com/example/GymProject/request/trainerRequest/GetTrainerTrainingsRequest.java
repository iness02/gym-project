package com.example.GymProject.request.trainerRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainerTrainingsRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Date periodFrom;
    private Date periodTo;
    private String trainerName;
}
