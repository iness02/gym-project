package com.example.GymProject.repository;

import com.example.GymProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);


    @Query("SELECT MAX(u.id) FROM User u")
    Long findMaxId();
}
