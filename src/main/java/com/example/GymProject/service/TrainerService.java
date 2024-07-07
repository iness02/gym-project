package com.example.GymProject.service;

import com.example.GymProject.dto.TrainerDto;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.dto.request.TrainerRegistrationRequestDto;
import com.example.GymProject.dto.request.TrainerTrainingsRequestDto;
import com.example.GymProject.dto.request.UpdateTrainerProfileRequestDto;
import com.example.GymProject.dto.respone.GetTrainingResponseDto;
import com.example.GymProject.dto.respone.TrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UpdateTrainerProfileResponseDto;
import com.example.GymProject.dto.respone.UserPassResponseDto;
import com.example.GymProject.exception.ResourceNotFoundException;
import com.example.GymProject.exception.UserAlreadyRegisteredException;
import com.example.GymProject.mapper.EntityMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Transactional
    public UserPassResponseDto createTrainer(TrainerRegistrationRequestDto trainerDto) {

        Assert.notNull(trainerDto, "TrainerRegistrationRequestDto cannot be null");
        String username = userService.generateUniqueUserName(trainerDto.getFirstName(), trainerDto.getLastName());

        if (traineeRepository.existsByUserUsername(username) || trainerRepository.existsByUserUsername(username)) {
            throw new UserAlreadyRegisteredException("User is already registered as a trainer or trainee");
        }
        Trainer trainer = entityMapper.toTrainer(trainerDto);
        User user = new User();
        user.setUsername(username);
        user.setFirstName(trainerDto.getFirstName());
        user.setLastName(trainerDto.getLastName());
        user.setPassword(passwordEncoder.encode(trainerDto.getPassword()));
        user.setIsActive(true);
        logger.info("Creating user for trainee with name {}", trainerDto.getFirstName() + " " + trainerDto.getLastName());
        userRepository.save(user);
        trainer.setUser(user);
        Trainer trainer1 = trainerRepository.save(trainer);
        return new UserPassResponseDto(trainer1.getId(), trainer1.getUser().getUsername(), trainer1.getUser().getPassword());
    }

    public TrainerDto getTrainerDtoByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerRepository.getTrainerByUserUsername(username);
        if (trainer == null) {
            throw new ResourceNotFoundException("Trainer not found with username: " + username);
        }
        UserDto userDto = entityMapper.toUserDto(trainer.getUser());
        TrainerDto trainerDto = entityMapper.toTrainerDto(trainer);
        trainerDto.setUserDto(userDto);
        return trainerDto;
    }

    public TrainerProfileResponseDto getTrainerByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        Trainer trainer = trainerRepository.getTrainerByUserUsername(username);
        if (trainer == null) {
            throw new ResourceNotFoundException("Trainer not found with username: " + username);
        }
        TrainerDto trainerDto = entityMapper.toTrainerDto(trainer);
        return entityMapper.toTrainerProfileResponseDto(trainerDto);
    }


    @Transactional
    public UpdateTrainerProfileResponseDto updateTrainer(UpdateTrainerProfileRequestDto trainerDto) {
        Assert.notNull(trainerDto, "TrainerDto cannot be null");
        User existingUser = userRepository.findByUsername(trainerDto.getUsername());
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found with username: " + trainerDto.getUsername());
        }

        existingUser.setLastName(trainerDto.getLastName());
        existingUser.setPassword(trainerDto.getPassword());
        existingUser.setIsActive(trainerDto.getIsActive());
        userRepository.save(existingUser);

        Trainer existingTrainer = trainerRepository.findByUser(existingUser);
        if (existingTrainer == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + trainerDto.getUsername());

        }
        existingTrainer.setSpecialization(trainerDto.getSpecialization());
        return entityMapper.toUpdateTrainerProfileResponseDto(trainerRepository.save(existingTrainer));
    }

    @Transactional
    public boolean deleteTrainerByUsername(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainer trainer = trainerRepository.getTrainerByUserUsername(username);
        User user = trainer.getUser();
        if (trainer != null && user != null) {
            trainerRepository.deleteByUserUsername(username);
        } else {
            logger.warn("No trainer found with username: {}", username);
            return false;
        }
        return true;
    }

    @Transactional
    public boolean activate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainer trainer = trainerRepository.getTrainerByUserUsername(username);
        if (trainer == null) {
            throw new ResourceNotFoundException("Trainee not found with username: " + username);
        }

        logger.info("Activation was successfully performed for trainer {}", username);
        trainer.getUser().setIsActive(true);
        userService.updateUser(entityMapper.toUserDto(trainer.getUser()));

        return true;
    }

    @Transactional
    public boolean deactivate(String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Trainer trainer = trainerRepository.getTrainerByUserUsername(username);
        if (trainer == null) {
            throw new ResourceNotFoundException("Trainer not found with username: " + username);
        }

        logger.info("Deactivation was successfully performed for trainer {}", username);
        trainer.getUser().setIsActive(false);
        userService.updateUser(entityMapper.toUserDto(trainer.getUser()));

        return true;
    }


    public List<GetTrainingResponseDto> getTrainerTrainings(TrainerTrainingsRequestDto request) {
        Assert.notNull(request, "Request cannot be null");
        Assert.notNull(request.getUsername(), "Username cannot be null");
        List<Training> trainings = trainingRepository.findTrainerTrainings(request.getUsername(), request.getPeriodFrom(), request.getPeriodTo(), request.getTraineeName());
        logger.info("Getting trainings for trainer {}", request.getUsername());
        List<TrainingDto> trainingDtos = trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
        return trainingDtos.stream()
                .map(entityMapper::toGetTrainingResponseDto)
                .collect(Collectors.toList());
    }


}
