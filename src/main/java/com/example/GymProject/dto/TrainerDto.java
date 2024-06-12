package com.example.GymProject.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {
    private Long id;
    private String specialization;
    private UserDto userDto;
}