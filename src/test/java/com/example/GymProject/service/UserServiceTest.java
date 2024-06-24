package com.example.GymProject.service;

import com.example.GymProject.config.TestConfig;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testMatchUsernameAndPassword() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");

        when(userDao.findUserByUsername("test")).thenReturn(user);

        assertTrue(userService.checkUsernameAndPassword("test", "password"));
        assertFalse(userService.checkUsernameAndPassword("test", "wrongpassword"));

        verify(userDao, times(2)).findUserByUsername("test");
    }

    @Test
    void testGetUserByUsername() {
        User user = new User();
        user.setUsername("test");

        when(userDao.findUserByUsername("test")).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        assertNotNull(userService.getUserByUsername("test"));

        verify(userDao).findUserByUsername("test");
        verify(entityMapper).toUserDto(user);
    }


    @Test
    void testGenerateUniqueUserName_WhenUserNameExists() {
        String firstName = "test";
        String lastName = "user";
        String baseUserName = firstName + "." + lastName;

        when(userDao.existsByUserName(baseUserName)).thenReturn(true);
        when(userDao.findMaxUserId()).thenReturn(1L);

        assertEquals("test.user2", userService.generateUniqueUserName(firstName, lastName));

        verify(userDao).existsByUserName(baseUserName);
        verify(userDao).findMaxUserId();
    }

    @Test
    void testGenerateUniqueUserName_WhenUserNameDoesNotExist() {
        String firstName = "firstname";
        String lastName = "lastname";
        String baseUserName = firstName + "." + lastName;

        when(userDao.existsByUserName(baseUserName)).thenReturn(false);

        assertEquals("firstname.lastname", userService.generateUniqueUserName(firstName, lastName));

        verify(userDao).existsByUserName(baseUserName);
    }


    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("test");

        User user = new User();
        user.setUsername("test");

        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(userDao.updateUser(user)).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(userDto);

        assertNotNull(userService.updateUser(userDto));

        verify(entityMapper).toUser(userDto);
        verify(userDao).updateUser(user);
        verify(entityMapper).toUserDto(user);
    }

    @Test
    public void testGetNextAvailableUserId() {
        long expectedMaxUserId = 100L;

        when(userDao.findMaxUserId()).thenReturn(expectedMaxUserId);

        long result = userService.getNextAvailableUserId();

        assertEquals(expectedMaxUserId, result);
        verify(userDao, times(1)).findMaxUserId();
    }

    @Test
    public void testChangePassword() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userDao.findUserByUsername(anyString())).thenReturn(user);
        when(entityMapper.toUserDto(any())).thenReturn(new UserDto("username", "password"));
        assertTrue(userService.changePassword("username", "newpassword", "password"));
    }

    @Test
    public void testChangePasswordUserNotFound() {
        when(userDao.findUserByUsername(anyString())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> userService.changePassword("username", "newpassword", "password"));
    }
}
