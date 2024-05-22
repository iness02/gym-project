package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TraineeDao {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Value("${traineeFilePath}")
    private String traineeFilePath;
    private final Map<String, Trainee> traineeMap = new HashMap<>();

    public List<Trainee> findAll(){
        return new ArrayList<>(traineeMap.values());
    }

    public void create(Trainee trainee){
        try {
            if (trainee.getUserId() == null) {
                logger.warning("Trainee userId is null");
                throw new IllegalArgumentException("UserId cannot be null");
            }
            traineeMap.put(trainee.getUserId(), trainee);
            logger.info("Inserted New Trainee");
        } catch (Exception e){
          logger.severe("Error While Inserting New Value");
            throw new IllegalArgumentException(e);
        }
    }

    public Trainee select(String key){
        Trainee trainee;
        try {
            if(traineeMap.containsKey(key)){
                logger.info("Trainee found");
                trainee = traineeMap.get(key);
            }else{
               logger.warning("Wrong Key, Trainee not Found");
                throw new IllegalArgumentException("Trainee Not Found");
            }
            return trainee;
        } catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public void update(String key, Trainee trainee){
        try {
            if (key == null) {
               logger.warning("Trainee userId is null");
                throw new IllegalArgumentException("UserId cannot be null");
            }
            traineeMap.put(key, trainee);
            logger.info("Trainee Updated");
        } catch (Exception e){
          logger.warning("Trainee has not been Updated");
            throw new IllegalArgumentException(e);
        }
    }
    public void delete(String key){
        try {
            if (traineeMap.containsKey(key)){
                traineeMap.remove(key);
                logger.info("Trainee Removed Successfully!");
            }else {
               logger.warning("Trainee has not been Removed");
                throw new IllegalArgumentException("Wrong Key");
            }
        } catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public boolean containsKey(String key) {
        return traineeMap.containsKey(key);
    }

    @PostConstruct
    public void init() throws Exception {
        BufferedReader br = null;
        try {
          logger.info("Starting Populating Trainee Storage");
            File file = new File(traineeFilePath);
            br = new BufferedReader(new FileReader(file));
            String line;
            Trainee trainee;

            while ((line = br.readLine()) != null){

                String[] traineeInfo = line.split(",");

                String firstName = traineeInfo[0];
                String lastName = traineeInfo[1];
                String username = traineeInfo[2];
                String password = traineeInfo[3];
                Boolean isActive = Boolean.parseBoolean(traineeInfo[4]);
                LocalDate dob = LocalDate.parse(traineeInfo[5]);
                String address = traineeInfo[6];
                String userId = traineeInfo[7];

                trainee = new Trainee(firstName,lastName,username,password,
                        isActive,dob,address,userId);

                traineeMap.put(userId, trainee);
            }
        } catch (Exception e){
           logger.warning("File can not be found!");
            throw new FileNotFoundException("Wrong File");
        } finally {
            if (br != null){
            logger.info("Closing Buffered Reader");
                br.close();
            }
        }
        logger.info("Populating Trainee Storage Ended Successfully!");
    }
}

