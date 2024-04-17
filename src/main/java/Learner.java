import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Learner {

    private String id, learnerId;;
    private String name, emergencyContactNumber;
    private boolean gender;
    private int gradeLevel;
    private LocalDate birthDate;

    //Format of the UK Phone Number using Regex. Accepts country code as well.
    private final String phoneNumberRegex = "^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$";

    public Learner(String name, String gender, LocalDate birthDate, String emergencyContactNumber, int gradeLevel) throws Exception {
        setName(name);
        setGender(gender);
        setBirthDate(birthDate);
        setEmergencyContactNumber(emergencyContactNumber);
        setGradeLevel(gradeLevel);
        setId();
        setLearnerId();
    }

    /**
     * Formatting the string into a UK Mobile Phone syntax
     * @param emergencyContactNumber
     * @throws Exception for parsing the string into number format
     */
    public void  setEmergencyContactNumber(String emergencyContactNumber) throws Exception {
        //If the Phone Number matches the UK Format then add it to the number
        if(!emergencyContactNumber.startsWith("0") && !emergencyContactNumber.startsWith("+")) emergencyContactNumber = "0" + emergencyContactNumber;
        if(emergencyContactNumber.matches(phoneNumberRegex)){
            this.emergencyContactNumber = emergencyContactNumber;
        }else{
            throw new Exception("Phone number entered is not a valid UK Phone Number.");
        }
    }

    public String generateLearnerID(){
        if(getName().isEmpty() || getName().isBlank()) return "INVALID_ID";
        String[] nameSplit = getName().trim().toUpperCase().split(" ");
        String nameGen;
        if(nameSplit.length == 1) {
            if(nameSplit[0].length() > 1)
            nameGen = nameSplit[0].substring(0,2);
            else nameGen = nameSplit[0].charAt(0) + "X";
        }else{
            nameGen = String.valueOf(nameSplit[0].charAt(0)) + String.valueOf(nameSplit[nameSplit.length-1].charAt(0));
        }
        return nameGen + (getBirthDate().getYear()%100) + String.format("%02d",getBirthDate().getMonthValue()) + String.format("%02d", getBirthDate().getDayOfMonth());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        if(gender){
            return "Male";
        }
        return "Female";
    }

    /**
     * True for male. False for female.
     * @param gender
     */
    public void setGender(String gender) {
        if(gender.toUpperCase().equals("MALE") || gender.toUpperCase().equals("M") || gender.equals("1")){
            this.gender = true;
        }else this.gender = false;
    }


    public LocalDate getBirthDate() { return birthDate; }

    public void setBirthDate(LocalDate dateOfBirth) throws Exception {
        if(dateOfBirth.isAfter(LocalDate.now())) throw new Exception("Learner birth date can not be in the future");
        this.birthDate = dateOfBirth;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = String.valueOf(UUID.randomUUID());;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public void setLearnerId() {
        this.learnerId = generateLearnerID();
    }

    public String printLearner(){
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "ID: " + getLearnerId() + ",  Name: " + getName() + ", Gender: " + getGender() + ", Grade: " + getGradeLevel() +", Date of Birth: " + getBirthDate().format(inputDateFormatter) + ", Emergency Phone Number: " + getEmergencyContactNumber() + ".";
    }
}
