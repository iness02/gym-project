package com.example.trainer_workload.repository;

import com.example.trainer_workload.entity.TrainingWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingWorkRepository extends JpaRepository<TrainingWork, Long> {
    Optional<TrainingWork> findByUsername(String username);
}