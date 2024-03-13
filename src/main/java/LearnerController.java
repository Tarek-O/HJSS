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


    public ArrayList<Learner> getArrayOfLearners() {
        return arrayOfLearners;
    }

    public void setArrayOfLearners(ArrayList<Learner> arrayOfLearners) {
        this.arrayOfLearners = arrayOfLearners;
    }
}
