
package com.example.GymProject.service;

import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.InvalidCredentialsException;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EntityMapper entityMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public boolean matchUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        User user = userDao.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            logger.warn("User with username {} not found or password is wrong", username);
            throw new InvalidCredentialsException("Username or password is incorrect");
        }

        logger.info("Username and password are right for user {}", username);

        return true;
    }

    public UserDto getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidCredentialsException("Username is invalid");
        }
        User user = userDao.findUserByUsername(username);
        return entityMapper.toUserDto(user);
    }

    public String generateUniqueUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        Boolean userNameExist = userDao.existsByUserName(baseUserName);
        if (userNameExist) {
            long nextUserId = getNextAvailableUserId() + 1L;
            return baseUserName + nextUserId;
        } else {
            return baseUserName;
        }
    }

    public long getNextAvailableUserId() {
        return userDao.findMaxUserId();
    }

    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new InvalidCredentialsException("UserDto cannot be null");
        }

        User user = entityMapper.toUser(userDto);
        return entityMapper.toUserDto(userDao.updateUser(user));
    }

    public boolean changePassword(String username, String newPassword, String password) {
        if (username == null || password == null || newPassword == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            UserDto userDto = getUserByUsername(username);
            if (userDto == null) {
                throw new ResourceNotFoundException("User not found with username: " + username);
            }
            if (!password.equals(newPassword)) {
                userDto.setPassword(newPassword);
                updateUser(userDto);
                logger.info("Password has successfully changed for trainee {}", username);
            } else {
                logger.warn("Cannot change password for user {} since new password is equal to old password", username);

            }
            return true;
        }
        logger.error("Authentication failed for trainee {}", username);
        return false;
    }

    public boolean isAuthenticated(String username, String password) {
        return matchUsernameAndPassword(username, password);
    }
}
