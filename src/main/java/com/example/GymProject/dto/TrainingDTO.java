package com.example.GymProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    private Long id;
    private TraineeDTO trainee;
    private TrainerDTO trainer;
    @NotNull
    private String trainingName;
    @NotNull
    private TrainingTypeDTO trainingType;
    @NotNull
    private Date trainingDate;
    @NotNull
    private Integer trainingDuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TraineeDTO getTrainee() {
        return trainee;
    }

    public void setTrainee(TraineeDTO trainee) {
        this.trainee = trainee;
    }

    public TrainerDTO getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerDTO trainer) {
        this.trainer = trainer;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingTypeDTO getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingTypeDTO trainingType) {
        this.trainingType = trainingType;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
}