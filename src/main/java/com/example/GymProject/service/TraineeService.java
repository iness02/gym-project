package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.exception.InvalidCredentialsException;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import com.example.GymProject.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.response.GetTrainingResponse;
import com.example.GymProject.response.UserPassResponse;
import com.example.GymProject.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.response.traineeResponse.TrainerForTraineeResponse;
import com.example.GymProject.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    @Autowired
    private TrainerService trainerService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private EntityMapper entityMapper;

    @Transactional
    public UserPassResponse createTrainee(TraineeDto traineeDto) {
        if (traineeDto == null) {
            throw new InvalidCredentialsException("TraineeDto cannot be null");
        }
        Trainee trainee = entityMapper.toTrainee(traineeDto);
        User user = entityMapper.toUser(traineeDto.getUserDto());
        user.setUsername(userService.generateUniqueUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);
        logger.info("Creating username for trainee with id {}", traineeDto.getId());
        userDao.createUser(user);
        trainee.setUser(user);
        Trainee trainee1 = traineeDao.createTrainee(trainee);
        entityMapper.toTraineeDto(trainee1);
        return new UserPassResponse(trainee1.getUser().getUsername(), trainee1.getUser().getPassword());
    }

    public TraineeDto getTraineeByUsername(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee == null) {
                throw new ResourceNotFoundException("Trainee not found with username: " + username);
            }
            return entityMapper.toTraineeDto(trainee);
        }
        logger.error("Authentication failed for trainee {}", username);
        return null;

    }

    public GetTraineeProfileResponse getTraineeByUsername(String username) {
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        TraineeDto traineeDto = entityMapper.toTraineeDto(trainee);
        return entityMapper.toGetTraineeProfileResponse(traineeDto);

    }

    public UpdateTraineeProfileResponse updateTrainee(TraineeDto traineeDto) {
        if (traineeDto == null) {
            throw new InvalidCredentialsException("TraineeDto cannot be null");
        }
        if (isAuthenticated(traineeDto.getUserDto().getUsername(), traineeDto.getUserDto().getPassword())) {
            Trainee trainee = entityMapper.toTrainee(traineeDto);
            User user = entityMapper.toUser(traineeDto.getUserDto());
            userDao.updateUser(user);
            trainee.setUser(user);
            Trainee trainee1 = traineeDao.updateTrainee(trainee);
            TraineeDto traineeDto1 = entityMapper.toTraineeDto(trainee1);
            return entityMapper.toUpdateTraineeProfileResponse(traineeDto1);
        }
        logger.error("Authentication failed for trainee {}", traineeDto.getUserDto().getUsername());
        return null;
    }


    public boolean deleteTraineeByUsername(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            traineeDao.deleteTraineeByUsername(username);
            return true;
        }
        logger.error("Authentication failed for trainee {}", username);
        return false;

    }

    public boolean matchUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        return userService.matchUsernameAndPassword(username, password);
    }


    public boolean activate(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee == null) {
                throw new ResourceNotFoundException("Trainee not found with username: " + username);
            }
            logger.info("Activation was successfully performed for trainee {}", username);
            trainee.getUser().setIsActive(true);
            userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
            return true;
        }
        logger.error("Authentication failed for trainee {}", username);
        return false;
    }

    public boolean deactivate(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee == null) {
                throw new ResourceNotFoundException("Trainee not found with username: " + username);
            }
            logger.info("Deactivation was successfully performed for trainee {}", username);
            trainee.getUser().setIsActive(false);
            userService.updateUser(entityMapper.toUserDto(trainee.getUser()));

            return true;
        }
        logger.error("Authentication failed for trainee {}", username);
        return false;
    }

  /*  public List<TrainingDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType, String password) {
        Assert.notNull(username, "Username cannot be null");
        if (isAuthenticated(username, password)) {
            List<Training> trainings = traineeDao.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
            logger.info("Getting trainings for trainee {}", username);
            return trainings.stream()
                    .map(entityMapper::toTrainingDto)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}", username);
        return null;
    }*/

    public List<GetTrainingResponse> getTraineeTrainings(GetTraineeTrainingsRequest request) {
        if (request == null) {
            throw new InvalidCredentialsException("Request cannot be null");
        }
        if (isAuthenticated(request.getUsername(), request.getPassword())) {
            List<Training> trainings = traineeDao.getTraineeTrainings(
                    request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTrainerName(), request.getTrainingType());
            logger.info("Getting trainings for trainee {}", request.getUsername());
            List<TrainingDto> trainingDtos = trainings.stream()
                    .map(entityMapper::toTrainingDto)
                    .collect(Collectors.toList());
            return trainingDtos.stream()
                    .map(entityMapper::toGetTrainingResponse)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}", request.getUsername());
        return null;
    }

    public void updateTraineeTrainers(String username, Set<TrainerDto> trainerDtos, String password) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            logger.info("Updating trainers for trainee {}", username);
            Set<Trainer> trainers = trainerDtos.stream()
                    .map(entityMapper::toTrainer)
                    .collect(Collectors.toSet());
            trainee.setTrainers(trainers);
            traineeDao.updateTrainee(trainee);
        }
        logger.error("Authentication failed for trainee {}", username);

    }

    public List<TrainerForTraineeResponse> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames) {
        if (username == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(username, password)) {
            Set<TrainerDto> assignTrainers = new HashSet<>();
            TrainerDto trainerDto;
            List<TrainerForTraineeResponse> trainerForTraineeResponses = new LinkedList<>();
            for (String user : trainerUsernames) {
                trainerDto = trainerService.getTrainerByUsername(user);
                assignTrainers.add(trainerDto);
                TrainerForTraineeResponse response = new TrainerForTraineeResponse();
                response.setUserName(user);
                response.setFirstName(trainerDto.getUserDto().getFirstName());
                response.setLastName(trainerDto.getUserDto().getLastName());
                response.setSpecialization(trainerDto.getSpecialization());
                trainerForTraineeResponses.add(response);
            }
            Trainee trainee = traineeDao.getTraineeByUsername(username);
            if (trainee == null) {
                throw new ResourceNotFoundException("Trainee not found with username: " + username);
            }
            logger.info("Updating trainers for trainee {}", username);
            Set<Trainer> trainers = assignTrainers.stream()
                    .map(entityMapper::toTrainer)
                    .collect(Collectors.toSet());
            trainee.setTrainers(trainers);
            traineeDao.updateTrainee(trainee);
            return trainerForTraineeResponses;

        }
        logger.error("Authentication failed for trainee {}", username);
        return null;

    }

    @Transactional(readOnly = true)
    public List<TrainerDto> getUnassignedTrainers(String traineeUsername, String password) {
        if (traineeUsername == null || password == null) {
            throw new InvalidCredentialsException("Username or password is invalid");
        }
        if (isAuthenticated(traineeUsername, password)) {
            Trainee trainee = traineeDao.getTraineeByUsername(traineeUsername);
            List<Trainer> allTrainers = trainerDao.getAllTrainers();

            List<Trainer> assignedTrainers = new ArrayList<>(trainee.getTrainers());

            List<Trainer> unassignedTrainers = allTrainers.stream()
                    .filter(trainer -> !assignedTrainers.contains(trainer))
                    .collect(Collectors.toList());

            return unassignedTrainers.stream()
                    .map(entityMapper::toTrainerDto)
                    .collect(Collectors.toList());
        }
        logger.error("Authentication failed for trainee {}", traineeUsername);
        return null;
    }

    public boolean isAuthenticated(String username, String password) {
        return matchUsernameAndPassword(username, password);
    }
}