package com.example.GymProject.service;

import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.model.User;
import com.example.GymProject.mapper.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserService {
    @Autowired
    private UserDao userDAO;
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

    public UserDTO getUserByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        User user = userDAO.findUserByUsername(username);
        return EntityMapper.INSTANCE.userToUserDTO(user);
    }


    public UserDTO updateUser(UserDTO userDto) {
        Assert.notNull(userDto, "UserDto cannot be null");
        User user = EntityMapper.INSTANCE.userDTOToUser(userDto);
        return EntityMapper.INSTANCE.userToUserDTO(userDAO.updateUser(user));
    }
}
