package com.example.trainer_workload.repository;

import com.example.trainer_workload.entity.TrainingMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingMonthRepository extends JpaRepository<TrainingMonth, Long> {
}
