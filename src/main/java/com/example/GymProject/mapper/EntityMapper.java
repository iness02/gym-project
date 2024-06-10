package com.example.GymProject.mapper;

import com.example.GymProject.dto.*;
import com.example.GymProject.model.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EntityMapper {
    @Mapping(source = "user", target = "userDto")
    TraineeDto toTraineeDto(Trainee trainee);

    @Mapping(source = "userDto", target = "user")
    Trainee toTrainee(TraineeDto traineeDTO);

    TrainerDto toTrainerDto(Trainer trainer);

    Trainer toTrainer(TrainerDto trainerDTO);

    TrainingDto toTrainingDto(Training training);

    Training toTraining(TrainingDto trainingDTO);

    TrainingTypeDto toTrainingTypeDto(TrainingType trainingType);

    TrainingType toTrainingType(TrainingTypeDto trainingTypeDTO);

    UserDto toUserDto(User user);

    User toUser(UserDto userDTO);
}