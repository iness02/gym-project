package com.example.GymProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "trainer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Trainee> trainees;
}
