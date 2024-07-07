package com.example.GymProject.service;


import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import com.example.GymProject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserByUsername() {
        User user = new User();
        user.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(new UserDto());

        assertNotNull(userService.getUserByUsername("test"));

        verify(userRepository).findByUsername("test");
        verify(entityMapper).toUserDto(user);
    }


    @Test
    void testGenerateUniqueUserName_UserNameDoesNotExist() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUserName = "John.Doe";

        when(userRepository.existsByUsername(expectedUserName)).thenReturn(false);

        String result = userService.generateUniqueUserName(firstName, lastName);

        assertEquals(expectedUserName, result);
    }

    @Test
    void testGenerateUniqueUserName_UserNameExists() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUserName = "John.Doe";
        String existingUserName = "John.Doe1";
        String expectedUserName = "John.Doe2";

        when(userRepository.existsByUsername(baseUserName)).thenReturn(true);
        when(userRepository.existsByUsername(existingUserName)).thenReturn(true);
        when(userRepository.findMaxId()).thenReturn(1L);

        String result = userService.generateUniqueUserName(firstName, lastName);

        assertEquals(expectedUserName, result);
    }

    @Test
    void testGetNextAvailableUserId() {
        long maxId = 10L;

        when(userRepository.findMaxId()).thenReturn(maxId);

        long result = userService.getNextAvailableUserId();

        assertEquals(maxId, result);
    }

    @Test
    void testUpdateUser() {
        String username = "testUser";
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        User user = new User();
        user.setUsername(username);

        when(entityMapper.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(entityMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.updateUser(userDto);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testChangePassword_UserFound() {
        String username = "testUser";
        String currentPassword = "password";
        String newPassword = "newPassword";
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(currentPassword);

        when(userService.getUserByUsername(username)).thenReturn(userDto);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        boolean result = userService.changePassword(username, newPassword, currentPassword);

        assertTrue(result);
    }

    @Test
    void testChangePassword_UserNotFound() {
        String username = "nonExistingUser";
        String currentPassword = "password";
        String newPassword = "newPassword";

        when(userService.getUserByUsername(username)).thenThrow(new ResourceNotFoundException("User not found with username: " + username));

        assertThrows(ResourceNotFoundException.class, () -> userService.changePassword(username, newPassword, currentPassword));
    }

}
