package com.example.GymProject.request.traineerRquest;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequest {
    private String username;
    private String password;
    @NotNull
    private Set<String> trainerUsernames;
}
