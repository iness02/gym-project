package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeProfileRequestDto {
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    @Nonnull
    private Boolean isActive;
}