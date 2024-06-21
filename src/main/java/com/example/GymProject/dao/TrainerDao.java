package com.example.GymProject.dao;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class TrainerDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        try {
            logger.info("Creating trainer with username: {}", trainer.getUser().getUsername());
            sessionFactory.getCurrentSession().persist(trainer);
            logger.info("Successfully created trainer with username: {}", trainer.getUser().getUsername());
            return trainer;
        } catch (Exception e) {
            logger.error("Error occurred while creating trainer with username: {}", trainer.getUser().getUsername(), e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username) {
        System.out.println(username);
        try {
            logger.info("Fetching trainer with username: {}", username);
            Trainer trainer = sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainer != null) {
                Hibernate.initialize(trainer.getTrainees()); // Ensure trainees are fully initialized
                logger.info("Successfully fetched trainer with username: {}", username);
            } else {
                logger.warn("No trainer found with username: {}", username);
            }
            return trainer;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainer with username: {}", username, e);
            throw e;
        }
    }


    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        try {
            logger.info("Updating trainer with username: {}", trainer.getUser().getUsername());
            sessionFactory.getCurrentSession().merge(trainer);
            return trainer;
        } catch (Exception e) {
            logger.error("Error occurred while updating trainer with username: {}", trainer.getUser().getUsername(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();
    }

    @Transactional
    public void deleteTrainerByUsername(String username) {
        try {
            logger.info("Deleting trainer with username: {}", username);
            Trainer trainer = getTrainerByUsername(username);
            User user = trainer.getUser();
            if (trainer != null && user != null) {
                sessionFactory.getCurrentSession().remove(trainer);
                sessionFactory.getCurrentSession().remove(user);
                logger.info("Successfully deleted trainer with username: {}", username);
            } else {
                logger.warn("No trainer found with username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting trainer with username: {}", username, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        try {
            logger.info("Fetching trainings for trainer with username: {}", username);
            List<Training> trainings = sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
                            "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                            "AND t.trainee.user.username = :traineeName", Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .getResultList();
            logger.info("Successfully fetched {} trainings for trainer with username: {}", trainings.size(), username);
            return trainings;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainings for trainer with username: {}", username, e);
            throw e;
        }
    }
    public boolean existsByUsername(String username) {
        String hql = "SELECT COUNT(t) FROM Trainer t WHERE t.user.username = :username";
        Long count = (Long) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("username", username)
                .uniqueResult();
        return count > 0;
    }
}
