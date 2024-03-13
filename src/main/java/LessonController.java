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
    public void bookLesson(String key, Lesson lesson, String inputLearnerID) throws Exception {
        if(lesson.getListOfLearners().contains(inputLearnerID)) throw new Exception("The learner has already booked this lesson!");

        if(!lesson.hasAvailableSpot()) throw new Exception("The lesson is fully booked! You can not book a spot.");

        lesson.getListOfLearners().add(inputLearnerID);
        lesson.getLogOfActions().add(new LessonEvent(key, inputLearnerID, 0));
    }

    public void cancelLesson(String key, Lesson lesson, String learnerID){
        if(lesson.getListOfLearners().isEmpty()) {
            return;
        }
        try {
            if(hasLearnerBookedLesson(lesson, learnerID)) throw new Exception("The learner did not booked this lesson.");

            if (getLastEventOfLearner(lesson, learnerID).getStatus() == 1) throw new Exception("You can not cancel an attended lesson.");

            if(getLastEventOfLearner(lesson, learnerID).getStatus() == 0) {
                lesson.getListOfLearners().remove(learnerID);
                lesson.getLogOfActions().add(new LessonEvent(key, learnerID, -1));
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void attendLesson(String key, Lesson lesson, String learnerID, String comment){
        if(lesson.getListOfLearners().isEmpty() || !lesson.getListOfLearners().contains(learnerID)){
            return;
        }
        lesson.getLogOfActions().add(new LessonEvent(key, learnerID, 1, comment));
    }

    public LessonEvent getLastEventOfLearner(Lesson lesson, String learnerID) throws Exception {
        if(learnerID.isEmpty()){
            throw new Exception("The learner has no records under this lesson.");
        }
        LessonEvent lastLessonEvent = null;
        for(LessonEvent le : lesson.getLogOfActions()){
            if(le.getLearnerID().equals(learnerID)) lastLessonEvent = le;
        }
        return lastLessonEvent;
    }

    public void removeLearnerFromLesson(String learnerID, Lesson lesson) throws Exception {
        if(hasLearnerBookedLesson(lesson, learnerID)) lesson.getListOfLearners().remove(learnerID);
    }

    public boolean hasLearnerBookedLesson(Lesson lesson, String learnerID) throws Exception {
        if(!lesson.getListOfLearners().contains(learnerID)) throw new Exception("The learner did not book this lesson.");
        return true;
    }

    public HashMap<String, Lesson> getMapOfLessons() {
        return mapOfLessons;
    }

    public void setMapOfLessons(HashMap<String, Lesson> mapOfLessons) {
        this.mapOfLessons = mapOfLessons;
    }
}
