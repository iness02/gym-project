package com.example.GymProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private Long id;
    private Date dateOfBirth;
    private String address;
    private UserDto userDto;
    private Set<TrainerDto> trainers;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TraineeDto{");
        sb.append("id=").append(id);
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", address='").append(address).append('\'');
        sb.append(", userDto=").append(userDto);
        sb.append(", trainers=").append(trainers);
        sb.append('}');
        return sb.toString();
    }
}