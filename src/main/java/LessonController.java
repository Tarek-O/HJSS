import java.util.ArrayList;
import java.util.HashMap;

public class LessonController{

    private HashMap<String, Lesson> mapOfLessons;

    LessonController(){
        mapOfLessons = new HashMap<String, Lesson>();
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

    public Lesson getLessonByID(String ID){
        return getMapOfLessons().get(ID);
    }

    /**
     * If the Lesson has capacity available it will append the Learner's ID as one of its Learners. Then it adds the action in its logs.
     * @param inputLearnerID The ID of the Learner
     */
    public void bookLesson(Lesson lesson, String inputLearnerID) throws Exception {
        if(lesson.getListOfLearners().contains(inputLearnerID)) throw new Exception("The learner has already booked this lesson!");

        if(!lesson.hasAvailableSpot()) throw new Exception("The lesson is fully booked! You can not book a spot.");

        lesson.getListOfLearners().add(inputLearnerID);
        lesson.getLogOfActions().add(new LessonHistory(inputLearnerID, 0));
    }

    public void cancelLesson(Lesson lesson, String learnerID){
        if(lesson.getListOfLearners().isEmpty()) {
            return;
        }
        lesson.getListOfLearners().remove(learnerID);
        lesson.getLogOfActions().add(new LessonHistory(learnerID, -1));
    }

    public void attendLesson(Lesson lesson, String learnerID, String comment){
        if(lesson.getListOfLearners().isEmpty() || !lesson.getListOfLearners().contains(learnerID)){
            return;
        }
        lesson.getLogOfActions().add(new LessonHistory(learnerID, 1, comment));
    }

    public HashMap<String, Lesson> getMapOfLessons() {
        return mapOfLessons;
    }

    public void setMapOfLessons(HashMap<String, Lesson> mapOfLessons) {
        this.mapOfLessons = mapOfLessons;
    }
}
