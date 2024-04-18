import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LearnerTest {


    @Test
    void setEmergencyContactNumber() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String emergencyContactNumber = "07570123123";
        try {
            Learner learner = new Learner("name", "M", localDateNow, emergencyContactNumber, 1);
            assertNotNull(learner);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setEmergencyContactNumberWithCountryCode() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String emergencyContactNumber = "+447570123123";
        try {
            Learner learner = new Learner("name", "M", localDateNow, emergencyContactNumber, 1);
            assertNotNull(learner);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setEmergencyContactNumberWithInvalidNumber() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String emergencyContactNumber = "1231231";
        Learner learner = null;
        try {
            learner = new Learner("name", "M", localDateNow, emergencyContactNumber, 1);
            fail();
        }catch (Exception e){
            assertNull(learner);
        }
    }

    @Test
    void setGender() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String gender = "M";
        try {
            Learner learner = new Learner("name", gender, localDateNow, "07570123123", 1);
            assertTrue(learner.getGender().equalsIgnoreCase("Male"));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setGenderNumber() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String gender = "1";
        try {
            Learner learner = new Learner("name", gender, localDateNow, "07570123123", 1);
            assertTrue(learner.getGender().equalsIgnoreCase("male"));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setGenderMale() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String gender = "Male";
        try {
            Learner learner = new Learner("name", gender, localDateNow, "07570123123", 1);
            assertTrue(learner.getGender().equalsIgnoreCase("Male"));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setGenderAnythingElse() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        String gender = "2131231";
        try {
            Learner learner = new Learner("name", gender, localDateNow, "07570123123", 1);
            assertTrue(learner.getGender().equalsIgnoreCase("Female"));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setBirthDate() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.minusYears(5);
        try {
            Learner learner = new Learner("name", "M", localDateNow, "07570123123", 1);
            assertNotNull(learner);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    void setBirthUnbornChild() {
        LocalDate localDateNow = LocalDate.now();
        localDateNow = localDateNow.plusDays(1);
        Learner learner = null;
        try {
            learner = new Learner("name", "M", localDateNow, "07570123123", 1);
            fail();
        }catch (Exception e){
            assertNull(learner);
        }
    }
}