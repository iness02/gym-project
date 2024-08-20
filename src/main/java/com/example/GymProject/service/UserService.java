
package com.example.GymProject.service;

import com.example.GymProject.dto.UserDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.User;
import com.example.GymProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private EntityMapper entityMapper;


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public UserDto getUserByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        User user = userRepository.findByUsername(username);
        return entityMapper.toUserDto(user);
    }

    public String generateUniqueUserName(String firstName, String lastName) {
        String baseUserName = firstName + "." + lastName;
        boolean userNameExist = userRepository.existsByUsername(baseUserName);
        if (userNameExist) {
            long nextUserId = getNextAvailableUserId() + 1L;
            return baseUserName + nextUserId;
        } else {
            return baseUserName;
        }
    }

    public long getNextAvailableUserId() {
        return userRepository.findMaxId();
    }

    public UserDto updateUser(UserDto userDto) {
        Assert.notNull(userDto, "UserDto cannot be null");
        User user = entityMapper.toUser(userDto);
        return entityMapper.toUserDto(userRepository.save(user));
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
