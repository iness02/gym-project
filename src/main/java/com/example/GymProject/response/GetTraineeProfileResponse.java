package com.example.GymProject.response;

import java.time.LocalDate;
import java.util.Set;


public class GetTraineeProfileResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private Set<TrainerForTraineeResponse> trainers;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GetTraineeProfileResponse{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", address='").append(address).append('\'');
        sb.append(", isActive=").append(isActive);
        sb.append(", trainers=").append(trainers);
        sb.append('}');
        return sb.toString();
    }

    public GetTraineeProfileResponse(String firstName, String lastName, LocalDate dateOfBirth, String address, Boolean isActive, Set<TrainerForTraineeResponse> trainers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
        this.trainers = trainers;
    }

    public GetTraineeProfileResponse() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Set<TrainerForTraineeResponse> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<TrainerForTraineeResponse> trainers) {
        this.trainers = trainers;
    }
}
