package com.example.GymProject.service;

import com.example.GymProject.dao.MemoryStorage;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.example.GymProject.util.Utils.generateUsername;
import static com.example.GymProject.util.Utils.usernameExists;

@Service
public class TrainerService {
    private MemoryStorage memoryStorage;

    @Autowired
    public void setMemoryStorage(MemoryStorage memoryStorage) {
        this.memoryStorage = memoryStorage;
    }

    public void createTrainer(String firstName, String lastName, Boolean isActive,
                              TrainingType specialization, String userId) {
        Trainer trainer = new Trainer(firstName, lastName, isActive, specialization, userId);
        boolean contains = usernameExists(memoryStorage.getUsernames(), trainer);
        trainer.setUsername(generateUsername(trainer.getFirstName(), trainer.getLastName(), contains));
        memoryStorage.getTrainerRepository().create(trainer);
    }

    public Trainer selectTrainer(String key) {
        return memoryStorage.getTrainerRepository().select(key);
    }

    public void updateTrainer(String key, Trainer newTrainer) {
        Trainer trainer = new Trainer();
        if (!memoryStorage.getTrainerRepository().containsKey(key)) {
            throw new NoSuchElementException("Wrong Key, Update Failed!");
        }
        boolean contains = usernameExists(memoryStorage.getUsernames(), newTrainer);

        trainer.setUsername(generateUsername(newTrainer.getFirstName(), newTrainer.getLastName(), contains));
        trainer.setPassword(selectTrainer(key).getPassword());
        trainer.setFirstName(newTrainer.getFirstName());
        trainer.setLastName(newTrainer.getLastName());
        trainer.setSpecialization(newTrainer.getSpecialization());
        trainer.setActive(newTrainer.getActive());

        memoryStorage.getTrainerRepository().update(key, trainer);
    }

}
