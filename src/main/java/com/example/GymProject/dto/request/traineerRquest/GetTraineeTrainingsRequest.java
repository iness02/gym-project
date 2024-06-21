package com.example.GymProject.dto.request.traineerRquest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTraineeTrainingsRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Date periodFrom;
    private Date periodTo;
    private String trainerName;
    private String trainingType;

}
