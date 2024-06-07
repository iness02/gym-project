package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.GymProject.util.Utils.generatePassword;
import static com.example.GymProject.util.Utils.generateUsername;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDAO;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private EntityMapper entityMapper;
    
    public TraineeDto createTrainee(TraineeDto traineeDTO) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        Trainee trainee = entityMapper.toTrainee(traineeDTO);
        logger.info("Creating username for trainee with id {}", traineeDTO.getId());
        String username = generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName(), true);
        String password = generatePassword();
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(password);
        return entityMapper.toTraineeDto(traineeDAO.createTrainee(trainee));
    }

    public TraineeDto getTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        return entityMapper.toTraineeDto(trainee);
    }


    public TraineeDto updateTrainee(TraineeDto traineeDTO, String password) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        authenticate(traineeDTO.getUserDTO().getUsername(), password);
        Trainee trainee = entityMapper.toTrainee(traineeDTO);
        return entityMapper.toTraineeDto(traineeDAO.updateTrainee(trainee));
    }


    public void deleteTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        traineeDAO.deleteTraineeByUsername(username);
    }

    public boolean matchUsernameAndPassword(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        return userService.matchUsernameAndPassword(username, password);
    }


    public void changeTraineePassword(String username, String newPassword, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(newPassword, "New password cannot be null");
        authenticate(username, password);
        UserDto userDTO = userService.getUserByUsername(username);
        if (userDTO != null) {
            userDTO.setPassword(newPassword);
            userService.updateUser(userDTO);
            logger.info("Password has successfully changed for trainee {}", username);
        } else {
            logger.warn("Cannot change password for user {} since such user was not found", username);
        }
    }

    public void activateDeactivateTrainee(String username, boolean isActive, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        if (trainee != null) {
            logger.info("Activation/Deactivation was successfully performed for trainee {}", username);
            trainee.getUser().setIsActive(isActive);
            userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
        }
    }

    public List<TrainingDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        List<Training> trainings = traineeDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        logger.info("Getting trainings for trainee {}", username);
        return trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
    }


    public void updateTraineeTrainers(String username, Set<TrainerDto> trainerDtos, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        logger.info("Updating trainers for trainee {}", username);
        Set<Trainer> trainers = trainerDtos.stream()
                .map(entityMapper::toTrainer)
                .collect(Collectors.toSet());
        trainee.setTrainers(trainers);
        traineeDAO.updateTrainee(trainee);
    }

    private void authenticate(String username, String password) {
        if (!matchUsernameAndPassword(username, password)) {
            throw new SecurityException("Authentication failed for username: " + username);
        }
    }
}
