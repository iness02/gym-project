package com.example.GymProject.repository;

import com.example.GymProject.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);

    Token findByUsername(String username);
}