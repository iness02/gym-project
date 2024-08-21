package com.example.GymProject.service;

import com.example.GymProject.client.MicroserviceClient;
import com.example.GymProject.dto.TrainingDto;
import com.example.GymProject.dto.request.AddTrainingRequestDto;
import com.example.GymProject.dto.request.TrainingRequest;
import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.model.*;
import com.example.GymProject.repository.TraineeRepository;
import com.example.GymProject.repository.TrainerRepository;
import com.example.GymProject.repository.TrainingRepository;
import com.example.GymProject.repository.TrainingTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private MicroserviceClient microserviceClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Transactional
    public TrainingDto addTraining(AddTrainingRequestDto request) {
        logger.info("Adding training with name {} ", request.getName());
        Assert.notNull(request, "AddTrainingRequestDto cannot be null");
        TrainingDto trainingDto = entityMapper.toTrainingDto(request);
        Training training = entityMapper.toTraining(trainingDto);
        Trainee trainee = entityMapper.toTrainee(trainingDto.getTrainee());
        Trainer trainer = entityMapper.toTrainer(trainingDto.getTrainer());
        TrainingType trainingType = entityMapper.toTrainingType(trainingDto.getTrainingType());

        if (trainingType == null) {
            TrainingType trainingType1 = new TrainingType();
            trainingType1.setTrainingTypeName(Trainings.FITNESS);
            if (trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingType1.getTrainingTypeName()) == null)
                trainingTypeRepository.save(trainingType1);
            else
                trainingType1 = trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingType1.getTrainingTypeName());
            training.setTrainingType(trainingType1);
        } else {
            training.setTrainingType(trainingType);

        }
        training.setTrainee(traineeRepository.getTraineeByUserUsername(trainee.getUser().getUsername()));
        training.setTrainer(trainerRepository.getTrainerByUserUsername(trainer.getUser().getUsername()));
        TrainingRequest trainingRequest = TrainingRequest.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().getIsActive())
                .date(Date.from(Instant.from(training.getTrainingDate())))
                .duration(training.getTrainingDuration())
                .action("ADD")
                .build();
        microserviceClient.actionTraining(trainingRequest, MDC.get("transactionId"), MDC.get("Authorization"));
        return entityMapper.toTrainingDto(trainingRepository.save(training));
    }


    public List<TrainingDto> getAllTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        return trainings.stream()
                .map(entityMapper::toTrainingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrainingDto updateTraining(TrainingDto trainingDTO, String username, String password) {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(password, "Password cannot be null");
        Training training = entityMapper.toTraining(trainingDTO);
        return entityMapper.toTrainingDto(trainingRepository.save(training));

    }

}
