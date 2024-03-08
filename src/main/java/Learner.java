import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

public class Learner {

    private String name, emergencyContactNumber;
    private boolean gender;
    private int gradeLevel;
    private Date birthDate;

    //Format of the UK Phone Number using Regex. Accepts country code as well.
    private final String phoneNumberRegex = "^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$";

    public Learner(){};
    public Learner(String name, boolean gender, Date birthDate, String emergencyContactNumber, int gradeLevel) throws Exception {
        setName(name);
        setGender(gender);
        setBirthDate(birthDate);
        setEmergencyContactNumber(emergencyContactNumber);
        setGradeLevel(gradeLevel);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthDate() { return birthDate; }

    public void setBirthDate(Date dateOfBirth) throws Exception{
        //Age should be between 4 to 11 requirement
        this.birthDate = dateOfBirth;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void  setEmergencyContactNumber(String emergencyContactNumber) throws Exception {
        //If the Phone Number matches the UK Format then add it to the number
       if(emergencyContactNumber.matches(phoneNumberRegex)){
            this.emergencyContactNumber = emergencyContactNumber;
       }else{
           throw new Exception("Phone number enetered is not a valid UK Phone Number.");
       }
    }


    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }


    public double calculateAge(){
        Date dateNow = new Date();
        return (double) ((dateNow.getTime() - getBirthDate().getTime()) / 31540000) / 1000;
    }
}
