import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LessonControllerTest {

    LessonController lessonController;

    @BeforeEach
    void setUp() {
        lessonController = new LessonController();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getFamiliarKeys() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        Lesson lesson2 = new Lesson(LocalDate.now().plusDays(1), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson2);
        List<String> listOfIds = lessonController.getFamiliarKeys("1GR");
        assertEquals(2, listOfIds.size());
        assertTrue(listOfIds.containsAll(List.of(lesson.getId(), lesson2.getId())));
    }

    @Test
    void addNewLesson() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        assertTrue(lessonController.getMapOfLessons().containsValue(lesson));
        assertTrue(lessonController.getMapOfLessons().containsKey(lesson.getId()));
        Lesson lesson2 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2");
        lessonController.addNewLesson(lesson2);
        assertFalse(lessonController.getMapOfLessons().containsValue(lesson2));
        assertTrue(lessonController.getMapOfLessons().containsKey(lesson2.getId()));
    }

    @Test
    void addOrModifyLesson() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        assertTrue(lessonController.getMapOfLessons().containsValue(lesson));
        assertTrue(lessonController.getMapOfLessons().containsKey(lesson.getId()));
        Lesson lesson2 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2");
        lessonController.addOrModifyLesson(lesson2);
        assertFalse(lessonController.getMapOfLessons().containsValue(lesson));
        assertTrue(lessonController.getMapOfLessons().containsValue(lesson2));
        assertTrue(lessonController.getMapOfLessons().containsKey(lesson2.getId()));
    }

    @Test
    void removeLesson() {
        try {
            lessonController.removeLesson("TEST DOES NOT EXIST");
            fail();
        }catch (Exception e){}
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        try {
            lessonController.removeLesson(lesson.getId());
            assertFalse(lessonController.getMapOfLessons().containsValue(lesson));
        }catch (Exception e){
            fail();
        }
        Lesson lesson2 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson2);
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson2.getId(), learner.getId(), lesson2.getCoachName());
            lessonController.attendLesson("attend", lesson2, learner.getId(), "test comment", 5, lesson2.getCoachName());
            lessonController.removeLesson(lesson.getId());
            assertTrue(lessonController.getMapOfLessons().containsValue(lesson2));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getLessonByID() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        try {
            Lesson lesson2 = lessonController.getLessonByID(lesson.getId());
            assertEquals(lesson2, lesson);
        }catch (Exception e){
            fail();
        }

        try {
            lessonController.getLessonByID("ID DOES NOT EXIST");
            fail();
        }catch (Exception e){}
    }

    @Test
    void getLessonByDate() {
        LocalDate localDate = LocalDate.of(2024, 4, 22);
        Lesson lesson = new Lesson(localDate, 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        Lesson lesson2 = lessonController.getLessonByDate(localDate);
        assertEquals(lesson, lesson2);
    }

    @Test
    void bookLesson() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        Learner learner = null;
        try {
            learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson.getId(), learner.getId(), lesson.getCoachName());
            assertTrue(
                    lessonController.hasLearnerBookedLesson(lesson.getId(), learner.getId()));
        }catch (Exception e){
            fail();
        }

        try {
            lessonController.bookLesson("testKey2131", lesson.getId(), learner.getId(), lesson.getCoachName());
            fail();
        }catch (Exception e){
        }

        Lesson lesson2 = new Lesson(LocalDate.now(), 4, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        try {
            for(int i = 0 ; i < lesson2.getMaxNumberOfLearners(); i++) {
                Learner learnerTemp = new Learner("name" + i, "M", LocalDate.now().minusYears(5), "+447570123123", 4);
                lessonController.bookLesson("testKey", lesson2.getId(), learnerTemp.getId(), lesson2.getCoachName());
            }
            try{
                Learner learnerTemp = new Learner("nameTemp", "M", LocalDate.now().minusYears(5), "+447570123123", 4);
                lessonController.bookLesson("testKey", lesson2.getId(), learnerTemp.getId(), lesson2.getCoachName());
                fail();
            }catch (Exception e){}
        }catch (Exception e){

        }
    }

    @Test
    void cancelLesson() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson);
        Learner learner = null;
        try {
            learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson.getId(), learner.getId(), lesson.getCoachName());
            lessonController.cancelLesson("TEST", lesson, learner.getId(), lesson.getCoachName());
            try{
                lessonController.hasLearnerBookedLesson(lesson.getId(), learner.getId());
                fail();
            }catch (Exception e){}
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void attendLesson() {
        Lesson lesson1 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson1);
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson1.getId(), learner.getId(), lesson1.getCoachName());
            lessonController.attendLesson("attend", lesson1, learner.getId(), "test comment", 5, lesson1.getCoachName());
            LessonEvent lessonEvent = lessonController.getLastEventOfLearner(lesson1.getId(), learner.getId());
            assertEquals(1, lessonEvent.getStatus());
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void getListOfLessonsByCoach() {
        lessonController.addNewLesson(new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name"));
        lessonController.addNewLesson(new Lesson(LocalDate.now(), 2, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name"));
        lessonController.addNewLesson(new Lesson(LocalDate.now(), 3, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2"));
        lessonController.addNewLesson(new Lesson(LocalDate.now(), 4, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2"));
        lessonController.addNewLesson(new Lesson(LocalDate.now(), 5, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name3"));
        lessonController.addNewLesson(new Lesson(LocalDate.now().plusDays(1), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name"));
        lessonController.addNewLesson(new Lesson(LocalDate.now().plusDays(1), 2, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name"));
        lessonController.addNewLesson(new Lesson(LocalDate.now().plusDays(1), 3, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2"));
        lessonController.addNewLesson(new Lesson(LocalDate.now().plusDays(1), 4, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name2"));
        lessonController.addNewLesson(new Lesson(LocalDate.now().plusDays(1), 5, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name3"));
        List<Lesson> listOfLesson = lessonController.getListOfLessonsByCoach("Coach Name");
        assertEquals(4, listOfLesson.size());
        for(int i = 0 ; i < listOfLesson.size(); i++){
            assertEquals("Coach Name", listOfLesson.get(i).getCoachName());
        }
    }

    @Test
    void hasLearnerBookedLesson() {
        Lesson lesson1 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson1);
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson1.getId(), learner.getId(), lesson1.getCoachName());
            assertTrue(
                    lessonController.hasLearnerBookedLesson(lesson1.getId(), learner.getId()));
        }catch (Exception e){
            fail();
        }
        lesson1.setListOfLearners(new ArrayList<>());
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.hasLearnerBookedLesson(lesson1.getId(), learner.getId());
            fail();
        }catch (Exception e){
        }
    }

    @Test
    void hasLearnerAttendedLesson() {
        Lesson lesson2 = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        lessonController.addNewLesson(lesson2);
        try {
            Learner learner = new Learner("name", "M", LocalDate.now().minusYears(5), "+447570123123", 1);
            lessonController.bookLesson("testKey", lesson2.getId(), learner.getId(), lesson2.getCoachName());
            lessonController.attendLesson("attend", lesson2, learner.getId(), "test comment", 5, lesson2.getCoachName());
            assertTrue(lessonController.hasLearnerAttendedLesson(lesson2.getId(), learner.getId()));
            assertTrue(lessonController.hasLessonBeenAttended(lesson2));
        }catch (Exception e){
            fail();
        }
    }
}