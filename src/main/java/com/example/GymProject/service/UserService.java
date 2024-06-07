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
    private UserDao userDAO;
    @Autowired
    private EntityMapper entityMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    public boolean matchUsernameAndPassword(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        User user = userDAO.findUserByUsername(username);
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
        User user = userDAO.findUserByUsername(username);
        return entityMapper.toUserDto(user);
    }


    public UserDto updateUser(UserDto userDto) {
        Assert.notNull(userDto, "UserDto cannot be null");
        User user = entityMapper.toUser(userDto);
        return entityMapper.toUserDto(userDAO.updateUser(user));
    }
}
