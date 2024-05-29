package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    public boolean containsKey(String key) {
        return trainingMap.containsKey(key);
    }

    public void create(Training training) {
        Assert.notNull(training.getTraineeId(),"Trainee not found");
        Assert.notNull(training.getTrainerId(),"Trainer not found");
        trainingMap.put(training.getTrainingName(), training);
        logger.info("Inserted New Training");

    }

    public Training select(String key) {
        Training training;
        Assert.isTrue(trainingMap.containsKey(key),"Wrong Key, Training Not Found");
        logger.info("Training Found");
        training = trainingMap.get(key);

        return training;

    }

    public void delete(String key) {

        Assert.isTrue(trainingMap.containsKey(key),"Wrong Key, Training has not been Removed");
        trainingMap.remove(key);
        logger.info("Training Removed Successfully!");

    }

    @PostConstruct
    public void init() throws Exception {
        BufferedReader br = null;
        try {
            logger.info("Starting Populating Training Storage");
            File file = new File(trainingFilePath);
            br = new BufferedReader(new FileReader(file));
            String line;
            Training training;

            while ((line = br.readLine()) != null) {

                String[] trainingInfo = line.split(",");

                String traineeId = trainingInfo[0];
                Trainee trainee = traineeDao.select(traineeId);

                String trainerId = trainingInfo[1];
                Trainer trainer = trainerDao.select(trainerId);

                String trainingName = trainingInfo[2];
                TrainingType trainingType = TrainingType.valueOf(trainingInfo[3].toUpperCase());
                LocalDate trainingDate = LocalDate.parse(trainingInfo[4]);
                Integer trainingDuration = Integer.parseInt(trainingInfo[5]);

                training = new Training(trainee, trainer, trainingName,
                        trainingType, trainingDate, trainingDuration);
                trainingMap.put(trainingName, training);
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
        logger.info("Populating Training Storage Ended Successfully!");
    }


}
