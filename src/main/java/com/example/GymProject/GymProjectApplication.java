package com.example.GymProject;


import com.example.GymProject.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class GymProjectApplication {

	public static void main(String[] args) {

		new AnnotationConfigApplicationContext(AppConfig.class);
	}

}
