package com.example.GymProject;

import com.example.GymProject.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
class GymProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
