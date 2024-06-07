package com.example.GymProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_type")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_type_name", nullable = false)
    private Trainings trainingTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trainings getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(Trainings trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
