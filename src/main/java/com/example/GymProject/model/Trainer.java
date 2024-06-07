package com.example.GymProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainer")
@AllArgsConstructor
@NoArgsConstructor
public class Trainer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setUser(User user) {
        this.user = user;
    }
}