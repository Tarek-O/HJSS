import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class LearnerController {

    private List<Learner> arrayOfLearners;

    LearnerController(){
        arrayOfLearners = new ArrayList<Learner>();
    }

    /**
     * Search for Learner based on their ID
     * @param id The ID of the expected Learner
     * @return The object of the Learner found or null of the learner was not found
     */
    public Learner getLearnerByID(String id){
        try{
            for(Learner le : arrayOfLearners){
                if(le.getId().equals(id)) return le;
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Learner> getListOfLearnersByLearnerID(String learnerID){
        List<Learner> listOfLearners = new ArrayList<>();
        for(Learner le : arrayOfLearners){
            if(le.getLearnerId().trim().equals(learnerID)) listOfLearners.add(le);
        }
        return listOfLearners;
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

    public List<Learner> getListOfLearners() {
        return arrayOfLearners;
    }

    public void setArrayOfLearners(ArrayList<Learner> arrayOfLearners) {
        this.arrayOfLearners = arrayOfLearners;
    }
}
