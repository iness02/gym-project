package com.example.GymProject.repository;

import com.example.GymProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);


    @Query("SELECT MAX(u.id) FROM User u")
    Long findMaxId();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = ?1 WHERE u.username = ?2")
    void updateFailedAttempts(int failedAttempts, String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lockTime = ?1 WHERE u.username = ?2")
    void updateLockTime(LocalDateTime lockTime, String username);


}

