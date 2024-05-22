package com.example.GymProject.model;



import java.util.Objects;

public class Trainer extends User {
    private TrainingType specialization;
    private String userId;

    public Trainer() {
    }
    public Trainer(String firstName, String lastName, Boolean isActive,
                   TrainingType specialization) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName, String username, String password,
                   Boolean isActive, TrainingType specialization, String userId) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }
    public Trainer(String firstName, String lastName, String username, String password,
                   Boolean isActive, TrainingType specialization) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName, Boolean isActive,
                   TrainingType specialization, String userId) {
        super(firstName, lastName, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization +
                ", userId='" + userId + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Trainer trainer = (Trainer) object;
        return Objects.equals(userId, trainer.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}