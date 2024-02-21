public class Learner {

    private String name, gender, emergencyContactNumber;
    private int age, gradeLevel;

    //Format of the UK Phone Number using Regex. Accepts country code as well.
    private final String phoneNumberRegex = "^(((\\+44\\s?\\d{4}|\\(?0\\d{4}\\)?)\\s?\\d{3}\\s?\\d{3})|((\\+44\\s?\\d{3}|\\(?0\\d{3}\\)?)\\s?\\d{3}\\s?\\d{4})|((\\+44\\s?\\d{2}|\\(?0\\d{2}\\)?)\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$";

    public Learner(){};
    public Learner(String name, String gender, int age, String emergencyContactNumber, int gradeLevel) throws Exception {
        setName(name);
        setGender(gender);
        setAge(age);
        setEmergencyContactNumber(emergencyContactNumber);
        setGradeLevel(gradeLevel);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws Exception {
        //Age should be between 4 to 11 requirement
        if(age >= 4 && age <= 11){
            this.age = age;
        }else{
            throw new Exception("The age should be between 4 to 11 years old only.");
        }
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

}
