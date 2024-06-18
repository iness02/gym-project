
package com.example.GymProject.service;

import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDto;
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

    public boolean matchUsernameAndPassword(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        User user = userDao.findUserByUsername(username);
        boolean isMatched = user != null && user.getPassword().equals(password);
        if (isMatched) {
            logger.info("Username and password are right for user {}", username);
        } else {
            logger.warn("User with username {} not found or password is wrong", username);
        }
        return isMatched;
    }

    public UserDto getUserByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        User user = userDao.findUserByUsername(username);
        return entityMapper.toUserDto(user);
    }

    public String generateUniqueUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        Boolean userNameExist = userDao.existsByUserName(baseUserName);
        if (userNameExist) {
            long nextUserId = getNextAvailableUserId() + 1L;
            return baseUserName +nextUserId;
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
        Assert.notNull(newPassword, "New password cannot be null");
        if (isAuthenticated(username, password)) {
            UserDto userDto = getUserByUsername(username);
            if (userDto != null) {
                if (!password.equals(newPassword)) {
                    userDto.setPassword(newPassword);
                    updateUser(userDto);
                    logger.info("Password has successfully changed for trainee {}", username);
                } else {
                    logger.warn("Cannot change password for user {} since new password is equal to old password", username);

                }
            } else {
                logger.warn("Cannot change password for user {} since such user was not found", username);
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
