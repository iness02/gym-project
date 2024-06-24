package com.example.GymProject.dao;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
public class UserDaoTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<User> userQuery;

    @Mock
    private Query<Long> longQuery;

    @InjectMocks
    private UserDao userDao;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);

        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("User");
    }

    @Test
    void testCreateUser() {
        doNothing().when(session).persist(user);

        userDao.createUser(user);

        verify(session, times(1)).persist(user);
    }

    @Test
    void testFindUserByUsername() {
        when(session.createQuery("select u FROM User u WHERE u.username = :username", User.class))
                .thenReturn(userQuery);
        when(userQuery.setParameter("username", "testuser")).thenReturn(userQuery);
        when(userQuery.uniqueResult()).thenReturn(user);

        User result = userDao.findUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testUpdateUser() {
        when(session.createQuery("select u from User u where u.username = :username"))
                .thenReturn(userQuery);
        when(userQuery.setParameter("username", "testuser")).thenReturn(userQuery);
        when(userQuery.uniqueResult()).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setUsername("testuser");
        updatedUser.setPassword("newpassword");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setIsActive(true);

        User result = userDao.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("newpassword", result.getPassword());
        assertEquals("Updated", result.getFirstName());
        assertEquals("User", result.getLastName());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(session.createQuery("select u from User u where u.username = :username"))
                .thenReturn(userQuery);
        when(userQuery.setParameter("username", "nonexistent")).thenReturn(userQuery);
        when(userQuery.uniqueResult()).thenReturn(null);

        User updatedUser = new User();
        updatedUser.setUsername("nonexistent");

        assertThrows(EntityNotFoundException.class, () -> userDao.updateUser(updatedUser));
    }

    @Test
    void testExistsByUserName() {
        when(session.createQuery("select count(u) from User u where u.username = :username", Long.class))
                .thenReturn(longQuery);
        when(longQuery.setParameter("username", "testuser")).thenReturn(longQuery);
        when(longQuery.uniqueResult()).thenReturn(1L);

        boolean exists = userDao.existsByUserName("testuser");

        assertTrue(exists);
    }

    @Test
    void testFindMaxUserId() {
        when(session.createQuery("select max(u.id) from User u", Long.class))
                .thenReturn(longQuery);
        when(longQuery.uniqueResult()).thenReturn(10L);

        Long maxUserId = userDao.findMaxUserId();

        assertEquals(10L, maxUserId);
    }
}
