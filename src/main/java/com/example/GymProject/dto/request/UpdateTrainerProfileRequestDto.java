package com.example.GymProject.dto.request;


import jakarta.annotation.Nonnull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileRequestDto {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    @Nonnull
    private String specialization;
    @Nonnull
    private Boolean isActive;
}