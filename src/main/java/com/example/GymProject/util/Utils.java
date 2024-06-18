package com.example.GymProject.util;


import com.example.GymProject.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@PropertySource("classpath:application.properties")
public class Utils {
    private final static String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Logger logger = LoggerFactory.getLogger(Utils.class.getName());
    private static StringBuilder sb;
    private static final Set<String> existingUsernames = new HashSet<>();

    public static String generatePassword() {
        Random random = new Random();
        sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

}