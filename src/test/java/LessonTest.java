import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LessonTest {

    @Test
    void hasAvailableSpot() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        int maxNumberOfLearners = lesson.getMaxNumberOfLearners();
        if(maxNumberOfLearners == 0){
            assertFalse(lesson.hasAvailableSpot());
        }
        for(int i = 0 ; i < maxNumberOfLearners - 1; i++){
            lesson.getListOfLearners().add(String.valueOf(i));
        }
        assertTrue(lesson.hasAvailableSpot());
    }

    @Test
    void hasAvailableSpotInvalid() {
        Lesson lesson = new Lesson(LocalDate.now(), 1, LocalTime.now(), LocalTime.now().plusHours(1), "Coach Name");
        int maxNumberOfLearners = lesson.getMaxNumberOfLearners();
        if(maxNumberOfLearners == 0){
            assertFalse(lesson.hasAvailableSpot());
        }
        for(int i = 0 ; i < maxNumberOfLearners; i++){
            lesson.getListOfLearners().add(String.valueOf(i));
        }
        assertFalse(lesson.hasAvailableSpot());
    }
}