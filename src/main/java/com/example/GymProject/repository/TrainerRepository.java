package com.example.GymProject.repository;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Trainer getTrainerByUserUsername(String username);

    @Transactional
    void deleteByUserUsername(String username);

    boolean existsByUserUsername(String username);

    Trainer findByUser(User user);
}
