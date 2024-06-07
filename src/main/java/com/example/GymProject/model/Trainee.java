package com.example.GymProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainee")
@AllArgsConstructor
@NoArgsConstructor
public class Trainee{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "trainee",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private Set<Trainer> trainers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public User getUser() {
        return user;
    }

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }
}
