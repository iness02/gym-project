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


    }

}
