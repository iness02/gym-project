package com.example.GymProject.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetTrainingResponse {
    private String name;
    private Date date;
    private String type;
    private Integer duration;
    private String trainerName;
}
