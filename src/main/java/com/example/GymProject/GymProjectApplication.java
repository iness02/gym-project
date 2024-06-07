package com.example.GymProject;


import com.example.GymProject.config.AppConfig;
import com.example.GymProject.dto.TraineeDto;
import com.example.GymProject.dto.UserDto;
import com.example.GymProject.service.TraineeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class GymProjectApplication {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(context.getBean("trainerDao"));
		TraineeService traineeService = (TraineeService) context.getBean("traineeService");
		TraineeDto traineeDTO = new TraineeDto();
		traineeDTO.setUserDTO(new UserDto(null, "Andrei", "Pushkin", "apushkin", "123", true));
		traineeDTO.setAddress("00005, 17, Long Avenue, Yerevan");
		TraineeDto trainee = traineeService.createTrainee(traineeDTO);
		System.out.println(trainee);
	}

}
