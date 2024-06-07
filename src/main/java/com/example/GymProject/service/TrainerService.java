package com.example.GymProject.service;

import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dto.TrainerDTO;
import com.example.GymProject.dto.TrainingDTO;
import com.example.GymProject.dto.UserDTO;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerService {
    @Autowired
    private TrainerDao trainerDAO;

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    public TrainerDTO getTrainerByUsername(String username, String password) {
        authenticate(username, password);
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerDAO.getTrainerByUsername(username);
        return EntityMapper.INSTANCE.trainerToTrainerDTO(trainer);
    }


    public TrainerDTO updateTrainer(TrainerDTO trainerDTO, String password) {
        authenticate(trainerDTO.getUser().getUsername(), password);
        Assert.notNull(trainerDTO, "TrainerDto cannot be null");
        Trainer trainer = EntityMapper.INSTANCE.trainerDTOToTrainer(trainerDTO);
        return EntityMapper.INSTANCE.trainerToTrainerDTO(trainerDAO.updateTrainer(trainer));
    }


    public void changeTrainerPassword(String username, String newPassword, String password) {
        authenticate(username, password);
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(newPassword, "New password cannot be null");
        UserDTO userDTO = userService.getUserByUsername(username);
        if (userDTO != null) {
            userDTO.setPassword(newPassword);
            userService.updateUser(userDTO);
            logger.info("Password has successfully changed for trainer {}", username);
        } else {
            logger.warn("Cannot change password for trainer {} since such user was not found", username);
        }
    }
    public void deleteTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        authenticate(username, password);
        trainerDAO.deleteTrainerByUsername(username);
    }
    public void activateDeactivateTrainer(String username, boolean isActive, String password) {
        authenticate(username, password);
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerDAO.getTrainerByUsername(username);
        if (trainer != null) {
            logger.info("Activation/Deactivation was successfully performed for trainer {}", username);
            trainer.getUser().setIsActive(isActive);
            userService.updateUser(EntityMapper.INSTANCE.userToUserDTO(trainer.getUser()));
        }
    }

    public List<TrainingDTO> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName, String password) {
        authenticate(username, password);
        Assert.notNull(username, "Username cannot be null");
        List<Training> trainings = trainerDAO.getTrainerTrainings(username, fromDate, toDate, traineeName);
        logger.info("Getting trainings for trainer {}", username);
        return trainings.stream()
                .map(EntityMapper.INSTANCE::trainingToTrainingDTO)
                .collect(Collectors.toList());
    }
    private void authenticate(String username, String password) {
        if (!userService.matchUsernameAndPassword(username, password)) {
            throw new SecurityException("Authentication failed for username: " + username);
        }
    }
}
