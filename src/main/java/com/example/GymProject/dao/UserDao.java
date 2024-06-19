package com.example.GymProject.dao;

import com.example.GymProject.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataSource dataSource;

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

      /*  @Transactional
        public User updateUser(User user) {
            try {
                logger.info("Updating user with username: {}", user.getUsername());
                sessionFactory.getCurrentSession().merge(user);
               *//* sessionFactory.getCurrentSession().createQuery("UPDATE  User u  SET u.firstName=:firstName, u.lastName =:lastName, u.isActive=:isActive, u.password=:password WHERE u.username = :username")
                        .setParameter("firstName", user.getFirstName())
                        .setParameter("lastName", user.getLastName())
                        .setParameter("isActive",user.getIsActive())
                        .setParameter("password",user.getPassword())
                        .setParameter("username",user.getUsername()).executeUpdate();*//*
                logger.info("Successfully updated user with username: {}", user.getUsername());
                return user;
            } catch (Exception e) {
                logger.error("Error occurred while updating user with username: {}", user.getUsername(), e);
                throw e;
            }
        }*/

    @Transactional
    public User updateUser(User user) {
        try {
            logger.info("Updating user with username: {}", user.getUsername());
            // Fetch the existing user by username
            User existingUser = (User) sessionFactory.getCurrentSession()
                    .createQuery("select u from User u where u.username = :username")
                    .setParameter("username", user.getUsername())
                    .uniqueResult();

            if (existingUser != null) {
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setIsActive(user.getIsActive());
                existingUser.setPassword(user.getPassword());
                sessionFactory.getCurrentSession().merge(existingUser);
            } else {
                logger.error("User with username: {} does not exist", user.getUsername());
                throw new EntityNotFoundException("User not found");
            }
            logger.info("Successfully updated user with username: {}", user.getUsername());
            return existingUser;
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
   /* public void deleteUserByUsername(String username) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Deleting user with username: {}", username);
            User user = findUserByUsername(username);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
            logger.info("Successfully deleted user with username: {}", username);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while deleting user with username: {}", username);
            }
            logger.error("Error occurred while deleting user with username: {}", username, e);
        }
    }*/
}