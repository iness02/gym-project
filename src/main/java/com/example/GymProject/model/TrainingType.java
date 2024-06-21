package com.example.GymProject.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "training_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_type_name", nullable = false)
    private Trainings trainingTypeName;
}