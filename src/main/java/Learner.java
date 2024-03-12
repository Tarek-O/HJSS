import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

public class Learner {

    private String id;
    private String name, emergencyContactNumber;
    private boolean gender;
    private int gradeLevel;
    private LocalDate birthDate;

    //Format of the UK Phone Number using Regex. Accepts country code as well.
    private final String phoneNumberRegex = "^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$";

    public Learner(){};
    public Learner(String name, boolean gender, LocalDate birthDate, String emergencyContactNumber, int gradeLevel) throws Exception {
        setName(name);
        setGender(gender);
        setBirthDate(birthDate);
        setEmergencyContactNumber(emergencyContactNumber);
        setGradeLevel(gradeLevel);
        setId();
    }

    public String generateLearnerID(){
        LocalDate ldNow = LocalDate.now();
        return getName().substring(0,3).toUpperCase()+ "_" + ldNow.getYear() + String.format("%02d",ldNow.getMonthValue()) + String.format("%02d", ldNow.getDayOfMonth());
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

    /**
     * True for male. False for female.
     * @param gender
     */
    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() { return birthDate; }

    public void setBirthDate(LocalDate dateOfBirth) {
        this.birthDate = dateOfBirth;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    /**
     * Formatting the string into a UK Mobile Phone syntax
     * @param emergencyContactNumber
     * @throws Exception for parsing the string into number format
     */
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

    /**
     * The age of the learner as of the moment the method is called.
     *
     * @param
     * @return
     */
    public int calculateAgeNow(){
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * The age of the learner of when they will potentially attend an event at a specific date.
     *
      * @param dateThen
     * @return age as a double
     */
    public int calculateAgeWhen(LocalDate dateThen){
        return Period.between(birthDate, dateThen).getYears();
    }

    public String getId() {
        return id;
    }

    public void setId() {
        if(getId() == null){
            this.id = generateLearnerID();
        }
    }

    public boolean isGender() {
        return gender;
    }


}
