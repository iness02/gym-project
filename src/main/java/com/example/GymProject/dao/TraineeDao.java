package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
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
public class TraineeDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    public Trainee createTrainee(Trainee trainee) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Creating trainee with username: {}", trainee.getUser().getUsername());
            session.persist(trainee);
            transaction.commit();
            logger.info("Successfully created trainee with username: {}", trainee.getUser().getUsername());
            return trainee;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while creating trainee with username: {}", trainee.getUser().getUsername());
            }
            logger.error("Error occurred while creating trainee with username: {}", trainee.getUser().getUsername(), e);
            throw e;
        }
    }

    public Trainee getTraineeByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Fetching trainee with username: {}", username);
            Trainee trainee = session.createQuery("FROM Trainee WHERE user.username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (trainee != null) {
                logger.info("Successfully fetched trainee with username: {}", username);
            } else {
                logger.warn("No trainee found with username: {}", username);
            }
            return trainee;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainee with username: {}", username, e);
            throw e;
        }
    }


    public Trainee updateTrainee(Trainee trainee) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Updating trainee with username: {}", trainee.getUser().getUsername());
            session.merge(trainee);
            transaction.commit();
            logger.info("Successfully updated trainee with username: {}", trainee.getUser().getUsername());
            return trainee;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while updating trainee with username: {}", trainee.getUser().getUsername());
            }
            logger.error("Error occurred while updating trainee with username: {}", trainee.getUser().getUsername(), e);
            throw e;
        }
    }

    public void deleteTraineeByUsername(String username) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Deleting trainee and their trainings with username: {}", username);

            Trainee trainee = getTraineeByUsername(username);
            if (trainee != null) {
                List<Training> trainings = session.createQuery("FROM Training WHERE trainee.user.username = :username", Training.class)
                        .setParameter("username", username)
                        .list();

                for (Training training : trainings) {
                    session.remove(training);
                }

                session.remove(trainee);
                transaction.commit();
                logger.info("Successfully deleted trainee and their trainings with username: {}", username);
            } else {
                logger.warn("No trainee found with username: {}", username);
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while deleting trainee with username: {}", username);
            }
            logger.error("Error occurred while deleting trainee with username: {}", username, e);
        }
    }

    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
                            "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                            "AND t.trainer.user.username = :trainerName" +
                            "AND t.trainingType.trainingTypeName = :trainingType", Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .getResultList();
        }
    }

}

