import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LearnerControllerTest {
    LearnerController learnerController;
    @BeforeEach
    void setUp() {
        learnerController = new LearnerController();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getLearnerByID() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner learner = new Learner("name", "M", localDateNow, "07570123123", 1);
            learnerController.addNewLearner(learner);
            Learner learnerQuery = learnerController.getLearnerByID(learner.getId());
            assertEquals(learner, learnerQuery);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getLearnerByIDNotExisting() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner learner = new Learner("name", "M", localDateNow, "07570123123", 1);
            Learner secondLearner = new Learner("Tak Ota", "M", localDateNow, "07570123123", 2);
            learnerController.addNewLearner(learner);
            Learner learnerQuery = learnerController.getLearnerByID(secondLearner.getId());
            assertNotEquals(secondLearner, learnerQuery);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getListOfLearnersByLearnerID() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            Learner secondLearner = new Learner("Tak Ota", "M", localDateNow, "07570123123", 2);
            learnerController.addNewLearner(firstLearner);
            learnerController.addNewLearner(secondLearner);
            List<Learner> listOfLearner = learnerController.getListOfLearnersByLearnerID(firstLearner.getLearnerId());
            assertEquals(2, listOfLearner.size());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getListOfLearnersByLearnerIDWithDifferentIDs() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            Learner secondLearner = new Learner("Aakt Taa", "M", localDateNow, "07570123123", 2);
            learnerController.addNewLearner(firstLearner);
            learnerController.addNewLearner(secondLearner);
            List<Learner> listOfLearner = learnerController.getListOfLearnersByLearnerID(firstLearner.getLearnerId());
            assertEquals(1, listOfLearner.size());
            assertFalse(listOfLearner.contains(secondLearner));
            assertTrue(listOfLearner.contains(firstLearner));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void addNewLearner() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            int firstCount = learnerController.getListOfLearners().size();
            learnerController.addNewLearner(firstLearner);
            int secondCount = learnerController.getListOfLearners().size();
            assertEquals(firstCount + 1, secondCount);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void addNewLearnerDuplicate() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            int firstCount = learnerController.getListOfLearners().size();
            learnerController.addNewLearner(firstLearner);
            int secondCount = learnerController.getListOfLearners().size();
            learnerController.addNewLearner(firstLearner);
            int thirdCount = learnerController.getListOfLearners().size();
            assertEquals(thirdCount, secondCount);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void removeLearner() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            learnerController.addNewLearner(firstLearner);
            int firstCount = learnerController.getListOfLearners().size();
            learnerController.removeLearner(firstLearner);
            int secondCount = learnerController.getListOfLearners().size();
            assertEquals(firstCount - 1, secondCount);
        }catch (Exception e){
            fail();
        }
    }


    @Test
    void removeLearnerNotExisting() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            Learner secondLearner = new Learner("Tak Ota", "M", localDateNow, "07570123123", 2);
            learnerController.addNewLearner(firstLearner);
            int firstCount = learnerController.getListOfLearners().size();
            learnerController.removeLearner(secondLearner);
            int secondCount = learnerController.getListOfLearners().size();
            assertEquals(firstCount, secondCount);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getListOfLearners() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner firstLearner = new Learner("Tarek Omran", "M", localDateNow, "07570123123", 1);
            Learner secondLearner = new Learner("Tak Ota", "M", localDateNow, "07570123123", 2);
            ArrayList<Learner> listOfLearners = new ArrayList<>();
            listOfLearners.add(firstLearner);
            listOfLearners.add(secondLearner);
            learnerController.setArrayOfLearners(listOfLearners);
            assertEquals(listOfLearners, learnerController.getListOfLearners());
        }catch (Exception e){
            fail();
        }
    }
}