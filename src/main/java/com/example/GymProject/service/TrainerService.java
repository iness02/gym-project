package com.example.GymProject.service;

import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.mapper.EntityMapper;
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
import java.util.stream.Collectors;

@Service
public class TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityMapper entityMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Transactional
    public TrainerDto createTrainer(TrainerDto trainerDto) {
        Assert.notNull(trainerDto, "TraineeDto cannot be null");
        Trainer trainer = entityMapper.toTrainer(trainerDto);
        User user = entityMapper.toUser(trainerDto.getUserDto());
        user.setUsername(userService.generateUniqueUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);
        logger.info("Creating username for trainer with id {}", trainerDto.getId());
        userDao.createUser(user);
        trainer.setUser(user);
        return entityMapper.toTrainerDto(trainerDao.createTrainer(trainer));
    }

    public TrainerDto getTrainerByUsername(String username, String password) {
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            Trainer trainer = trainerDao.getTrainerByUsername(username);
            UserDto userDto = entityMapper.toUserDto(trainer.getUser());
            TrainerDto trainerDto = entityMapper.toTrainerDto(trainer);
            trainerDto.setUserDto(userDto);
            return trainerDto;
        }
        logger.error("Authentication failed for trainee {}",username);
        return null;
    }


    public TrainerDto updateTrainer(TrainerDto trainerDTO, String password) {
        if(isAuthenticated(trainerDTO.getUserDto().getUsername(), password)) {
            Assert.notNull(trainerDTO, "TrainerDto cannot be null");
            Trainer trainer = entityMapper.toTrainer(trainerDTO);
            User user = entityMapper.toUser(trainerDTO.getUserDto());
            trainer.setUser(user);
            return entityMapper.toTrainerDto(trainerDao.updateTrainer(trainer));
        }
        logger.error("Authentication failed for trainee {}",trainerDTO.getUserDto().getUsername());
        return null;
    }


    public void changeTrainerPassword(String username, String newPassword, String password) {
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            Assert.notNull(newPassword, "New password cannot be null");
            UserDto userDTO = userService.getUserByUsername(username);
            if (userDTO != null) {
                userDTO.setPassword(newPassword);
                userService.updateUser(userDTO);
                logger.info("Password has successfully changed for trainer {}", username);
            } else {
                logger.warn("Cannot change password for trainer {} since such user was not found", username);
            }
        }
        logger.error("Authentication failed for trainee {}",username);
    }

    public void deleteTrainerByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        if(isAuthenticated(username, password)) {
            trainerDao.deleteTrainerByUsername(username);
        }
        logger.error("Authentication failed for trainee {}",username);

    }

    public void activateDeactivateTrainer(String username, boolean isActive, String password) {
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            Trainer trainer = trainerDao.getTrainerByUsername(username);
            if (trainer != null) {
                logger.info("Activation/Deactivation was successfully performed for trainer {}", username);
                trainer.getUser().setIsActive(isActive);
                userService.updateUser(entityMapper.toUserDto(trainer.getUser()));
            }
        }
        logger.error("Authentication failed for trainee {}",username);

    }

    public void activate(String username,String password){
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            Trainer trainer = trainerDao.getTrainerByUsername(username);
            if (trainer != null) {
                logger.info("Activation was successfully performed for trainer {}", username);
                trainer.getUser().setIsActive(true);
                userService.updateUser(entityMapper.toUserDto(trainer.getUser()));
            }
        }
        logger.error("Authentication failed for trainee {}",username);
    }

    public void deactivate(String username,String password){
        if(isAuthenticated(username, password)) {
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerDao.getTrainerByUsername(username);
        if (trainer != null) {
            logger.info("Deactivation was successfully performed for trainer {}", username);
            trainer.getUser().setIsActive(false);
            userService.updateUser(entityMapper.toUserDto(trainer.getUser()));
            }
        }
        logger.error("Authentication failed for trainee {}",username);

    }


    public List<TrainingDto> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName, String password) {
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            List<Training> trainings = trainerDao.getTrainerTrainings(username, fromDate, toDate, traineeName);
            logger.info("Getting trainings for trainer {}", username);
            return trainings.stream()
                    .map(entityMapper::toTrainingDto)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}",username);
        return null;
    }

    public boolean isAuthenticated(String username,String password){
        return userService.matchUsernameAndPassword(username, password);
    }
}
