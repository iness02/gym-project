package com.example.GymProject.mapper;

import com.example.GymProject.dto.*;
import com.example.GymProject.dto.request.traineerRquest.UpdateTraineeProfileRequest;
import com.example.GymProject.dto.request.trainerRequest.UpdateTrainerProfileRequest;
import com.example.GymProject.dto.request.trainingRequest.AddTrainingRequest;
import com.example.GymProject.dto.response.GetTrainingResponse;
import com.example.GymProject.dto.response.traineeResponse.GetTraineeProfileResponse;
import com.example.GymProject.dto.response.traineeResponse.TrainerForTraineeResponse;
import com.example.GymProject.dto.response.traineeResponse.UnassignedTrainerResponse;
import com.example.GymProject.dto.response.traineeResponse.UpdateTraineeProfileResponse;
import com.example.GymProject.dto.response.trainerResponse.GetTrainerProfileResponse;
import com.example.GymProject.dto.response.trainerResponse.TraineeForTrainerResponse;
import com.example.GymProject.dto.response.trainerResponse.UpdateTrainerProfileResponse;
import com.example.GymProject.model.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EntityMapper {

    @Mapping(source = "userDto", target = "user")
    Trainee toTrainee(TraineeDto traineeDTO);

    default TraineeDto toTraineeDto(Trainee trainee) {
        if (trainee == null) {
            return null;
        }

        TraineeDto traineeDto = new TraineeDto();

        traineeDto.setUserDto(toUserDto(trainee.getUser()));
        traineeDto.setId(trainee.getId());
        traineeDto.setDateOfBirth(trainee.getDateOfBirth());
        traineeDto.setAddress(trainee.getAddress());
        traineeDto.setTrainers(trainerSetToTrainerDtoSet(trainee.getTrainers()));

        return traineeDto;
    }

    default Set<TrainerDto> trainerSetToTrainerDtoSet(Set<Trainer> trainers) {
        if (trainers == null) {
            return null;
        }

        return trainers.stream()
                .map(this::toTrainerDto)
                .collect(Collectors.toSet());
    }

 /*   default TrainerDto toTrainerDto(Trainer trainer) {
        if ( trainer == null ) {
            return null;
        }

        TrainerDto trainerDto = new TrainerDto();

        trainerDto.setId( trainer.getId() );
        trainerDto.setUserDto(toUserDto(trainer.getUser()));
        trainerDto.setSpecialization( trainer.getSpecialization() );
        trainerDto.setTrainees( traineeSetToTraineeDtoSet( trainer.getTrainees() ) );

        return trainerDto;
    }*/

    default TrainerDto toTrainerDto(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setId(trainer.getId());
        trainerDto.setSpecialization(trainer.getSpecialization());

        // Convert User to UserDto
        UserDto userDto = new UserDto();
        userDto.setId(trainer.getUser().getId());
        userDto.setFirstName(trainer.getUser().getFirstName());
        userDto.setLastName(trainer.getUser().getLastName());
        userDto.setUsername(trainer.getUser().getUsername());
        userDto.setPassword(trainer.getUser().getPassword());
        userDto.setIsActive(trainer.getUser().getIsActive());

        trainerDto.setUserDto(userDto);

        // Convert Set<Trainee> to Set<TraineeDto>
        Set<TraineeDto> traineeDtos = new HashSet<>();
        if (trainer.getTrainees() != null) {
            for (Trainee trainee : trainer.getTrainees()) {
                TraineeDto traineeDto = new TraineeDto();
                traineeDto.setId(trainee.getId());
                traineeDto.setDateOfBirth(trainee.getDateOfBirth());
                traineeDto.setAddress(trainee.getAddress());

                // Convert User to UserDto
                UserDto traineeUserDto = new UserDto();
                traineeUserDto.setId(trainee.getUser().getId());
                traineeUserDto.setFirstName(trainee.getUser().getFirstName());
                traineeUserDto.setLastName(trainee.getUser().getLastName());
                traineeUserDto.setUsername(trainee.getUser().getUsername());
                traineeUserDto.setPassword(trainee.getUser().getPassword());
                traineeUserDto.setIsActive(trainee.getUser().getIsActive());

                traineeDto.setUserDto(traineeUserDto);
                traineeDtos.add(traineeDto);
            }
        }
        trainerDto.setTrainees(traineeDtos);

        return trainerDto;
    }

    /*default Set<TraineeDto> traineeSetToTraineeDtoSet(Set<Trainee> trainees) {
        if (trainees == null) {
            return null;
        }

        return trainees.stream()
                .map(this::toTraineeDto)
                .collect(Collectors.toSet());
    }
*/

    default Trainer toTrainer(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }
        Trainer trainer = new Trainer();
        User user = new User();
        user.setId(trainerDto.getUserDto().getId());
        user.setPassword(trainerDto.getUserDto().getPassword());
        user.setUsername(trainerDto.getUserDto().getUsername());
        user.setIsActive(trainerDto.getUserDto().getIsActive());
        user.setFirstName(trainerDto.getUserDto().getFirstName());
        user.setLastName(trainerDto.getUserDto().getLastName());
        trainer.setId(trainerDto.getId());
        trainer.setUser(user);
        trainer.setSpecialization(trainerDto.getSpecialization());

        return trainer;
    }


    TrainingDto toTrainingDto(Training training);

    Training toTraining(TrainingDto trainingDTO);

    TrainingTypeDto toTrainingTypeDto(TrainingType trainingType);

    TrainingType toTrainingType(TrainingTypeDto trainingTypeDTO);

    UserDto toUserDto(User user);

    User toUser(UserDto userDTO);

    default GetTraineeProfileResponse toGetTraineeProfileResponse(TraineeDto traineeDto) {
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

    default TrainerForTraineeResponse toTrainerForTraineeResponse(TrainerDto trainerDto) {
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

    default TraineeDto toTraineeDto(UpdateTraineeProfileRequest request) {
        if (request == null) {
            return null;
        }
        UserDto userDto = new UserDto(null, request.getFirstName(), request.getLastName(),
                request.getUsername(), request.getPassword(), request.getIsActive());
        TraineeDto traineeDto = new TraineeDto(null, request.getDateOfBirth(), request.getAddress(), userDto, null);

        return traineeDto;

    }

    default TrainerDto toTrainerDao(UpdateTrainerProfileRequest request) {
        if (request == null) {
            return null;
        }
        UserDto userDto = new UserDto(null, request.getFirstName(), request.getLastName(),
                request.getUsername(), request.getPassword(), request.getIsActive());
        TrainerDto trainerDto = new TrainerDto(null, request.getSpecialization(), userDto, null);

        return trainerDto;

    }

    default UpdateTraineeProfileResponse toUpdateTraineeProfileResponse(TraineeDto traineeDto) {
        if (traineeDto == null) {
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

    default UpdateTrainerProfileResponse toUpdateTrainerProfileResponse(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        UpdateTrainerProfileResponse response = new UpdateTrainerProfileResponse();
        response.setUserName(trainerDto.getUserDto().getUsername());
        response.setFirstName(trainerDto.getUserDto().getFirstName());
        response.setLastName(trainerDto.getUserDto().getLastName());
        response.setSpecialization(trainerDto.getSpecialization());
        response.setIsActive(trainerDto.getUserDto().getIsActive());
        response.setTrainees(trainerDtoSetToTraineeForTrainerResponseSet(trainerDto.getTrainees()));
        return response;
    }

    default GetTrainingResponse toGetTrainingResponse(TrainingDto trainingDto) {
        return new GetTrainingResponse(
                trainingDto.getTrainingName(),
                trainingDto.getTrainingDate(),
                trainingDto.getTrainingType().getTrainingTypeName().toString(),
                trainingDto.getTrainingDuration(),
                trainingDto.getTrainer().getUserDto().getFirstName() + trainingDto.getTrainer().getUserDto().getLastName()
        );
    }

    default GetTrainerProfileResponse toGetTrainerProfileResponse(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        GetTrainerProfileResponse getTrainerProfileResponse = new GetTrainerProfileResponse();

        getTrainerProfileResponse.setFirstName(trainerDto.getUserDto().getFirstName());
        getTrainerProfileResponse.setLastName(trainerDto.getUserDto().getLastName());
        getTrainerProfileResponse.setIsActive(trainerDto.getUserDto().getIsActive());
        getTrainerProfileResponse.setTrainees(trainerDtoSetToTraineeForTrainerResponseSet(trainerDto.getTrainees()));

        return getTrainerProfileResponse;
    }

    default TraineeForTrainerResponse toTraineeForTrainerResponse(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeForTrainerResponse traineeForTrainerResponse = new TraineeForTrainerResponse();
        traineeForTrainerResponse.setUserName(traineeDto.getUserDto().getUsername());
        traineeForTrainerResponse.setFirstName(traineeDto.getUserDto().getFirstName());
        traineeForTrainerResponse.setLastName(traineeDto.getUserDto().getLastName());

        return traineeForTrainerResponse;
    }

    default Set<TraineeForTrainerResponse> trainerDtoSetToTraineeForTrainerResponseSet(Set<TraineeDto> set) {
        if (set == null) {
            return null;
        }

        Set<TraineeForTrainerResponse> set1 = new LinkedHashSet<>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TraineeDto traineeDto : set) {
            set1.add(toTraineeForTrainerResponse(traineeDto));
        }

        return set1;
    }

    default TrainingDto toTrainingDto(AddTrainingRequest request) {
        if (request == null) {
            return null;
        }

        TrainingDto trainingDto = new TrainingDto();
        UserDto userForTrainee = new UserDto();
        UserDto userForTrainer = new UserDto();
        TraineeDto traineeDto = new TraineeDto();
        TrainerDto trainerDto = new TrainerDto();

        userForTrainee.setUsername(request.getTraineeUsername());
        userForTrainer.setUsername(request.getTrainerUsername());
        traineeDto.setUserDto(userForTrainee);
        trainerDto.setUserDto(userForTrainer);
        trainingDto.setTrainee(traineeDto);
        trainingDto.setTrainer(trainerDto);
        trainingDto.setTrainingName(request.getName());
        trainingDto.setTrainingDate(request.getDate());
        trainingDto.setTrainingDuration(request.getDuration());

        return trainingDto;
    }

    default UnassignedTrainerResponse toUnassignedTrainerResponse(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }
        UnassignedTrainerResponse response = new UnassignedTrainerResponse();
        response.setUsername(trainerDto.getUserDto().getUsername());
        response.setLastname(trainerDto.getUserDto().getLastName());
        response.setFirstName(trainerDto.getUserDto().getFirstName());
        response.setSpecialization(trainerDto.getSpecialization());

        return response;
    }
}