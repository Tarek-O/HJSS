import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Controller {

    private HashMap<String, Lesson> mapOfLessons;
    private ArrayList<Learner> arrayOfLearners;

    Controller(){
         mapOfLessons = new HashMap<String, Lesson>();
         arrayOfLearners = new ArrayList<Learner>();
    }

    public void linkLearnerToLesson(Learner inputLearner, Lesson inputLesson){
        addNewLearner(inputLearner);
        addNewLesson(inputLesson);
        mapOfLessons.get(inputLesson.getId()).addLearner(inputLearner);
    }

    /**
     * Returns an Arraylist of all keys that contain the parameter.
     * @param search
     * @return
     */
    public ArrayList<String> getFamiliarKeys(String search){
        ArrayList<String> listOfKeys = new ArrayList<String>();
        for(String input : mapOfLessons.keySet()){
            if(input.contains(search)){
                listOfKeys.add(input);
            }
        }
        return listOfKeys;
    }

    /**
     * Checks and only inserts the new lesson ONLY if it does not exist prior.
     * @param newLesson
     * @return
     */
    public boolean addNewLesson(Lesson newLesson){
        if(!mapOfLessons.containsKey(newLesson.getId())){
            mapOfLessons.put(newLesson.getId(), newLesson);
            return true;
        }
        return false;
    }

    /**
     * Adds new lesson or modifies previously added lesson.
     * @param newLesson
     */
    public void addOrModifyLesson(Lesson newLesson){
        mapOfLessons.put(newLesson.getId(), newLesson);
    }

    /**
     * Removes a Lesson if it exists.
     * @param remLesson
     */
    public void removeLesson(Lesson remLesson){
        mapOfLessons.remove(remLesson.getId());
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

    public HashMap<String, Lesson> getMapOfLessons() {
        return mapOfLessons;
    }

    public void setMapOfLessons(HashMap<String, Lesson> mapOfLessons) {
        this.mapOfLessons = mapOfLessons;
    }

    public ArrayList<Learner> getArrayOfLearners() {
        return arrayOfLearners;
    }

    public void setArrayOfLearners(ArrayList<Learner> arrayOfLearners) {
        this.arrayOfLearners = arrayOfLearners;
    }
}
