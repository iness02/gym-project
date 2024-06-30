package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequestDto {
    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
