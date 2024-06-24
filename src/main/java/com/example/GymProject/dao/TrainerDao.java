package com.example.GymProject.dao;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
            logger.info("Updating trainee with username: {}", trainer.getUser().getUsername());
            Session session = sessionFactory.getCurrentSession();
            Trainer existingTrainer = (Trainer) session
                    .createQuery("select t from Trainer t where t.user.username = :username")
                    .setParameter("username", trainer.getUser().getUsername())
                    .uniqueResult();
            if (existingTrainer != null) {
                User existingUser = existingTrainer.getUser();
                existingUser.setLastName(trainer.getUser().getLastName());
                existingUser.setFirstName(trainer.getUser().getFirstName());
                existingUser.setIsActive(trainer.getUser().getIsActive());
                // Update other fields of the User entity if needed

                existingTrainer.setUser(existingUser);
                existingTrainer.setSpecialization(trainer.getSpecialization());
                existingTrainer.setTrainees(trainer.getTrainees());
                session.update(existingTrainer);
                logger.info("Successfully updated trainee with username: {}", trainer.getUser().getUsername());
                return existingTrainer;
            } else {
                logger.error("Trainee with username: {} does not exist", trainer.getUser().getUsername());
                throw new EntityNotFoundException("Trainee not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating trainee with username: {}", trainer.getUser().getUsername(), e);
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
                            "OR t.trainingDate BETWEEN :fromDate AND :toDate " +
                            "OR t.trainee.user.username = :traineeName", Training.class)
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
