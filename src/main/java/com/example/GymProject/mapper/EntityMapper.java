package com.example.GymProject.mapper;

import com.example.GymProject.dto.*;
import com.example.GymProject.model.*;
import com.example.GymProject.request.UpdateTraineeProfileRequest;
import com.example.GymProject.response.GetTraineeProfileResponse;
import com.example.GymProject.response.GetTrainingResponse;
import com.example.GymProject.response.TrainerForTraineeResponse;
import com.example.GymProject.response.UpdateTraineeProfileResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.LinkedHashSet;
import java.util.Set;

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

    default GetTraineeProfileResponse toGetTraineeProfileResponse(TraineeDto traineeDto){
        if (traineeDto == null) {
            return null;
        }

        GetTraineeProfileResponse getTraineeProfileResponse = new GetTraineeProfileResponse();

        getTraineeProfileResponse.setDateOfBirth(traineeDto.getDateOfBirth());
        getTraineeProfileResponse.setAddress(traineeDto.getAddress());
        getTraineeProfileResponse.setFirstName(traineeDto.getUserDto().getFirstName());
        getTraineeProfileResponse.setLastName(traineeDto.getUserDto().getLastName());
        getTraineeProfileResponse.setIsActive(traineeDto.getUserDto().getIsActive());
        getTraineeProfileResponse.setTrainers(trainerDtoSetToTrainerForTraineeResponseSet(traineeDto.getTrainers()));

        return getTraineeProfileResponse;
    }

    default TrainerForTraineeResponse toTrainerForTraineeResponse(TrainerDto trainerDto){
        if (trainerDto == null) {
            return null;
        }

        TrainerForTraineeResponse trainerForTraineeResponse = new TrainerForTraineeResponse();
        trainerForTraineeResponse.setUserName(trainerDto.getUserDto().getUsername());
        trainerForTraineeResponse.setFirstName(trainerDto.getUserDto().getFirstName());
        trainerForTraineeResponse.setLastName(trainerDto.getUserDto().getLastName());
        trainerForTraineeResponse.setSpecialization(trainerDto.getSpecialization());

        return trainerForTraineeResponse;
    }

    default Set<TrainerForTraineeResponse> trainerDtoSetToTrainerForTraineeResponseSet(Set<TrainerDto> set) {
        if (set == null) {
            return null;
        }

        Set<TrainerForTraineeResponse> set1 = new LinkedHashSet<>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TrainerDto trainerDto : set) {
            set1.add(toTrainerForTraineeResponse(trainerDto));
        }

        return set1;
    }

    default TraineeDto toTraineeDao(UpdateTraineeProfileRequest request){
        if (request == null){
            return  null;
        }
        UserDto userDto = new UserDto(null, request.getFirstName(),request.getLastName(),
                request.getUsername(), request.getPassword(),request.getIsActive());
        TraineeDto traineeDto = new TraineeDto(null,request.getDateOfBirth(),request.getAddress(),userDto,null);

        return traineeDto;

    }
    default UpdateTraineeProfileResponse toUpdateTraineeProfileResponse(TraineeDto traineeDto){
        if (traineeDto == null){
            return null;
        }

        UpdateTraineeProfileResponse response = new UpdateTraineeProfileResponse();
        response.setUserName(traineeDto.getUserDto().getUsername());
        response.setFirstName(traineeDto.getUserDto().getFirstName());
        response.setLastName(traineeDto.getUserDto().getLastName());
        response.setDateOfBirth(traineeDto.getDateOfBirth());
        response.setAddress(traineeDto.getAddress());
        response.setIsActive(traineeDto.getUserDto().getIsActive());
        response.setTrainers(trainerDtoSetToTrainerForTraineeResponseSet(traineeDto.getTrainers()));
        return response;
    }

    default GetTrainingResponse toGetTrainingResponse(TrainingDto trainingDto) {
        return new GetTrainingResponse(
                trainingDto.getTrainingName(),
                trainingDto.getTrainingDate(),
                trainingDto.getTrainingType().toString(),
                trainingDto.getTrainingDuration(),
                trainingDto.getTrainer().getUserDto().getFirstName() +trainingDto.getTrainer().getUserDto().getLastName()
        );
    }
}