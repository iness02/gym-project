package com.example.GymProject.request.traineerRquest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequest {
    private String username;
    private String password;
    @NotNull
    private Set<String> trainerUsernames;
}
