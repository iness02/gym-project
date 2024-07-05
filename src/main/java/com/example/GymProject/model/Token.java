package com.example.GymProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_id_gen")
    @SequenceGenerator(name = "tokens_id_gen", sequenceName = "tokens_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", length = Integer.MAX_VALUE)
    private String token;

    @Column(name = "username")
    private String username;

    @Column(name = "is_valid")
    private Boolean isValid;

}