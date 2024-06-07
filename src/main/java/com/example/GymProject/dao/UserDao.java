package com.example.GymProject.dao;

import com.example.GymProject.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private SessionFactory sessionFactory;

    /*public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Creating user with username: {}", user.getUsername());
            session.persist(user);
            transaction.commit();
            logger.info("Successfully created user with username: {}", user.getUsername());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while creating user with username: {}", user.getUsername());
            }
            logger.error("Error occurred while creating user with username: {}", user.getUsername(), e);
        }
    }*/

    public User findUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Fetching user with username: {}", username);
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
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

    public User updateUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            logger.info("Updating user with username: {}", user.getUsername());
            session.merge(user);
            transaction.commit();
            logger.info("Successfully updated user with username: {}", user.getUsername());
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                logger.warn("Transaction rolled back while updating user with username: {}", user.getUsername());
            }
            logger.error("Error occurred while updating user with username: {}", user.getUsername(), e);
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