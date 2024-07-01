package com.example.GymProject.repository;

import com.example.GymProject.model.TrainingType;
import com.example.GymProject.model.Trainings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    TrainingType findTrainingTypeByTrainingTypeName(Trainings name);

}
