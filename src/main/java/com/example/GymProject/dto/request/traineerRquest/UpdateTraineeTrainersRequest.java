package com.example.GymProject.dto.request.traineerRquest;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Set<String> trainerUsernames;
}
