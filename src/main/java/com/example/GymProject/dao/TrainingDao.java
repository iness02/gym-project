/*
package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.Trainings;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TrainingDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Map<String, Training> trainingMap = new HashMap<>();
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    @Value("${trainingFilePath}")
    private String trainingFilePath;
    private final ResourceLoader resourceLoader;
    private BufferedReader br;


    public TrainingDao(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public void setBufferedReader(BufferedReader br) {
        this.br = br;
    }

    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    protected boolean containsKey(String key) {
        return trainingMap.containsKey(key);
    }

    public void create(Training training) {
        Assert.notNull(training, "Training cannot be null");
        Assert.notNull(training.getTraineeId(), "Trainee not found");
        Assert.notNull(training.getTrainerId(), "Trainer not found");
        trainingMap.put(training.getTrainingName(), training);
        logger.info("Inserted new training");

    }

    public Training select(String userId) {
        Training training;
        Assert.isTrue(trainingMap.containsKey(userId), "Wrong key, Training not found");
        logger.info("Training found");
        training = trainingMap.get(userId);
        return training;
    }

    public void delete(String userId) {
        Assert.isTrue(trainingMap.containsKey(userId), "Wrong key, Training has not been removed");
        trainingMap.remove(userId);
        logger.info("Training removed successfully!");
    }

    @PostConstruct
    public void init() throws Exception {
        try {
            logger.info("Starting populating training storage");
            Resource resource = resourceLoader.getResource(trainingFilePath);
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            Training training;

            while ((line = br.readLine()) != null) {

                String[] trainingInfo = line.split(",");

                String traineeId = trainingInfo[0];
                Trainee trainee = traineeDao.select(traineeId);

                String trainerId = trainingInfo[1];
                Trainer trainer = trainerDao.select(trainerId);

                String trainingName = trainingInfo[2];
                Trainings trainingType = Trainings.valueOf(trainingInfo[3].toUpperCase());
                LocalDate trainingDate = LocalDate.parse(trainingInfo[4]);
                Integer trainingDuration = Integer.parseInt(trainingInfo[5]);

                training = new Training(trainee, trainer, trainingName,
                        trainingType, trainingDate, trainingDuration);
                trainingMap.put(trainingName, training);
            }
        } catch (Exception e) {
            logger.error("File can not be found!");
            throw new FileNotFoundException("Wrong file");
        } finally {
            if (br != null) {
                logger.info("Closing Buffered Reader");
                br.close();
            }
        }
        logger.info("Populating training storage ended successfully!");
    }
}
*/
