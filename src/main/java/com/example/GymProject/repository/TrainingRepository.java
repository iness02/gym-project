package com.example.GymProject.repository;


import com.example.GymProject.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
            "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
            "AND t.trainer.user.username = :trainerName " +
            "AND t.trainingType.trainingTypeName = :trainingType")
    List<Training> findTraineeTrainings(@Param("username") String username,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("trainerName") String trainerName,
                                        @Param("trainingType") String trainingType);

    @Query("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
            "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
            "AND t.trainee.user.username = :traineeName")
    List<Training> findTrainerTrainings(@Param("username") String username,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("traineeName") String traineeName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Training t WHERE t.id IN :trainingIds")
    void removeTrainings(@Param("trainingIds") List<Long> trainingIds);
}
