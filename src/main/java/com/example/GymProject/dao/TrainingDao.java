package com.example.GymProject.dao;

import com.example.GymProject.model.Training;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TrainingDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Training addTraining(Training training) {
        try {
            logger.info("Adding new training with ID: {}", training.getId());
            sessionFactory.getCurrentSession().persist(training);
            logger.info("Successfully added new training with ID: {}", training.getId());
            return training;
        } catch (Exception e) {
            logger.error("Error occurred while adding new training with ID: {}", training.getId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        try {
            logger.info("Fetching all trainings");
            List<Training> trainings = sessionFactory.getCurrentSession()
                    .createQuery("Select t FROM Training t", Training.class)
                    .list();
            logger.info("Successfully fetched {} trainings", trainings.size());
            return trainings;
        } catch (Exception e) {
            logger.error("Error occurred while fetching all trainings", e);
            throw e;
        }
    }

    @Transactional
    public Training updateTraining(Training training) {
        try {
            logger.info("Updating training with name: {}", training.getTrainingName());
            sessionFactory.getCurrentSession().merge(training);
            logger.info("Successfully updated training with name: {}", training.getTrainingName());
            return training;
        } catch (Exception e) {
            logger.error("Error occurred while updating training with name: {}", training.getTrainingName(), e);
            throw e;
        }
    }

    @Transactional
    public void removeTrainings(List<Long> trainingIds) {
        try {
            logger.info("Removing trainings with IDs: {}", trainingIds);
            sessionFactory.getCurrentSession()
                    .createQuery("DELETE FROM Training t WHERE t.id IN :trainingIds")
                    .setParameter("trainingIds", trainingIds)
                    .executeUpdate();
            logger.info("Successfully removed trainings with IDs: {}", trainingIds);
        } catch (Exception e) {
            logger.error("Error occurred while removing trainings with IDs: {}", trainingIds, e);
            throw e;
        }
    }
}