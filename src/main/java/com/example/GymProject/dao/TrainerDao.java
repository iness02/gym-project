package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class TrainerDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    public Trainer createTrainer(Trainer trainer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Creating trainer with username: {}", trainer.getUser().getUsername());
            session.persist(trainer);
            transaction.commit();
            logger.info("Successfully created trainer with username: {}", trainer.getUser().getUsername());
            return trainer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while creating trainer with username: {}", trainer.getUser().getUsername());
            }
            logger.error("Error occurred while creating trainer with username: {}", trainer.getUser().getUsername(), e);
            throw e;
        }
    }

    public Trainer getTrainerByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Fetching trainer with username: {}", username);
            Trainer trainer = session.createQuery("FROM Trainer WHERE user.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainer != null) {
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

    public Trainer updateTrainer(Trainer trainer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Updating trainer with username: {}", trainer.getUser().getUsername());
            session.merge(trainer);
            transaction.commit();
            logger.info("Successfully updated trainer with username: {}", trainer.getUser().getUsername());
            return trainer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while updating trainer with username: {}", trainer.getUser().getUsername());
            }
            logger.error("Error occurred while updating trainer with username: {}", trainer.getUser().getUsername(), e);
            throw e;
        }
    }

    public void deleteTrainerByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            logger.info("Deleting trainer with username: {}", username);

            Trainer trainer = getTrainerByUsername(username);
            if (trainer != null) {
                session.delete(trainer);
                session.getTransaction().commit();
                logger.info("Successfully deleted trainer with username: {}", username);
            } else {
                logger.warn("No trainer found with username: {}", username);
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting trainer with username: {}", username, e);
            throw e;
        }
    }

    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Fetching trainings for trainer with username: {}", username);
            List<Training> trainings = session.createQuery("SELECT t FROM Training t WHERE t.trainer.user.username = :username " +
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
}
