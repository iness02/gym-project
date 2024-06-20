package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class TraineeDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        try {
            System.out.println(trainee);
            logger.info("Creating trainee with username: {}", trainee.getUser().getUsername());
            sessionFactory.getCurrentSession().persist(trainee);
            logger.info("Successfully created trainee with username: {}", trainee.getUser().getUsername());
            return trainee;
        } catch (Exception e) {
            logger.error("Error occurred while creating trainee with username: {}", trainee.getUser().getUsername(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        try {
            logger.info("Fetching trainee with username: {}", username);
            Trainee trainee = sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
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

    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        try {
            logger.info("Updating trainee with username: {}", trainee.getUser().getUsername());
            Session session = sessionFactory.getCurrentSession();
            Trainee existingTrainee = (Trainee) sessionFactory.getCurrentSession()
                    .createQuery("select t from Trainee t  where t.user.username = :username")
                    .setParameter("username", trainee.getUser().getUsername())
                    .uniqueResult();
            if (existingTrainee != null) {
                existingTrainee.setUser(trainee.getUser());
                existingTrainee.setAddress(trainee.getAddress());
                existingTrainee.setDateOfBirth(trainee.getDateOfBirth());
                existingTrainee.setTrainers(trainee.getTrainers());
                session.update(existingTrainee);
             /* sessionFactory.getCurrentSession().createQuery("update Trainee set user =:user, address = :address, dateOfBirth =:dateOfBirth, trainers = :trainers where user.username =:username" )
                      .setParameter("user",existingTrainee.getUser()).
                      setParameter("address",existingTrainee.getAddress()).
                      setParameter("dateOfBirth",existingTrainee.getDateOfBirth()).
                      setParameter("trainers",existingTrainee.getTrainers()).
                      setParameter("username",existingTrainee.getUser().getUsername()).executeUpdate();*/
                // sessionFactory.getCurrentSession().merge(existingTrainee);
            } else {
                logger.error("Trainee with username: {} does not exist", trainee.getUser().getUsername());
                throw new EntityNotFoundException("Trainee not found");
            }
            logger.info("Successfully updated trainee with username: {}", trainee.getUser().getUsername());
            return existingTrainee;
        } catch (Exception e) {
            logger.error("Error occurred while updating trainee with username: {}", trainee.getUser().getUsername(), e);
            throw e;
        }
    }


    @Transactional
    public void deleteTraineeByUsername(String username) {
        try {
            logger.info("Deleting trainee and their trainings with username: {}", username);

            Trainee trainee = getTraineeByUsername(username);
            User user = trainee.getUser();
            if (trainee != null && user != null) {
                List<Training> trainings = sessionFactory.getCurrentSession()
                        .createQuery("Select t FROM Training t WHERE t.trainee.user.username = :username", Training.class)
                        .setParameter("username", username)
                        .list();

                for (Training training : trainings) {
                    sessionFactory.getCurrentSession().remove(training);
                }
                sessionFactory.getCurrentSession().remove(trainee);
                sessionFactory.getCurrentSession().remove(user);
                logger.info("Successfully deleted trainee and their trainings with username: {}", username);
            } else {
                logger.warn("No trainee found with username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting trainee with username: {}", username, e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        try {
            logger.info("Fetching trainings for trainee with username: {}", username);
            List<Training> trainings = sessionFactory.getCurrentSession()
                    .createQuery("SELECT t FROM Training t WHERE t.trainee.user.username = :username " +
                            "AND t.trainingDate BETWEEN :fromDate AND :toDate " +
                            "AND t.trainer.user.username = :trainerName " +
                            "AND t.trainingType.trainingTypeName = :trainingType", Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .getResultList();
            logger.info("Successfully fetched {} trainings for trainee with username: {}", trainings.size(), username);
            return trainings;
        } catch (Exception e) {
            logger.error("Error occurred while fetching trainings for trainee with username: {}", username, e);
            throw e;
        }
    }
}

