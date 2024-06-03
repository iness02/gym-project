package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class TraineeDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Value("${traineeFilePath}")
    private String traineeFilePath;
    private final Map<String, Trainee> traineeMap = new HashMap<>();

    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }
    private final ResourceLoader resourceLoader;

    public TraineeDao(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void create(Trainee trainee) {

        Assert.notNull(trainee.getUserId(), "UserId cannot be null");
        traineeMap.put(trainee.getUserId(), trainee);
        logger.info("Inserted New Trainee");

    }

    public Trainee select(String key) {

        Trainee trainee;
        Assert.isTrue(traineeMap.containsKey(key), "Trainee Not Found");
        logger.info("Trainee found");
        trainee = traineeMap.get(key);

        return trainee;

    }

    public void update(String key, Trainee trainee) {
        Assert.notNull(key,"UserId cannot be null");
        Assert.notNull(trainee,"The trainee parameter cannot be null");
        traineeMap.put(key, trainee);
        logger.info("Trainee Updated");

    }

    public void delete(String key) {
        Assert.notNull(key,"UserId cannot be null");
        Assert.isTrue(traineeMap.containsKey(key), "Wrong Key");
        traineeMap.remove(key);
        logger.info("Trainee Removed Successfully!");

    }

    public boolean containsKey(String key) {
        return traineeMap.containsKey(key);
    }

    @PostConstruct
    public void init() throws Exception {
        BufferedReader br = null;
        try {
            logger.info("Starting Populating Trainee Storage");
            Resource resource = resourceLoader.getResource(traineeFilePath);
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            Trainee trainee;

            while ((line = br.readLine()) != null) {

                String[] traineeInfo = line.split(",");

                String firstName = traineeInfo[0];
                String lastName = traineeInfo[1];
                String username = traineeInfo[2];
                String password = traineeInfo[3];
                Boolean isActive = Boolean.parseBoolean(traineeInfo[4]);
                LocalDate dob = LocalDate.parse(traineeInfo[5]);
                String address = traineeInfo[6];
                String userId = traineeInfo[7];

                trainee = new Trainee(firstName, lastName, username, password,
                        isActive, dob, address, userId);

                traineeMap.put(userId, trainee);
            }
        } catch (Exception e) {
            logger.error("File can not be found!");
            throw new FileNotFoundException("Wrong File");
        } finally {
            if (br != null) {
                logger.info("Closing Buffered Reader");
                br.close();
            }
        }
        logger.info("Populating Trainee Storage Ended Successfully!");
    }
}

