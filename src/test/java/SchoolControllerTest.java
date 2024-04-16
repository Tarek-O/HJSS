import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class SchoolControllerTest {

    SchoolController schoolController;

    @BeforeEach
    void setUp() {
        schoolController = new SchoolController();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void bookLearnerToLesson() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        Learner learner = null;
        try {
            schoolController.addNewLesson(lesson);
            learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson);
            assertTrue(schoolController.getListOfLearners().contains(learner));
            assertTrue(schoolController.getMapOfLessons().containsValue(lesson));
            assertTrue(lesson.getListOfLearners().contains(learner.getId()));
        }catch (Exception e){
            fail();
        }

        Lesson lesson2 = new Lesson(LocalDate.now(), 2, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        Learner learner2 = null;
        try {
            schoolController.addNewLesson(lesson);
            learner2 = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner2, lesson2);
            assertTrue(schoolController.getListOfLearners().contains(learner2));
            assertTrue(schoolController.getMapOfLessons().containsValue(lesson2));
            assertTrue(lesson2.getListOfLearners().contains(learner2.getId()));
        }catch (Exception e){}

        try{
            Learner learnerTemp = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 2);
            Lesson lessonTemp = new Lesson(LocalDate.now().plusDays(1), 4, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
            schoolController.bookLearnerToLesson(learnerTemp, lessonTemp);
            fail();
        }catch (Exception e){
        }

        try{
            Learner learnerTemp = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 2);
            Lesson lessonTemp2 = new Lesson(LocalDate.now().plusDays(1), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
            schoolController.bookLearnerToLesson(learnerTemp, lessonTemp2);
            fail();
        }catch (Exception e){}
    }

    @Test
    void attendLesson() {
        Lesson lesson1 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            schoolController.addNewLesson(lesson1);
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson1);
            schoolController.attendLesson(lesson1.getId(), learner.getId(), "test comment", 6);
            fail();
        }catch (Exception e){}

        Lesson lesson2 = new Lesson(LocalDate.now().plusDays(2), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            schoolController.addNewLesson(lesson2);
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson2);
            schoolController.attendLesson(lesson2.getId(), learner.getId(), "test comment", -1);
            fail();
        }catch (Exception e){}

        Lesson lesson3 = new Lesson(LocalDate.now().plusDays(2), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            schoolController.addNewLesson(lesson3);
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson3);
            schoolController.attendLesson(lesson3.getId(), learner.getId(), "test comment", 2);
            assertEquals(1, schoolController.getLastEventOfLearner(lesson3.getId(), learner.getId()).getStatus());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void modifyBooking() {
        Lesson lesson1 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        Lesson lesson2 = new Lesson(LocalDate.now().plusDays(2), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            schoolController.addNewLesson(lesson1);
            schoolController.addNewLesson(lesson2);
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson1);
            schoolController.modifyBooking(learner.getId(), lesson1, lesson2);
            assertEquals(-1, schoolController.getLastEventOfLearner(lesson1.getId(), learner.getId()).getStatus());
            assertEquals(0, schoolController.getLastEventOfLearner(lesson2.getId(), learner.getId()).getStatus());
        }catch (Exception e){}
    }

    @Test
    void incrementLearnerGrade() {
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.incrementLearnerGrade(learner);
            assertEquals(2 ,learner.getGradeLevel());
            Learner learner2 = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 5);
            schoolController.incrementLearnerGrade(learner2);
            assertEquals(5 ,learner2.getGradeLevel());
            Learner learner3 = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", -5);
            schoolController.incrementLearnerGrade(learner3);
            assertEquals(1 ,learner3.getGradeLevel());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void revalidateLearnerBookings() {
        Lesson lesson1 = new Lesson(LocalDate.now(), 2, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        Lesson lesson2 = new Lesson(LocalDate.now().plusDays(2), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            schoolController.addNewLesson(lesson1);
            schoolController.addNewLesson(lesson2);
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            schoolController.bookLearnerToLesson(learner, lesson1);
            schoolController.bookLearnerToLesson(learner, lesson2);
            schoolController.incrementLearnerGrade(learner);
            schoolController.revalidateLearnerBookings(learner);
            assertEquals(0, schoolController.getLastEventOfLearner(lesson1.getId(), learner.getId()).getStatus());
            assertEquals(-1, schoolController.getLastEventOfLearner(lesson2.getId(), learner.getId()).getStatus());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void isLearnerAgeValid() {
        try{
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            assertTrue(schoolController.isLearnerAgeValid(learner, LocalDate.now()));
            Learner learner2 = new Learner("name", "M", LocalDate.now(), "+447570123123", 1);
            assertFalse(schoolController.isLearnerAgeValid(learner2, LocalDate.now()));
            Learner learner3 = new Learner("name", "M", LocalDate.now().minusYears(15), "+447570123123", 1);
            assertFalse(schoolController.isLearnerAgeValid(learner3, LocalDate.now()));
        }catch (Exception e){
            fail();
        }
    }
}