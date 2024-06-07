package com.example.GymProject.mapper;

import com.example.GymProject.dto.*;
import com.example.GymProject.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    TraineeDTO traineeToTraineeDTO(Trainee trainee);

    Trainee traineeDTOToTrainee(TraineeDTO traineeDTO);

    TrainerDTO trainerToTrainerDTO(Trainer trainer);

    Trainer trainerDTOToTrainer(TrainerDTO trainerDTO);

    TrainingDTO trainingToTrainingDTO(Training training);

    Training trainingDTOToTraining(TrainingDTO trainingDTO);

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}