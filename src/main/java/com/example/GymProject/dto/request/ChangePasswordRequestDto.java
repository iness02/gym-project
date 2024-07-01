package com.example.GymProject.dto.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequestDto {
    @Nonnull
    private String username;
    @Nonnull
    private String oldPassword;
    @Nonnull
    private String newPassword;
}