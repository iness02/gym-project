package com.example.GymProject.util;


import com.example.GymProject.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
@PropertySource("classpath:application.properties")
public class Utils {
    private final static String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    private static int serialNumber = 0;
    private static StringBuilder sb;
    public static String generateUsername(String firstName, String lastName, boolean exists){
        sb = new StringBuilder();
        logger.info("Generating Username");
        sb.append(firstName).append(".").append(lastName);
        if(exists){
            logger.info("Appending Serial Number to the Username");
            sb.append(serialNumber);
            serialNumber++;
        }
        return sb.toString();
    }
    public static String generatePassword(){
        Random random = new Random();
        sb = new StringBuilder();
        for(int i = 0; i < 10; i++){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }
    public static boolean usernameExists(List<String> usernames, User user){
        String username = generateUsername(user.getFirstName(), user.getLastName(), false);
        return usernames.contains(username);
    }
}