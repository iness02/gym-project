package com.example.GymProject.mapper;

import com.example.GymProject.dto.*;
import com.example.GymProject.dto.request.*;
import com.example.GymProject.dto.respone.*;
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

    default Trainee toTrainee(TraineeRegistrationRequestDto registrationRequestDto) {
        if (registrationRequestDto == null) {
            return null;
        }

        Trainee trainee = new Trainee();

        User user = new User();
        user.setFirstName(registrationRequestDto.getFirstName());
        user.setLastName(registrationRequestDto.getLastName());
        trainee.setUser(user);
        trainee.setDateOfBirth(registrationRequestDto.getDateOfBirth());
        trainee.setAddress(registrationRequestDto.getAddress());
        return trainee;
    }

    default Trainee toTrainee(UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        if (updateTraineeProfileRequestDto == null) {
            return null;
        }

        Trainee trainee = new Trainee();

        User user = new User();
        user.setFirstName(updateTraineeProfileRequestDto.getFirstName());
        user.setLastName(updateTraineeProfileRequestDto.getLastName());
        user.setUsername(updateTraineeProfileRequestDto.getUsername());
        user.setPassword(updateTraineeProfileRequestDto.getPassword());
        user.setIsActive(updateTraineeProfileRequestDto.getIsActive());
        trainee.setUser(user);
        trainee.setDateOfBirth(updateTraineeProfileRequestDto.getDateOfBirth());
        trainee.setAddress(updateTraineeProfileRequestDto.getAddress());
        return trainee;
    }

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

    default TrainerDto toTrainerDto(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setId(trainer.getId());
        trainerDto.setSpecialization(trainer.getSpecialization());

        UserDto userDto = new UserDto();
        userDto.setId(trainer.getUser().getId());
        userDto.setFirstName(trainer.getUser().getFirstName());
        userDto.setLastName(trainer.getUser().getLastName());
        userDto.setUsername(trainer.getUser().getUsername());
        userDto.setPassword(trainer.getUser().getPassword());
        userDto.setIsActive(trainer.getUser().getIsActive());

        trainerDto.setUserDto(userDto);

        Set<TraineeDto> traineeDtos = new HashSet<>();
        if (trainer.getTrainees() != null) {
            for (Trainee trainee : trainer.getTrainees()) {
                TraineeDto traineeDto = new TraineeDto();
                traineeDto.setId(trainee.getId());
                traineeDto.setDateOfBirth(trainee.getDateOfBirth());
                traineeDto.setAddress(trainee.getAddress());

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

    default Trainer toTrainer(TrainerRegistrationRequestDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName(trainerDto.getFirstName());
        user.setLastName(trainerDto.getLastName());
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

    default TraineeProfileResponseDto toTraineeProfileResponseDto(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeProfileResponseDto getTraineeProfileResponse = new TraineeProfileResponseDto();

        getTraineeProfileResponse.setDateOfBirth(traineeDto.getDateOfBirth());
        getTraineeProfileResponse.setAddress(traineeDto.getAddress());
        getTraineeProfileResponse.setFirstName(traineeDto.getUserDto().getFirstName());
        getTraineeProfileResponse.setLastName(traineeDto.getUserDto().getLastName());
        getTraineeProfileResponse.setIsActive(traineeDto.getUserDto().getIsActive());
        getTraineeProfileResponse.setTrainers(trainerDtoSetToTrainerForTraineeResponseDtoSet(traineeDto.getTrainers()));

        return getTraineeProfileResponse;
    }

    default TrainerForTraineeResponseDto toTrainerForTraineeResponseDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        TrainerForTraineeResponseDto trainerForTraineeResponse = new TrainerForTraineeResponseDto();
        trainerForTraineeResponse.setUserName(trainerDto.getUserDto().getUsername());
        trainerForTraineeResponse.setFirstName(trainerDto.getUserDto().getFirstName());
        trainerForTraineeResponse.setLastName(trainerDto.getUserDto().getLastName());
        trainerForTraineeResponse.setSpecialization(trainerDto.getSpecialization());

        return trainerForTraineeResponse;
    }

    default Set<TrainerForTraineeResponseDto> trainerDtoSetToTrainerForTraineeResponseDtoSet(Set<TrainerDto> set) {
        if (set == null) {
            return null;
        }

        Set<TrainerForTraineeResponseDto> set1 = new LinkedHashSet<>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TrainerDto trainerDto : set) {
            set1.add(toTrainerForTraineeResponseDto(trainerDto));
        }

        return set1;
    }

    default TraineeDto toTraineeDto(UpdateTraineeProfileRequestDto request) {
        if (request == null) {
            return null;
        }
        UserDto userDto = new UserDto(null, request.getFirstName(), request.getLastName(),
                request.getUsername(), request.getPassword(), request.getIsActive());
        TraineeDto traineeDto = new TraineeDto(null, request.getDateOfBirth(), request.getAddress(), userDto, null);

        return traineeDto;

    }

    default TrainerDto toTrainerDao(UpdateTrainerProfileRequestDto request) {
        if (request == null) {
            return null;
        }
        UserDto userDto = new UserDto(null, request.getFirstName(), request.getLastName(),
                request.getUsername(), request.getPassword(), request.getIsActive());
        TrainerDto trainerDto = new TrainerDto(null, request.getSpecialization(), userDto, null);

        return trainerDto;

    }

    default UpdateTraineeProfileResponseDto toUpdateTraineeProfileResponseDto(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        UpdateTraineeProfileResponseDto response = new UpdateTraineeProfileResponseDto();
        response.setUserName(traineeDto.getUserDto().getUsername());
        response.setFirstName(traineeDto.getUserDto().getFirstName());
        response.setLastName(traineeDto.getUserDto().getLastName());
        response.setDateOfBirth(traineeDto.getDateOfBirth());
        response.setAddress(traineeDto.getAddress());
        response.setIsActive(traineeDto.getUserDto().getIsActive());
        response.setTrainers(trainerDtoSetToTrainerForTraineeResponseDtoSet(traineeDto.getTrainers()));
        return response;
    }

    default UpdateTrainerProfileResponseDto toUpdateTrainerProfileResponseDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        UpdateTrainerProfileResponseDto response = new UpdateTrainerProfileResponseDto();
        response.setUserName(trainerDto.getUserDto().getUsername());
        response.setFirstName(trainerDto.getUserDto().getFirstName());
        response.setLastName(trainerDto.getUserDto().getLastName());
        response.setSpecialization(trainerDto.getSpecialization());
        response.setIsActive(trainerDto.getUserDto().getIsActive());
        response.setTrainees(trainerDtoSetToTraineeForTrainerResponseDtoSet(trainerDto.getTrainees()));
        return response;
    }

    default UpdateTrainerProfileResponseDto toUpdateTrainerProfileResponseDto(Trainer trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        UpdateTrainerProfileResponseDto response = new UpdateTrainerProfileResponseDto();
        response.setUserName(trainerDto.getUser().getUsername());
        response.setFirstName(trainerDto.getUser().getFirstName());
        response.setLastName(trainerDto.getUser().getLastName());
        response.setSpecialization(trainerDto.getSpecialization());
        response.setIsActive(trainerDto.getUser().getIsActive());
        response.setTrainees(trainerDtoSet(trainerDto.getTrainees()));
        return response;
    }

    default GetTrainingResponseDto toGetTrainingResponseDto(TrainingDto trainingDto) {
        return new GetTrainingResponseDto(
                trainingDto.getTrainingName(),
                trainingDto.getTrainingDate(),
                trainingDto.getTrainingType().getTrainingTypeName().toString(),
                trainingDto.getTrainingDuration(),
                trainingDto.getTrainer().getUserDto().getFirstName() + trainingDto.getTrainer().getUserDto().getLastName()
        );
    }

    default TrainerProfileResponseDto toTrainerProfileResponseDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }

        TrainerProfileResponseDto getTrainerProfileResponse = new TrainerProfileResponseDto();

        getTrainerProfileResponse.setFirstName(trainerDto.getUserDto().getFirstName());
        getTrainerProfileResponse.setLastName(trainerDto.getUserDto().getLastName());
        getTrainerProfileResponse.setIsActive(trainerDto.getUserDto().getIsActive());
        getTrainerProfileResponse.setTrainees(trainerDtoSetToTraineeForTrainerResponseDtoSet(trainerDto.getTrainees()));

        return getTrainerProfileResponse;
    }

    default TraineeForTrainerResponseDto toTraineeForTrainerResponseDto(TraineeDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeForTrainerResponseDto traineeForTrainerResponse = new TraineeForTrainerResponseDto();
        traineeForTrainerResponse.setUserName(traineeDto.getUserDto().getUsername());
        traineeForTrainerResponse.setFirstName(traineeDto.getUserDto().getFirstName());
        traineeForTrainerResponse.setLastName(traineeDto.getUserDto().getLastName());

        return traineeForTrainerResponse;
    }

    default TraineeForTrainerResponseDto toTraineeForTrainerResponseDto(Trainee traineeDto) {
        if (traineeDto == null) {
            return null;
        }

        TraineeForTrainerResponseDto traineeForTrainerResponse = new TraineeForTrainerResponseDto();
        traineeForTrainerResponse.setUserName(traineeDto.getUser().getUsername());
        traineeForTrainerResponse.setFirstName(traineeDto.getUser().getFirstName());
        traineeForTrainerResponse.setLastName(traineeDto.getUser().getLastName());

        return traineeForTrainerResponse;
    }

    default Set<TraineeForTrainerResponseDto> trainerDtoSetToTraineeForTrainerResponseDtoSet(Set<TraineeDto> set) {
        if (set == null) {
            return null;
        }

        Set<TraineeForTrainerResponseDto> set1 = new LinkedHashSet<>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (TraineeDto traineeDto : set) {
            set1.add(toTraineeForTrainerResponseDto(traineeDto));
        }

        return set1;
    }


    default Set<TraineeForTrainerResponseDto> trainerDtoSet(Set<Trainee> set) {
        if (set == null) {
            return null;
        }

        Set<TraineeForTrainerResponseDto> set1 = new LinkedHashSet<>(Math.max((int) (set.size() / .75f) + 1, 16));
        for (Trainee traineeDto : set) {
            set1.add(toTraineeForTrainerResponseDto(traineeDto));
        }

        return set1;
    }

    default TrainingDto toTrainingDto(AddTrainingRequestDto request) {
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

    default UnassignedTrainerResponseDto toUnassignedTrainerResponseDto(TrainerDto trainerDto) {
        if (trainerDto == null) {
            return null;
        }
        UnassignedTrainerResponseDto response = new UnassignedTrainerResponseDto();
        response.setUsername(trainerDto.getUserDto().getUsername());
        response.setLastname(trainerDto.getUserDto().getLastName());
        response.setFirstName(trainerDto.getUserDto().getFirstName());
        response.setSpecialization(trainerDto.getSpecialization());

        return response;
    }


}
