package com.example.GymProject.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
