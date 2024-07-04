package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationRequestDto {

    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    @Nonnull
    private String specialization;
}