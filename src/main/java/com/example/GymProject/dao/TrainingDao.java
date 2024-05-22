package com.example.GymProject.dao;

import com.example.GymProject.model.Trainee;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.Training;
import com.example.GymProject.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TrainingDao {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
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
        try {
            if (training.getTraineeId() == null || training.getTrainerId() == null) {
                logger.warning("Trainee/Trainer Not Found");
                throw new IllegalArgumentException("Trainee/Trainer Not Found");
            }
            trainingMap.put(training.getTrainingName(), training);
            logger.info("Inserted New Training");
        } catch (Exception e) {
            logger.warning("Error While Inserting Value");
            throw new IllegalArgumentException("Wrong Value");
        }
    }

    public Training select(String key) {
        Training training;
        try {
            if (trainingMap.containsKey(key)) {
                logger.info("Training Found");
                training = trainingMap.get(key);
            } else {
                logger.warning("Wrong Key, Training Not Found");
                throw new IllegalArgumentException("Training Not Found");
            }
            return training;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void delete(String key) {
        try {
            if (trainingMap.containsKey(key)) {
                trainingMap.remove(key);
                logger.info("Training Removed Successfully!");
            } else {
                logger.warning("Training has not been Removed");
                throw new IllegalArgumentException("Wrong Key");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
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
            logger.warning("File can not be found!");
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
