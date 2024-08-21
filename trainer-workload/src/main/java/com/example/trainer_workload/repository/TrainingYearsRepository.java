package com.example.trainer_workload.repository;

import com.example.trainer_workload.entity.TrainingYears;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingYearsRepository extends JpaRepository<TrainingYears, Long> {
}