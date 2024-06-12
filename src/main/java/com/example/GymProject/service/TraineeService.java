package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import com.example.GymProject.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private EntityMapper entityMapper;

    @Transactional
    public TraineeDto createTrainee(TraineeDto traineeDTO) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        Trainee trainee = entityMapper.toTrainee(traineeDTO);
        User user = entityMapper.toUser(traineeDTO.getUserDto());
        user.setUsername(userService.generateUniqueUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);
        logger.info("Creating username for trainee with id {}", traineeDTO.getId());
        userDao.createUser(user);
        trainee.setUser(user);
        Trainee trainee1 = traineeDao.createTrainee(trainee);
        return entityMapper.toTraineeDto(trainee1);

    }

    public TraineeDto getTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)){
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        return entityMapper.toTraineeDto(trainee);
    }
        logger.error("Authentication failed for trainee {}",username);
        return null;

    }


    public TraineeDto updateTrainee(TraineeDto traineeDTO, String password) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        if(isAuthenticated(traineeDTO.getUserDto().getUsername(), password)) {
            Trainee trainee = entityMapper.toTrainee(traineeDTO);
            User user = entityMapper.toUser(traineeDTO.getUserDto());
            trainee.setUser(user);
            return entityMapper.toTraineeDto(traineeDao.updateTrainee(trainee));
        }
        logger.error("Authentication failed for trainee {}",traineeDTO.getUserDto().getUsername());
        return null;
    }


    public void deleteTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)) {
            traineeDao.deleteTraineeByUsername(username);
        }
        logger.error("Authentication failed for trainee {}",username);

    }

    public boolean matchUsernameAndPassword(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        return userService.matchUsernameAndPassword(username, password);
    }


    public void changeTraineePassword(String username, String newPassword, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(newPassword, "New password cannot be null");
       if(isAuthenticated(username, password)) {
           UserDto userDTO = userService.getUserByUsername(username);
           if (userDTO != null) {
               if (!password.equals(newPassword)) {
                   userDTO.setPassword(newPassword);
                   userService.updateUser(userDTO);
                   logger.info("Password has successfully changed for trainee {}", username);
               } else {
                   logger.warn("Cannot change password for user {} since new password is equal to old password", username);

               }
           } else {
               logger.warn("Cannot change password for user {} since such user was not found", username);
           }
       }
        logger.error("Authentication failed for trainee {}",username);

    }


    public void activate(String username, String password){
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee != null) {
                logger.info("Activation was successfully performed for trainee {}", username);
                trainee.getUser().setIsActive(true);
                userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
            }
        }
        logger.error("Authentication failed for trainee {}",username);

    }
    public void deactivate(String username, String password){
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee != null) {
                logger.info("Deactivation was successfully performed for trainee {}", username);
                trainee.getUser().setIsActive(true);
                userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
            }
        }
        logger.error("Authentication failed for trainee {}",username);

    }

    public List<TrainingDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, String password) {
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)){
        List<Training> trainings = traineeDao.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        logger.info("Getting trainings for trainee {}", username);
        return trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
    }
        logger.error("Authentication failed for trainee {}",username);
        return null;
    }

    public void updateTraineeTrainers(String username, Set<TrainerDto> trainerDtos, String password) {
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            logger.info("Updating trainers for trainee {}", username);
            Set<Trainer> trainers = trainerDtos.stream()
                    .map(entityMapper::toTrainer)
                    .collect(Collectors.toSet());
            trainee.setTrainers(trainers);
            traineeDao.updateTrainee(trainee);
        }
        logger.error("Authentication failed for trainee {}",username);

    }

    @Transactional(readOnly = true)
    public List<TrainerDto> getUnassignedTrainers(String traineeUsername, String password) {
        if(isAuthenticated(traineeUsername, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(traineeUsername);
            List<Trainer> allTrainers = trainerDao.getAllTrainers();

            List<Trainer> assignedTrainers = trainee.getTrainers().stream()
                    .collect(Collectors.toList());

            List<Trainer> unassignedTrainers = allTrainers.stream()
                    .filter(trainer -> !assignedTrainers.contains(trainer))
                    .collect(Collectors.toList());

            return unassignedTrainers.stream()
                    .map(entityMapper::toTrainerDto)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}",traineeUsername);
        return null;
    }

    public boolean isAuthenticated(String username,String password){
        return matchUsernameAndPassword(username,password);
    }
}