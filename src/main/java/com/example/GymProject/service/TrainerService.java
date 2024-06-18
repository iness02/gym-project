package com.example.GymProject.service;

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
import com.example.GymProject.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.request.trainerRequest.GetTrainerTrainingsRequest;
import com.example.GymProject.response.GetTrainingResponse;
import com.example.GymProject.response.UserPassResponse;
import com.example.GymProject.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.response.trainerResponse.GetTrainerProfileResponse;
import com.example.GymProject.response.trainerResponse.UpdateTrainerProfileResponse;
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
    public UserPassResponse createTrainer(TrainerDto trainerDto) {
        Assert.notNull(trainerDto, "TraineeDto cannot be null");
        Trainer trainer = entityMapper.toTrainer(trainerDto);
        User user = entityMapper.toUser(trainerDto.getUserDto());
        user.setUsername(userService.generateUniqueUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);
        logger.info("Creating username for trainer with id {}", trainerDto.getId());
        userDao.createUser(user);
        trainer.setUser(user);
        Trainer trainer1 = trainerDao.createTrainer(trainer);
        entityMapper.toTrainerDto(trainer1);
        return new UserPassResponse(trainer1.getUser().getUsername(), trainer1.getUser().getPassword());
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

    public TrainerDto getTrainerByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerDao.getTrainerByUsername(username);
        System.out.println(trainer.getTrainees()+ " 1111");
        UserDto userDto = entityMapper.toUserDto(trainer.getUser());
        TrainerDto trainerDto = entityMapper.toTrainerDto(trainer);
        trainerDto.setUserDto(userDto);
        System.out.println(trainerDto.getTrainees()+" 2222");
        return trainerDto;

    }


    public UpdateTrainerProfileResponse updateTrainer(TrainerDto trainerDto) {
        Assert.notNull(trainerDto, "TrainerDto cannot be null");
        if(isAuthenticated(trainerDto.getUserDto().getUsername(), trainerDto.getUserDto().getPassword())) {
            Trainer trainer = entityMapper.toTrainer(trainerDto);
            User user = entityMapper.toUser(trainerDto.getUserDto());
            userDao.updateUser(user);
            trainer.setUser(user);
            Trainer trainer1 = trainerDao.updateTrainer(trainer);
            TrainerDto trainerDto1 = entityMapper.toTrainerDto(trainer1);
            return entityMapper.toUpdateTrainerProfileResponse(trainerDto1);
        }
        logger.error("Authentication failed for trainee {}",trainerDto.getUserDto().getUsername());
        return null;
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

    public boolean activate(String username,String password){
        if(isAuthenticated(username, password)) {
            Assert.notNull(username, "Username cannot be null");
            Trainer trainer = trainerDao.getTrainerByUsername(username);
            if (trainer != null) {
                logger.info("Activation was successfully performed for trainer {}", username);
                trainer.getUser().setIsActive(true);
                userService.updateUser(entityMapper.toUserDto(trainer.getUser()));
            }
            return true;
        }
        logger.error("Authentication failed for trainee {}",username);
        return false;
    }

    public boolean deactivate(String username,String password){
        if(isAuthenticated(username, password)) {
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerDao.getTrainerByUsername(username);
        if (trainer != null) {
            logger.info("Deactivation was successfully performed for trainer {}", username);
            trainer.getUser().setIsActive(false);
            userService.updateUser(entityMapper.toUserDto(trainer.getUser()));
            }
        return true;
        }

        logger.error("Authentication failed for trainee {}",username);
        return false;
    }


    public List<GetTrainingResponse> getTrainerTrainings(GetTrainerTrainingsRequest request) {
        if(isAuthenticated(request.getUsername(), request.getPassword())) {
            Assert.notNull(request.getUsername(), "Username cannot be null");
            List<Training> trainings = trainerDao.getTrainerTrainings(request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTrainerName());
            logger.info("Getting trainings for trainer {}", request.getUsername());
            List<TrainingDto> trainingDtos = trainings.stream()
                    .map(entityMapper::toTrainingDto)
                    .collect(Collectors.toList());
            return trainingDtos.stream()
                    .map(entityMapper::toGetTrainingResponse)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}",request.getUsername());
        return null;
    }


    public boolean isAuthenticated(String username,String password){
        return userService.matchUsernameAndPassword(username, password);
    }
}
