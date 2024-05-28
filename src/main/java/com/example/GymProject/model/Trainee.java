package com.example.GymProject.model;

import java.time.LocalDate;
import java.util.Objects;


public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private String userId;

    public Trainee() {
    }
    public Trainee(String firstName, String lastName, Boolean isActive,
                   LocalDate dob, String address){
        super(firstName, lastName, isActive);
        this.dateOfBirth = dob;
        this.address = address;
    }
    public Trainee(String firstName, String lastName, Boolean isActive,
                   LocalDate dob, String address, String userId){
        super(firstName, lastName, isActive);
        this.dateOfBirth = dob;
        this.address = address;
        this.userId = userId;
    }
    public Trainee(String firstName, String lastName, String username, String password,
                   Boolean isActive, LocalDate dob, String address) {
        super(firstName, lastName, username, password, isActive);
        this.dateOfBirth = dob;
        this.address = address;
    }

    public Trainee(String firstName, String lastName, String username, String password,
                   Boolean isActive, LocalDate dob, String address, String userId) {
        super(firstName, lastName, username, password, isActive);
        this.dateOfBirth = dob;
        this.address = address;
        this.userId = userId;
    }

    public LocalDate getDob() {
        return dateOfBirth;
    }

    public void setDob(LocalDate dob) {
        this.dateOfBirth = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address) && Objects.equals(userId, trainee.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfBirth, address, userId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trainee{");
        sb.append("dateOfBirth=").append(dateOfBirth);
        sb.append(", address='").append(address).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
