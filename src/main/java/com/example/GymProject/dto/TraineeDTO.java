package com.example.GymProject.dto;

import lombok.*;

import java.util.Date;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
public class TraineeDTO {
    private Long id;
    private Date dateOfBirth;
    private String address;
    private UserDTO user;
    private Set<TrainerDTO> trainers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<TrainerDTO> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<TrainerDTO> trainers) {
        this.trainers = trainers;
    }
}
