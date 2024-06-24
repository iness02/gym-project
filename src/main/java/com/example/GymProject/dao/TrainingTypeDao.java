package com.example.GymProject.dao;

import com.example.GymProject.model.TrainingType;
import com.example.GymProject.model.Trainings;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TrainingTypeDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public TrainingType addTrainingType(TrainingType trainingType) {
        try {
            logger.info("Adding new trainingType with ID: {}", trainingType.getId());
            sessionFactory.getCurrentSession().persist(trainingType);
            logger.info("Successfully added new trainingType with ID: {}", trainingType.getId());
            return trainingType;
        } catch (Exception e) {
            logger.error("Error occurred while adding new trainingType with ID: {}", trainingType.getId(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public TrainingType findTrainingByName(Trainings name) {
        try {
            logger.info("Fetching training type with name: {}", name);
            TrainingType trainingType = sessionFactory.getCurrentSession()
                    .createQuery("select t FROM TrainingType t WHERE t.trainingTypeName = :name", TrainingType.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (trainingType != null) {
                logger.info("Successfully fetched trainingType with name: {}", name);
            } else {
                logger.warn("No trainingType found with name: {}", name);
            }
            return trainingType;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainingType with name: {}", name, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public TrainingType getTrainingTypeByName(String name) {
        try {
            logger.info("Fetching trainingType with username: {}", name);
            TrainingType trainingType = sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM TrainingType t WHERE t.trainingTypeName = :name", TrainingType.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (trainingType != null) {
                logger.info("Successfully fetched trainingType with name: {}", name);
            } else {
                logger.warn("No trainingType found with name: {}", name);
            }
            return trainingType;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainingType with name: {}", name, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<TrainingType> getAllTrainingTypes() {
        try {
            logger.info("Fetching all training types");
            List<TrainingType> trainingTypes = sessionFactory.getCurrentSession()
                    .createQuery("Select t FROM TrainingType t", TrainingType.class)
                    .list();
            logger.info("Successfully fetched {} training types", trainingTypes.size());
            return trainingTypes;
        } catch (Exception e) {
            logger.error("Error occurred while fetching all training types", e);
            throw e;
        }
    }
}
