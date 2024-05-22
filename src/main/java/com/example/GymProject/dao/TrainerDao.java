package com.example.GymProject.dao;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TrainerDao {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<String, Trainer> trainerMap = new HashMap<>();
    @Value("${trainerFilePath}")
    private String trainerFilePath;
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }

    public void create(Trainer trainer) {
        try {
            if (trainer.getUserId() == null) {
                logger.warning("Trainer userId is null");
                throw new IllegalArgumentException("UserId cannot be null");
            }
            trainerMap.put(trainer.getUserId(), trainer);
            logger.info("Inserted New Trainer");
        } catch (Exception e){
            logger.severe("Error While Inserting New Value");
            throw new IllegalArgumentException(e);
        }
    }

    public Trainer select(String key) {
        Trainer trainer;
        try {
            if (trainerMap.containsKey(key)) {
                logger.info("Trainer found");
                trainer = trainerMap.get(key);
            } else {
                logger.warning("Wrong Key, Trainer not Found");
                throw new IllegalArgumentException("Trainer Not Found");
            }
            return trainer;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void update(String key, Trainer trainer) {
        try {
            if (key == null) {
                logger.warning("Trainer userId is null");
                throw new IllegalArgumentException("UserId cannot be null");
            }
            trainerMap.put(key, trainer);
            logger.info("Trainer Updated");
        } catch (Exception e){
            logger.warning("Trainer has not been Updated");
            throw new IllegalArgumentException(e);
        }
    }
    public void delete(String key){
        try {
            if (trainerMap.containsKey(key)){
                trainerMap.remove(key);
                logger.info("Trainer Removed Successfully!");
            }else {
                logger.warning("Trainer has not been Removed");
                throw new IllegalArgumentException("Wrong Key");
            }
        } catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public boolean containsKey(String key) {
        return trainerMap.containsKey(key);
    }

    @PostConstruct
    public void init() throws Exception {
        BufferedReader br = null;
        try {
            logger.info("Starting Populating Trainer Storage");
            File file = new File(trainerFilePath);
            br = new BufferedReader(new FileReader(file));
            String line;
            Trainer trainer;

            while ((line = br.readLine()) != null) {

                String[] trainerInfo = line.split(",");

                String firstName = trainerInfo[0];
                String lastName = trainerInfo[1];
                String username = trainerInfo[2];
                String password = trainerInfo[3];
                Boolean isActive = Boolean.parseBoolean(trainerInfo[4]);
                TrainingType specialization = TrainingType.valueOf(trainerInfo[5].toUpperCase());
                String userId = trainerInfo[6];

                trainer = new Trainer(firstName, lastName, username, password,
                        isActive, specialization, userId);

                trainerMap.put(userId, trainer);
            }
        } catch (Exception e) {
            logger.warning("File can not be found!");
            throw new FileNotFoundException("Wrong File");
        } finally {

            if (br != null) {
                logger.info("Closing Buffered Reader");
                br.close();
            }
        }
        logger.info("Populating Trainer Storage Ended Successfully!");
    }
}
