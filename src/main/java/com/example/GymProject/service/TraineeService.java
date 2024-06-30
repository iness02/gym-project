package com.example.GymProject.service;

import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.request.TraineeRegistrationRequestDto;
import com.example.GymProject.dto.request.TraineeTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTraineeProfileRequestDto;
import com.example.GymProject.dto.respone.*;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.User;
import com.example.GymProject.repository.TraineeRepository;
import com.example.GymProject.repository.TrainerRepository;
import com.example.GymProject.repository.TrainingRepository;
import com.example.GymProject.repository.UserRepository;
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
    TraineeRepository traineeRepository;
    @Autowired
    TrainerRepository trainerRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    TrainerService trainerService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Transactional
    public UserPassResponseDto createTrainee(TraineeRegistrationRequestDto traineeDto) {

        Assert.notNull(traineeDto, "TraineeRegistrationRequestDto cannot be null");
        String username = userService.generateUniqueUserName(traineeDto.getFirstName(), traineeDto.getLastName());

        if (traineeRepository.existsByUserUsername(username) || trainerRepository.existsByUserUsername(username)) {
            throw new UserAlreadyRegisteredException("User is already registered as a trainer or trainee");
        }
        Trainee trainee = entityMapper.toTrainee(traineeDto);
        User user = new User();
        user.setUsername(username);
        user.setFirstName(traineeDto.getFirstName());
        user.setLastName(traineeDto.getLastName());
        user.setPassword(Utils.generatePassword());
        user.setIsActive(true);
        logger.info("Creating user for trainee with name {}", traineeDto.getFirstName() + " " + traineeDto.getLastName());
        userRepository.save(user);
        trainee.setUser(user);
        Trainee trainee1 = traineeRepository.save(trainee);
        return new UserPassResponseDto(trainee1.getId(), trainee1.getUser().getUsername(), trainee1.getUser().getPassword());
    }

    public TraineeProfileResponseDto getTraineeByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        Trainee trainee = traineeRepository.getTraineeByUserUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        TraineeDto traineeDto = entityMapper.toTraineeDto(trainee);
        return entityMapper.toTraineeProfileResponseDto(traineeDto);

    }


    @Transactional
    public UpdateTraineeProfileResponseDto updateTrainee(UpdateTraineeProfileRequestDto traineeDto) {
        Assert.notNull(traineeDto, "UpdateTraineeProfileRequestDto cannot be null");
        User existingUser = userRepository.findByUsername(traineeDto.getUsername());
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found with username: " + traineeDto.getUsername());
        }

        existingUser.setFirstName(traineeDto.getFirstName());
        existingUser.setLastName(traineeDto.getLastName());
        existingUser.setPassword(traineeDto.getPassword());
        existingUser.setIsActive(traineeDto.getIsActive());
        userRepository.save(existingUser);

        Trainee existingTrainee = traineeRepository.findByUser(existingUser);
        if (existingTrainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + traineeDto.getUsername());

        }

        existingTrainee.setDateOfBirth(traineeDto.getDateOfBirth());
        existingTrainee.setAddress(traineeDto.getAddress());
        traineeRepository.save(existingTrainee);

        return entityMapper.toUpdateTraineeProfileResponseDto(entityMapper.toTraineeDto(existingTrainee));
    }

    @Transactional
    public boolean deleteTraineeByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeRepository.getTraineeByUserUsername(username);
        User user = trainee.getUser();
        if (trainee != null && user != null) {
            List<Training> trainings = trainingRepository.findTraineeTrainings(username, null, null, null, null);
            List<Long> trainingIds = trainings.stream().map(Training::getId).collect(Collectors.toList());
            trainingRepository.removeTrainings(trainingIds);
            traineeRepository.deleteByUserUsername(username);
        } else {
            logger.warn("No trainee found with username: {}", username);
            return false;
        }
        return true;
    }

    @Transactional
    public boolean activate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeRepository.getTraineeByUserUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        logger.info("Activation was successfully performed for trainee {}", username);
        trainee.getUser().setIsActive(true);
        userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
        return true;
    }

    @Transactional
    public boolean deactivate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeRepository.getTraineeByUserUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        logger.info("Deactivation was successfully performed for trainee {}", username);
        trainee.getUser().setIsActive(false);
        userService.updateUser(entityMapper.toUserDto(trainee.getUser()));
        return true;
    }

    public List<GetTrainingResponseDto> getTraineeTrainings(TraineeTrainingsRequestDto request) {
        Assert.notNull(request, "Request cannot be null");
        List<Training> trainings = trainingRepository.findTraineeTrainings(
                request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTrainerName(), request.getTrainingType());
        logger.info("Getting trainings for trainee {}", request.getUsername());
        List<TrainingDto> trainingDtos = trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
        return trainingDtos.stream()
                .map(entityMapper::toGetTrainingResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<TrainerForTraineeResponseDto> updateTraineeTrainers(String username, String password, Set<String> trainerUsernames) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");

        Set<TrainerDto> assignTrainers = new HashSet<>();
        TrainerDto trainerDto;
        List<TrainerForTraineeResponseDto> trainerForTraineeResponses = new LinkedList<>();

        for (String user : trainerUsernames) {
            trainerDto = trainerService.getTrainerDtoByUsername(user);
            assignTrainers.add(trainerDto);

            TrainerForTraineeResponseDto response = new TrainerForTraineeResponseDto();
            response.setUserName(user);
            response.setFirstName(trainerDto.getUserDto().getFirstName());
            response.setLastName(trainerDto.getUserDto().getLastName());
            response.setSpecialization(trainerDto.getSpecialization());
            trainerForTraineeResponses.add(response);
        }

        Trainee trainee = traineeRepository.getTraineeByUserUsername(username);
        if (trainee == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }
        logger.info("Updating trainers for trainee {}", username);
        Set<Trainer> trainers = assignTrainers.stream()
                .map(entityMapper::toTrainer)
                .collect(Collectors.toSet());
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
        return trainerForTraineeResponses;
    }

    @Transactional(readOnly = true)
    public List<UnassignedTrainerResponseDto> getUnassignedTrainers(String traineeUsername, String password) {
        Assert.notNull(traineeUsername, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainee trainee = traineeRepository.getTraineeByUserUsername(traineeUsername);
        List<Trainer> allTrainers = trainerRepository.findAll();

        List<Trainer> assignedTrainers = new ArrayList<>(trainee.getTrainers());

        List<Trainer> unassignedTrainers = allTrainers.stream()
                .filter(trainer -> !assignedTrainers.contains(trainer))
                .collect(Collectors.toList());

        List<TrainerDto> unassignedTrainerDtos = unassignedTrainers.stream()
                .map(entityMapper::toTrainerDto)
                .collect(Collectors.toList());
        return unassignedTrainerDtos.stream()
                .map(entityMapper::toUnassignedTrainerResponseDto)
                .collect(Collectors.toList());
    }


}
