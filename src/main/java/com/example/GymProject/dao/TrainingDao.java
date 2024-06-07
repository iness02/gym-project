package com.example.GymProject.dao;

import com.example.GymProject.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    public Training addTraining(Training training) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Adding new training with ID: {}", training.getId());
            session.persist(training);
            transaction.commit();
            logger.info("Successfully added new training with ID: {}", training.getId());
            return training;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while adding new training with ID: {}", training.getId());
            }
            logger.error("Error occurred while adding new training with ID: {}", training.getId(), e);
            throw e;
        }
    }

    public List<Training> getAllTrainings() {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Fetching all trainings");
            List<Training> trainings = session.createQuery("FROM Training", Training.class).list();
            logger.info("Successfully fetched {} trainings", trainings.size());
            return trainings;
        } catch (Exception e) {
            logger.error("Error occurred while fetching all trainings", e);
            throw e;
        }
    }

    public Training updateTraining(Training training) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Updating training with name: {}", training.getTrainingName());
            session.merge(training);
            transaction.commit();
            logger.info("Successfully updated training with name: {}", training.getTrainingName());
            return training;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while updating training with name: {}", training.getTrainingName());
            }
            logger.error("Error occurred while updating training with name: {}", training.getTrainingName(), e);
            throw e;
        }
    }
}