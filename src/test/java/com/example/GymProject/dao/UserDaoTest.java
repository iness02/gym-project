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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@EnableTransactionManagement
public class UserDaoTest {
    @InjectMocks
    private UserDao userDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<User> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");

        doNothing().when(session).persist(user);

        userDao.createUser(user);

        verify(session, times(1)).persist(user);
    }

    @Test
    public void testFindUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(session.createQuery("select u FROM User u WHERE u.username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User result = userDao.findUserByUsername(username);
        assertEquals(username, result.getUsername());
        verify(session, times(1)).createQuery("select u FROM User u WHERE u.username = :username", User.class);
    }

  @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testuser");

        when(session.merge(user)).thenReturn(user);

        User updatedUser = userDao.updateUser(user);
        assertEquals(user.getUsername(), updatedUser.getUsername());
        verify(session, times(1)).merge(user);
    }


    @Test
    public void testExistsByUserName() {
        String username = "testuser";
        Long count = 1L;

        Query<Long> mockQuery = mock(Query.class);

        when(session.createQuery("select count(u) from User u where u.username = :username", Long.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("username", username)).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(count);

        boolean exists = userDao.existsByUserName(username);
        assertTrue(exists);
        verify(session, times(1)).createQuery("select count(u) from User u where u.username = :username", Long.class);
    }


    @Test
    public void testFindMaxUserId() {
        Long maxUserId = 10L;

        Query<Long> mockQuery = mock(Query.class);

        when(session.createQuery("select max(u.id) from User u", Long.class)).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(maxUserId);

        Long result = userDao.findMaxUserId();
        assertEquals(maxUserId, result);
        verify(session, times(1)).createQuery("select max(u.id) from User u", Long.class);
    }

}
