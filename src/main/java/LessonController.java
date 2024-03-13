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

    public HashMap<String, Lesson> getMapOfLessons() {
        return mapOfLessons;
    }

    public void setMapOfLessons(HashMap<String, Lesson> mapOfLessons) {
        this.mapOfLessons = mapOfLessons;
    }
}
