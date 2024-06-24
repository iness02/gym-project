package com.example.GymProject.service;

import com.example.GymProject.dao.TraineeDao;
import com.example.GymProject.dao.TrainerDao;
import com.example.GymProject.dao.TrainingDao;
import com.example.GymProject.dao.UserDao;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.request.traineerRquest.GetTraineeTrainingsRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.UserPassResponse;
import com.example.GymProject.dto.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.dto.response.traineeResponse.TrainerForTraineeResponse;
import com.example.GymProject.dto.response.traineeResponse.UnassignedTrainerResponse;
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private TrainerDao trainerDao;
    @Autowired
    private TrainingDao trainingDao;
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

        Assert.notNull(traineeDto, "TraineeDto cannot be null");
        String username = userService.generateUniqueUserName(traineeDto.getUserDto().getFirstName(), traineeDto.getUserDto().getLastName());

        if (traineeDao.existsByUsername(username) || trainerDao.existsByUsername(username)) {
            throw new UserAlreadyRegisteredException("User is already registered as a trainer or trainee");
        }

        Trainee trainee = entityMapper.toTrainee(traineeDto);
        User user = entityMapper.toUser(traineeDto.getUserDto());
        user.setUsername(username);
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);

        logger.info("Creating username for trainee with id {}", traineeDto.getId());
        userDao.createUser(user);
        trainee.setUser(user);
        Trainee trainee1 = traineeDao.createTrainee(trainee);
        entityMapper.toTraineeDto(trainee1);
        return new UserPassResponse(trainee1.getId(), trainee1.getUser().getUsername(), trainee1.getUser().getPassword());
    }

    public GetTraineeProfileResponse getTraineeByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        TraineeDto traineeDto = entityMapper.toTraineeDto(trainee);
        return entityMapper.toGetTraineeProfileResponse(traineeDto);

    }


    @Transactional
    public UpdateTraineeProfileResponse updateTrainee(TraineeDto traineeDto) {
        Assert.notNull(traineeDto, "TraineeDto cannot be null");
        Trainee trainee = entityMapper.toTrainee(traineeDto);
        User user = entityMapper.toUser(traineeDto.getUserDto());
        userDao.updateUser(user);
        trainee.setUser(user);
        Trainee trainee1 = traineeDao.updateTrainee(trainee);
        TraineeDto traineeDto1 = entityMapper.toTraineeDto(trainee1);
        return entityMapper.toUpdateTraineeProfileResponse(traineeDto1);
    }


    public boolean deleteTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        User user = trainee.getUser();
        if (trainee != null && user != null) {
            List<Training> trainings = traineeDao.getTraineeTrainings(username,null,null,null,null);
            List<Long> trainingIds = trainings.stream().map(Training::getId).collect(Collectors.toList());
            trainingDao.removeTrainings(trainingIds);
            traineeDao.deleteTraineeByUsername(username,trainee);
        } else {
            logger.warn("No trainee found with username: {}", username);
        }
        return true;
    }


    public boolean activate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        logger.info("Activation was successfully performed for trainee {}", username);
        trainee.getUser().setIsActive(true);
        userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
        return true;
    }

    public boolean deactivate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeDao.getTraineeByUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        logger.info("Deactivation was successfully performed for trainee {}", username);
        trainee.getUser().setIsActive(false);
        userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
        return true;
    }

    public List<GetTrainingResponse> getTraineeTrainings(GetTraineeTrainingsRequest request) {
        Assert.notNull(request, "Request cannot be null");
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


    @Transactional
    public List<TrainerForTraineeResponse> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
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

    @Transactional(readOnly = true)
    public List<UnassignedTrainerResponse> getUnassignedTrainers(String traineeUsername, String password) {
        Assert.notNull(traineeUsername, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeDao.getTraineeByUsername(traineeUsername);
        List<Trainer> allTrainers = trainerDao.getAllTrainers();

        List<Trainer> assignedTrainers = new ArrayList<>(trainee.getTrainers());

        List<Trainer> unassignedTrainers = allTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .collect(Collectors.toList());

        List<TrainerDto> unassignedTrainerDtos = unassignedTrainers.stream()
                .map(entityMapper::toTrainerDto)
                .collect(Collectors.toList());
        return unassignedTrainerDtos.stream()
                .map(entityMapper::toUnassignedTrainerResponse)
                .collect(Collectors.toList());
    }


}