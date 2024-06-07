package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dto.TraineeDTO;
import com.example.GymProject.dto.TrainerDTO;
import com.example.GymProject.dto.TrainingDTO;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
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

import static com.example.GymProject.util.Utils.generatePassword;
import static com.example.GymProject.util.Utils.generateUsername;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDAO;

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    public TraineeDTO createTrainee(TraineeDTO traineeDTO) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        Trainee trainee = EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO);
        logger.info("Creating username for trainee with id {}", traineeDTO.getId());
        String username = generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName(), true);
        String password = generatePassword();
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(password);
        return EntityMapper.INSTANCE.traineeToTraineeDTO(traineeDAO.createTrainee(trainee));
    }

    public TraineeDTO getTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        return EntityMapper.INSTANCE.traineeToTraineeDTO(trainee);
    }


    public TraineeDTO updateTrainee(TraineeDTO traineeDTO, String password) {
        Assert.notNull(traineeDTO, "TraineeDto cannot be null");
        authenticate(traineeDTO.getUser().getUsername(), password);
        Trainee trainee = EntityMapper.INSTANCE.traineeDTOToTrainee(traineeDTO);
        return EntityMapper.INSTANCE.traineeToTraineeDTO(traineeDAO.updateTrainee(trainee));
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
        UserDTO userDTO = userService.getUserByUsername(username);
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
            userService.updateUser(EntityMapper.INSTANCE.userToUserDTO(trainee.getUser()));
        }
    }

    public List<TrainingDTO> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        List<Training> trainings = traineeDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        logger.info("Getting trainings for trainee {}", username);
        return trainings.stream()
                .map(EntityMapper.INSTANCE::trainingToTrainingDTO)
                .collect(Collectors.toList());
    }


    public void updateTraineeTrainers(String username, Set<TrainerDTO> trainerDTOs, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        logger.info("Updating trainers for trainee {}", username);
        Set<Trainer> trainers = trainerDTOs.stream()
                .map(EntityMapper.INSTANCE::trainerDTOToTrainer)
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
