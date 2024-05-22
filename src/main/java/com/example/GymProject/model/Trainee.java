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
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Trainee trainee = (Trainee) object;
        return Objects.equals(userId, trainee.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}
