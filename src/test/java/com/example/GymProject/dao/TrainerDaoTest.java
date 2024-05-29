package com.example.GymProject.dao;


import com.example.GymProject.config.AppConfig;
import com.example.GymProject.model.Trainer;
import com.example.GymProject.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TrainerDaoTest {
    @Autowired
    private TrainerDao trainerDao;

    @BeforeEach
    void deleteDataFromDao(){
        for(Trainer trainer : trainerDao.findAll()){
            if(trainerDao.containsKey(trainer.getUserId())){
                trainerDao.delete(trainer.getUserId());
            }
        }
    }
    @Test
    public void containsTrainerTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        trainerDao.create(trainer);

        assertTrue(trainerDao.containsKey("inesa123"));
    }
    @Test
   public void selectTrainerTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        trainerDao.create(trainer);

        Trainer result = trainerDao.select("inesa123");
        assertNotNull(result);
        assertEquals(trainer, result);
    }
    @Test
  public  void selectNonExistedTrainerFailTest(){
        assertThrows(IllegalArgumentException.class, () -> trainerDao.select("test"));
    }
    @Test
   public void selectAllTrainersTest(){
        Trainer trainer1 = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        Trainer trainer2 = new Trainer("Nune", "Karapetyan", true,
                TrainingType.FITNESS, "nune123");

        trainerDao.create(trainer1);
        trainerDao.create(trainer2);

        assertEquals(2, trainerDao.findAll().size());
    }
    @Test
    public void createTrainerTest(){
        Trainer trainer1 = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerDao.create(trainer1);

        Trainer trainer2 = trainerDao.select("inesa123");

        assertEquals(trainer1.getFirstName(), trainer2.getFirstName());
        assertEquals(trainer1.getLastName(), trainer2.getLastName());
        assertEquals(trainer1.getActive(), trainer2.getActive());
        assertEquals(trainer1.getUserId(), trainer2.getUserId());
    }
    @Test
   public void createTrainerWithNullUserIDFailTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, null);

        assertThrows(IllegalArgumentException.class, () -> trainerDao.create(trainer));
    }
    @Test
   public void deleteTrainerTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");

        trainerDao.create(trainer);

        trainerDao.delete("inesa123");

        assertEquals(0, trainerDao.findAll().size());
    }
    @Test
   public void deleteNonExistedTrainerFailTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        trainerDao.create(trainer);

        assertThrows(IllegalArgumentException.class, () -> trainerDao.delete("inesa1234"));
    }

    @Test
    public void updateTrainerTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, "inesa123");
        Trainer newTrainer = new Trainer("Mane", "Avagyan", true,
                TrainingType.FITNESS, "inesa123");

        trainerDao.create(trainer);
        trainerDao.update("inesa123", newTrainer);

        assertNotEquals(trainer.getFirstName(), trainerDao.select("inesa123").getFirstName());
        assertNotEquals(trainer.getLastName(), trainerDao.select("inesa123").getLastName());
        assertEquals(trainer.getSpecialization(), trainerDao.select("inesa123").getSpecialization());
    }
    @Test
   public void updateNonExistedTrainerFailTest(){
        Trainer trainer = new Trainer("Inesa", "Hakobyan", true,
                TrainingType.FITNESS, null);

        assertThrows(IllegalArgumentException.class, () -> trainerDao.update(trainer.getUserId(), trainer));
    }


}