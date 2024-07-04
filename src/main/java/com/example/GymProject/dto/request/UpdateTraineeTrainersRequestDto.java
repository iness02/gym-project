package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequestDto {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    @Nonnull
    private Set<String> trainerUsernames;
}