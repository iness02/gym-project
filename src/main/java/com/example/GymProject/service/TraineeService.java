package com.example.GymProject.service;

import com.example.GymProject.dao.MemoryStorage;
import com.example.GymProject.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static com.example.GymProject.util.Utils.generateUsername;
import static com.example.GymProject.util.Utils.usernameExists;

@Service
public class TraineeService {
    private MemoryStorage memoryStorage;

    @Autowired
    public void setInMemoryStorage(MemoryStorage memoryStorage) {
        this.memoryStorage = memoryStorage;
    }

    public void createTrainee(String firstName, String lastName, Boolean isActive,
                              LocalDate dob, String address, String userId) {
        Trainee trainee = new Trainee(firstName, lastName, isActive, dob, address, userId);
        boolean contains = usernameExists(memoryStorage.getUsernames(), trainee);
        trainee.setUsername(generateUsername(trainee.getFirstName(), trainee.getLastName(), contains));
        memoryStorage.getTraineeRepository().create(trainee);
    }

    public Trainee selectTrainee(String key) {
        return memoryStorage.getTraineeRepository().select(key);
    }

    public void updateTrainee(String key, Trainee newTrainee) {
        Trainee trainee = new Trainee();
        if (!memoryStorage.getTraineeRepository().containsKey(key)) {
            throw new NoSuchElementException("Wrong Key, Update Failed!");
        }
        boolean contains = usernameExists(memoryStorage.getUsernames(), newTrainee);

        trainee.setUsername(generateUsername(newTrainee.getFirstName(), newTrainee.getLastName(), contains));
        trainee.setPassword(selectTrainee(key).getPassword());
        trainee.setFirstName(newTrainee.getFirstName());
        trainee.setLastName(newTrainee.getLastName());
        trainee.setAddress(newTrainee.getAddress());
        trainee.setDob(newTrainee.getDob());
        trainee.setActive(newTrainee.getActive());

        memoryStorage.getTraineeRepository().update(key, trainee);
    }

    public void deleteTrainee(String key) {
        memoryStorage.getTraineeRepository().delete(key);
    }
}