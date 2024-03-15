import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class LearnerController {

    private ArrayList<Learner> arrayOfLearners;

    LearnerController(){
        arrayOfLearners = new ArrayList<Learner>();
    }

    /**
     * Search for Learner based on their ID
     * @param learnerID The ID of the suspected Learner
     * @return The object of the Learner found or null of the learner was not found
     */
    public Learner findLearnerByID(String learnerID){
        for(Learner le : arrayOfLearners){
            if(le.getId().trim().equals(learnerID)) return le;
        }
        return null;
    }

    /**
     * Add a new Learner and return true if it did not exist.
     * @param newLearner
     * @return
     */
    public boolean addNewLearner(Learner newLearner){
        if(!arrayOfLearners.contains(newLearner)){
            arrayOfLearners.add(newLearner);
            return true;
        }
        return false;
    }

    /**
     * Removes learner from list.
     * @param remLearner
     */
    public void removeLearner(Learner remLearner){
        arrayOfLearners.remove(remLearner);
    }

    /**
     * The age of the learner of when they will potentially attend an event at a specific date.
     *
     * @param dateThen The date you want to validate
     * @return Age as a double
     */
    public boolean isLearnerAgeValid(Learner learner, LocalDate dateThen){
        double age = getLearnerAgeWhen(learner, dateThen);
        if(age >= 4 && age <= 11){
            return true;
        }
        return false;
    }

    public Learner getLearnerByID(String learnerID){
        try{
            for(Learner le : arrayOfLearners){
                if(le.getId().equals(learnerID)) return le;
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public double getLearnerAge(Learner learner){
        return Period.between(learner.getBirthDate(), LocalDate.now()).getYears();
    }

    public double getLearnerAgeWhen(Learner learner, LocalDate dateThen){
        return Period.between(learner.getBirthDate(), dateThen).getYears();
    }

    public void incrementLearnerGrade(Learner learner){
        if(learner.getGradeLevel() == 5){
            return;
        }
        learner.setGradeLevel(learner.getGradeLevel() + 1);
    }

    public ArrayList<Learner> getArrayOfLearners() {
        return arrayOfLearners;
    }

    public void setArrayOfLearners(ArrayList<Learner> arrayOfLearners) {
        this.arrayOfLearners = arrayOfLearners;
    }
}
