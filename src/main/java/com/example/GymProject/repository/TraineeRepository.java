package com.example.GymProject.repository;


import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Trainee getTraineeByUserUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Trainee t WHERE t.user.username = :username")
    void deleteByUserUsername(String username);

    boolean existsByUserUsername(String username);

    Trainee findByUser(User user);
}
