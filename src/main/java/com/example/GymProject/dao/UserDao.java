package com.example.GymProject.dao;

import com.example.GymProject.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Repository
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;


    @Transactional
    public void createUser(User user) {
        try {
            logger.info("Creating user with username: {}", user.getUsername());
            sessionFactory.getCurrentSession().persist(user);
            logger.info("Successfully created user with username: {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Error occurred while creating user with username: {}", user.getUsername(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        try {
            logger.info("Fetching user with username: {}", username);
            User user = sessionFactory.getCurrentSession()
                    .createQuery("select u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            if (user != null) {
                logger.info("Successfully fetched user with username: {}", username);
            } else {
                logger.warn("No user found with username: {}", username);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error occurred while fetching user with username: {}", username, e);
            throw e;
        }
    }

    @Transactional
    public User updateUser(User user) {
        try {
            logger.info("Updating user with username: {}", user.getUsername());
            Session session = sessionFactory.getCurrentSession();
            User existingUser = (User) session.createQuery("select u from User u where u.username = :username")
                    .setParameter("username", user.getUsername())
                    .uniqueResult();

            if (existingUser != null) {
                existingUser.setIsActive(user.getIsActive());
                existingUser.setUsername(user.getUsername());
                existingUser.setPassword(user.getPassword());
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                session.update(existingUser);
                logger.info("Successfully updated user with username: {}", user.getUsername());
                return existingUser;
            } else {
                logger.error("User with username: {} does not exist", user.getUsername());
                throw new EntityNotFoundException("User not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating user with username: {}", user.getUsername(), e);
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public boolean existsByUserName(String username) {
        try {
            logger.info("Checking if user with username {} exists", username);
            Long count = sessionFactory.getCurrentSession()
                    .createQuery("select count(u) from User u where u.username = :username", Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            boolean exists = count != null && count > 0;
            logger.info("User with username {} exists: {}", username, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error occurred while checking if user with username {} exists", username, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Long findMaxUserId() {
        try {
            logger.info("Finding maximum user id");
            Long maxUserId = sessionFactory.getCurrentSession()
                    .createQuery("select max(u.id) from User u", Long.class)
                    .uniqueResult();
            logger.info("Maximum user id found: {}", maxUserId);
            return maxUserId != null ? maxUserId : 0;
        } catch (Exception e) {
            logger.error("Error occurred while finding maximum user id", e);
            throw e;
        }
    }

}