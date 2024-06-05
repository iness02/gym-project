package com.example.GymProject.dao;

import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Map<String, Trainer> trainerMap = new HashMap<>();
    @Value("${trainerFilePath}")
    private String trainerFilePath;
    private final ResourceLoader resourceLoader;
    private BufferedReader br;


    public TrainerDao(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }

    public void setBufferedReader(BufferedReader br) {
        this.br = br;
    }

    public void create(Trainer trainer) {
        Assert.notNull(trainer, "Trainer cannot be null");
        Assert.notNull(trainer.getUserId(), "Trainer userId is null");
        trainerMap.put(trainer.getUserId(), trainer);
        logger.info("Inserted new trainer");
    }

    public Trainer select(String userId) {
        Trainer trainer;
        Assert.isTrue(trainerMap.containsKey(userId), "Wrong key, Trainer not found");
        logger.info("Trainer found");
        trainer = trainerMap.get(userId);
        return trainer;
    }

    public void update(String userId, Trainer trainer) {
        Assert.notNull(trainer, "Trainer cannot be null");
        Assert.notNull(userId, "Trainer userId is null");
        Assert.notNull(trainer, "The trainer parameter cannot be null");
        trainerMap.put(userId, trainer);
        logger.info("Trainer updated");
    }

    public void delete(String userId) {
        Assert.notNull(userId, "UserId cannot be null");
        Assert.isTrue(trainerMap.containsKey(userId), "Wrong key");
        trainerMap.remove(userId);
        logger.info("Trainer removed successfully!");
    }

    public boolean containsKey(String key) {
        return trainerMap.containsKey(key);
    }

    @PostConstruct
    public void init() throws Exception {
        try {
            logger.info("Starting populating trainer storage");
            Resource resource = resourceLoader.getResource(trainerFilePath);
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
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
            logger.error("File cannot be found!");
            throw new FileNotFoundException("Wrong file");
        } finally {
            if (br != null) {
                logger.info("Closing Buffered Reader");
                br.close();
            }
        }
        logger.info("Populating trainer storage ended successfully!");
    }
}
