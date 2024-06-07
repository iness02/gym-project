package com.example.GymProject.service;

import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserServiceTest {
    @Mock
    private UserDao userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
   public void testMatchUsernameAndPassword() {
        String username = "testUser";
        String password = "testPass";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userDAO.findUserByUsername(username)).thenReturn(user);

        boolean result = userService.matchUsernameAndPassword(username, password);
        assertTrue(result);

        verify(userDAO, times(1)).findUserByUsername(username);
    }

    @Test
    public void testGetUserByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);

        when(userDAO.findUserByUsername(username)).thenReturn(user);
        when(EntityMapper.INSTANCE.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByUsername(username);
        assertNotNull(result);
        assertEquals(username, result.getUsername());

        verify(userDAO, times(1)).findUserByUsername(username);
    }

    @Test
   public void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        User user = new User();
        user.setUsername("testUser");

        when(EntityMapper.INSTANCE.userDTOToUser(userDTO)).thenReturn(user);
        when(userDAO.updateUser(user)).thenReturn(user);
        when(EntityMapper.INSTANCE.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userDTO);
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());

        verify(userDAO, times(1)).updateUser(user);
    }
}
