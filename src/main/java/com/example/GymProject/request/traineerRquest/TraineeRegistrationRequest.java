package com.example.GymProject.request.traineerRquest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.SerializableString;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequest {
    @JsonProperty("firstName")
    @NotBlank
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank
    private String lastName;

    @JsonProperty("dateOfBirth")
    private LocalDate dateOfBirth;
    @JsonProperty("address")
    private String address;
}
