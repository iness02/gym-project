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
    private static Set<String> existingUsernames = new HashSet<>();

    public static String generateUsername(String firstName, String lastName, boolean exists) {
        sb = new StringBuilder();
        logger.info("Generating username");
        sb.append(firstName).append(".").append(lastName);

        String baseUsername = sb.toString();
        String newUsername = baseUsername;

        if (exists) {
            logger.info("Appending serial number to the username");
            if (!existingUsernames.contains(newUsername + "0")) {
                newUsername += "0";
            } else {
                int maxSerialNumber = 0;
                for (String username : existingUsernames) {
                    if (username.startsWith(baseUsername) && username.length() > baseUsername.length()) {
                        String suffix = username.substring(baseUsername.length());
                        try {
                            int number = Integer.parseInt(suffix);
                            if (number > maxSerialNumber) {
                                maxSerialNumber = number;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore non-numeric suffix
                        }
                    }
                }
                newUsername += (maxSerialNumber + 1);
            }
        }

        existingUsernames.add(newUsername);
        return newUsername;
    }

    public static String generatePassword() {
        Random random = new Random();
        sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static boolean usernameExists(List<String> usernames, User user) {
        String username = generateUsername(user.getFirstName(), user.getLastName(), false);
        return usernames.contains(username);
    }
}