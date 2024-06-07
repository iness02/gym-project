package com.example.GymProject.dao;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserDaoTest {
    @InjectMocks
    private UserDao userDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;
    @Mock
    private Query<User> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testFindUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(session.createQuery("FROM User WHERE username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User result = userDao.findUserByUsername(username);
        assertEquals(username, result.getUsername());
        verify(session, times(1)).close();
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testuser");

        User updatedUser = userDao.updateUser(user);
        assertEquals(user.getUsername(), updatedUser.getUsername());
        verify(session, times(1)).merge(user);
        verify(transaction, times(1)).commit();
    }
}
