package com.example.GymProject.model;

import io.micrometer.common.lang.Nullable;

import java.time.LocalDate;

public class Training {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;

    public Training(Trainee trainee, Trainer trainer, String trainingName,
                    @Nullable TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        this.traineeId = trainee.getUserId();
        this.trainerId = trainer.getUserId();
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Training{");
        sb.append("traineeId='").append(traineeId).append('\'');
        sb.append(", trainerId='").append(trainerId).append('\'');
        sb.append(", trainingName='").append(trainingName).append('\'');
        sb.append(", trainingType=").append(trainingType);
        sb.append(", trainingDate=").append(trainingDate);
        sb.append(", trainingDuration=").append(trainingDuration);
        sb.append('}');
        return sb.toString();
    }
}