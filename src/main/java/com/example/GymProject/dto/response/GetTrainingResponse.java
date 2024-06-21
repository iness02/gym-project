package com.example.GymProject.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainingResponse {
    private String name;
    private LocalDate date;
    private String type;
    private Integer duration;
    private String trainerName;
}
