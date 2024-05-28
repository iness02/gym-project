package com.example.GymProject.model;

import java.util.Objects;

public class Trainer extends User {
    private TrainingType specialization;
    private String userId;

    public Trainer() {
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
        final StringBuilder sb = new StringBuilder("Trainer{");
        sb.append("specialization=").append(specialization);
        sb.append(", userId='").append(userId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return specialization == trainer.specialization && Objects.equals(userId, trainer.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialization, userId);
    }
}