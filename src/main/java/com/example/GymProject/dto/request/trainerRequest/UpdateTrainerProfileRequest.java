package com.example.GymProject.dto.request.trainerRequest;


import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @ReadOnlyProperty
    private String specialization;
    @NotEmpty
    private Boolean isActive;
}
