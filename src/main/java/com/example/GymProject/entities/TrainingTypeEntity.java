package com.example.GymProject.entities;

import com.example.GymProject.model.TrainingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainingTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "training_type_name", nullable = false)
    private TrainingType trainingTypeName;
}
