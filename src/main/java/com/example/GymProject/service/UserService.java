
package com.example.GymProject.service;

import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EntityMapper entityMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public boolean checkUsernameAndPassword(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        User user = userDao.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            logger.warn("User with username {} not found or password is wrong", username);
            return false;
        }

        logger.info("Username and password are right for user {}", username);

        return true;
    }

    public UserDto getUserByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        User user = userDao.findUserByUsername(username);
        return entityMapper.toUserDto(user);
    }

    public String generateUniqueUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        boolean userNameExist = userDao.existsByUserName(baseUserName);
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
        Assert.notNull(userDto, "UserDto cannot be null");
        User user = entityMapper.toUser(userDto);
        return entityMapper.toUserDto(userDao.updateUser(user));
    }

    public boolean changePassword(String username, String newPassword, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Assert.notNull(newPassword, "New password cannot be null");
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

}
