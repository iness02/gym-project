package com.example.GymProject;


import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dto.*;
import com.example.GymProject.model.TrainingType;
import com.example.GymProject.model.Trainings;
import com.example.GymProject.service.TraineeService;
import com.example.GymProject.service.TrainerService;
import com.example.GymProject.service.TrainingService;
import com.example.GymProject.service.UserService;
import com.example.GymProject.util.Utils;
import jdk.jshell.execution.Util;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class GymProjectApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(context.getBean("trainerDao"));
/*
        UserService userService = (UserService) context.getBean("userService");
        TrainerService trainerService = (TrainerService) context.getBean("trainerService");
        TraineeService traineeService = (TraineeService) context.getBean("traineeService");
        TrainingService trainingService = (TrainingService) context.getBean("trainingService");*/

        //Creating trainer

       /* UserDto userDto = new UserDto("Nina", "Karapetyan");
        TrainerDto trainerDto = new TrainerDto(null, "yuga trainer", userDto);*/
        //trainerService.createTrainer(trainerDto);

        //Getting trainer by username
        //System.out.println(trainerService.getTrainerByUsername("Inesa.Hakobyan47","feKGQcQQgf"));

        //Updating trainer
     /*   userDto.setUsername("Inesa.Hakobyan47");
        userDto.setPassword("feKGQcQQgf");
        userDto.setIsActive(true);
        userDto.setFirstName("Inesa");
        userDto.setLastName("Hakobyan");
        userDto.setId(47L);
        TrainerDto trainerDto1 = new TrainerDto(24L, "yoga", userDto);*/

        //trainerService.updateTrainer(trainerDto1,"kkkddds");

        //Changing trainer password
        //traineeService.changeTraineePassword("Inesa.Hakobyan47","kkkddds","123456");

        //Deactivating trainer
        //trainerService.activateDeactivateTrainer("Inesa.Hakobyan47",false,"kkkddds");

        //Deleting trainer
        //trainerService.deleteTrainerByUsername("Inesa.Hakobyan55","SDJxzHzjJj");


        //Creating trainee
      /*  UserDto userDto1 = new UserDto("Mane", "Petrosyan");
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserDto(userDto1);
        traineeDto.setAddress("00005, 17, Long Avenue, Yerevan");*/
        //traineeService.createTrainee(traineeDto);

        //Getting trainee by username
        //System.out.println(traineeService.getTraineeByUsername("Mane.Petrosyan","NVikJfGBgW"));

        //updating trainee
       /* userDto1.setPassword("VaOhWDrCWE");
        userDto1.setUsername("Mane.Petrosyan3");
        userDto1.setIsActive(true);
        userDto1.setId(1L);
        Set<TrainerDto> trainerDtos = new HashSet<>();
        trainerDtos.add(trainerDto1);
        TraineeDto traineeDto1 = new TraineeDto(3L, null, "00006, 17, Long Avenue, Yerevan", userDto1, trainerDtos);*/
        // traineeService.updateTrainee(traineeDto1,"VaOhWDrCWE");
        //System.out.println(traineeService.getUnassignedTrainers("Mane.Petrosyan3", "VaOhWDrCWE"));
        //traineeService.updateTrainee(traineeDto1,"NVikJfGBgW");

        //changing trainee password
        //	traineeService.changeTraineePassword("Mane.Petrosyan","12345","NVikJfGBgW");

        //deleting trainee by username
        //traineeService.deleteTraineeByUsername("Mane.Petrosyan3", "VAScHCktYA");

        //Creating training
/*
        TrainingDto trainingDto = new TrainingDto();
        TrainerDto trainerDto2 = trainerService.getTrainerByUsername("Inesa.Hakobyan", "LyOxLtTQOm");
        TraineeDto traineeDto2 = traineeService.getTraineeByUsername("Mane.Petrosyan3", "VaOhWDrCWE");
        trainingDto.setTrainee(traineeDto2);
        trainingDto.setTrainingDate(LocalDate.of(2024, 9, 21));
        trainingDto.setTrainer(trainerDto2);
        trainingDto.setTrainingName("first training");
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto(1L, Trainings.CARDIO);
        trainingDto.setTrainingType(trainingTypeDto);
        trainingDto.setTrainingDuration(1200);*/
        // trainingService.addTraining(trainingDto);
        //System.out.println(trainingService.getAllTrainings());
        //trainingDto.setTrainingDuration(2500);
        //trainingDto.setId(1L);
        //trainingService.updateTraining(trainingDto,"Mane.Petrosyan3","VaOhWDrCWE");


    }

}
