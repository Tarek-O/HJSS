import java.time.LocalDate;
import java.util.*;

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
     * @param lessonId
     */
    public void removeLesson(String lessonId) throws Exception {
        if(hasLessonBeenAttended(lessonId)) return;
        mapOfLessons.remove(lessonId);
    }

    public Lesson getLessonByID(String ID) throws Exception {
        Lesson lesson = getMapOfLessons().get(ID);
        if(lesson == null) throw new Exception("The lesson does not exist in our system.");
        return lesson;
    }

    /**
     * If the Lesson has capacity available it will append the Learner's ID as one of its Learners. Then it adds the action in its logs.
     * @param inputLearnerID The ID of the Learner
     */
    public void bookLesson(String key, String lessonId, String inputLearnerID, String coachName) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        if(lesson.getListOfLearners().contains(inputLearnerID)) throw new Exception("The learner has already booked this lesson!");
        if(!lesson.hasAvailableSpot()) throw new Exception("The lesson is fully booked! You can not book a spot.");

        lesson.getListOfLearners().add(inputLearnerID);
        lesson.getLogOfActions().add(new LessonEvent(key, inputLearnerID, 0, coachName));
    }

    public void cancelLesson(String key, Lesson lesson, String learnerID, String coachName) throws Exception {
        if(lesson.getListOfLearners().isEmpty()) {
            return;
        }
        if(!hasLearnerBookedLesson(lesson.getId(), learnerID)) throw new Exception("The learner did not book this lesson.");

        LessonEvent event = getLastEventOfLearner(lesson.getId(), learnerID);
        if (event.getStatus() == -1) throw new Exception("You can not cancel a previously canceled lesson.");
        if (event.getStatus() == 1) throw new Exception("You can not cancel an attended lesson.");
        if(event.getStatus() == 0) {
            lesson.getListOfLearners().remove(learnerID);
            lesson.getLogOfActions().add(new LessonEvent(key, learnerID, -1, coachName));
        }
    }

    public void attendLesson(String key, Lesson lesson, String learnerID, String comment, int rating, String coachName) throws Exception {
        if(lesson.getListOfLearners().isEmpty() || !lesson.getListOfLearners().contains(learnerID)){
            return;
        }
        lesson.getLogOfActions().add(new LessonEvent(key, learnerID, 1, comment, rating, coachName));
    }

    public LessonEvent getLastEventOfLearner(String lessonId, String learnerID) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        if(learnerID.isEmpty()){
            throw new Exception("The learner has no records under this lesson.");
        }
        LessonEvent lastLessonEvent = null;
        for(LessonEvent le : lesson.getLogOfActions()){
            if(le.getLearnerID().equals(learnerID)) lastLessonEvent = le;
        }
        if(lastLessonEvent == null) throw new Exception("The learner has no records under this lesson.");
        return lastLessonEvent;
    }

    public List<LessonEvent> getAllLearnerActivityFromLesson(Lesson lesson, String learnerID) throws Exception {
        if(learnerID.isEmpty()){
            throw new Exception("The learner has no records under this lesson.");
        }
        if(lesson == null) throw new Exception("The lesson is not valid.");

        List<LessonEvent> listOfLessonEvents = new ArrayList<LessonEvent>();
        for(LessonEvent le : lesson.getLogOfActions()){
            if(le.getLearnerID().equals(learnerID)) listOfLessonEvents.add(le);
        }
        if(listOfLessonEvents == null || listOfLessonEvents.isEmpty()) throw new Exception("The learner has no records under this lesson.");
        return listOfLessonEvents;
    }

    public List<Lesson> getListOfLessonsByCoach(String coachName){
        List<Lesson> resultList = new ArrayList<Lesson>();
        for (Map.Entry<String, Lesson> set :
                getMapOfLessons().entrySet()) {
            if(set.getValue().getCoachName().equals(coachName))
                resultList.add(set.getValue());
        }
        return resultList;
    }

    /**
     * Gets the list of all lessons the learner has a valid booking or previous attendance.
     * @param learnerID The learner's ID
     * @return List of lessons
     */
    public List<Lesson> getListOfLessonsByLearner(String learnerID){
        List<Lesson> lessonList = new ArrayList<Lesson>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()){
            if(entry.getValue().getListOfLearners().contains(learnerID)){
                lessonList.add(entry.getValue());
            }
        }
        return lessonList;
    }

    public HashMap<Lesson, List<LessonEvent>> getLearnerHistory(String learnerId){
        if(getMapOfLessons() == null || getMapOfLessons().isEmpty()) return null;

        HashMap<Lesson, List<LessonEvent>> resultMap = new HashMap<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()){
            List<LessonEvent> listOfLessonEvents = new ArrayList<>();
            for(int i = 0 ; i < entry.getValue().getLogOfActions().size(); i++){
                LessonEvent lessonEvent = entry.getValue().getLogOfActions().get(i);
                if(lessonEvent.getLearnerID().equals(learnerId))
                    listOfLessonEvents.add(lessonEvent);
            }
            if(!listOfLessonEvents.isEmpty())
                resultMap.put(entry.getValue(), listOfLessonEvents);
        }
        return resultMap;
    }

    public HashMap<String, Double> getAllCoachReport(){
        if(getMapOfLessons() == null || getMapOfLessons().isEmpty()) return null;

        HashMap<String, List<Integer>> resultMap = new HashMap<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()){
            Lesson lesson = entry.getValue();
            if(lesson == null) break;
            for(int i = 0 ; i < entry.getValue().getLogOfActions().size(); i++){
                LessonEvent lessonEvent = entry.getValue().getLogOfActions().get(i);
                if(lessonEvent.getStatus() != 1) continue;
                if(resultMap.containsKey(lessonEvent.getCoachName())){
                    List<Integer> temp = resultMap.get(lessonEvent.getCoachName());
                    temp.add(lessonEvent.getRating());
                }else{
                    List<Integer> temp = new ArrayList<>();
                    temp.add(lessonEvent.getRating());
                    resultMap.put(lessonEvent.getCoachName(), temp);
                }
            }
        }

        HashMap<String, Double> returnMap = new HashMap<>();
        for(Map.Entry<String, List<Integer>> entry : resultMap.entrySet()) {
            double averageOfLessonEventRating = 0;
            for(int i = 0 ; i < entry.getValue().size(); i++){
                averageOfLessonEventRating += entry.getValue().get(i);
            }
            averageOfLessonEventRating = averageOfLessonEventRating / entry.getValue().size();
            returnMap.put(entry.getKey(), averageOfLessonEventRating);
        }
        return returnMap;
    }

    public HashMap<String, List<Lesson>> getListOfLessonsPerCoach(){
        if(getMapOfLessons() == null || getMapOfLessons().isEmpty()) return null;

        HashMap<String, List<Lesson>> resultMap = new HashMap<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()){
            Lesson lesson = entry.getValue();
            if(lesson == null) break;
            if(resultMap.containsKey(entry.getValue().getCoachName())){
                List<Lesson> temp = resultMap.get(entry.getValue().getCoachName());
                temp.add(entry.getValue());
            }else{
                List<Lesson> temp = new ArrayList<>();
                temp.add(entry.getValue());
                resultMap.put(entry.getValue().getCoachName(), temp);
            }
        }

        return resultMap;
    }
    public List<Lesson> getListOfLessonsByMonth(int numberOfMonth, int year){
        LocalDate temp = LocalDate.of(year, numberOfMonth, 1);
        String monthAbbrev = temp.getMonth().toString().substring(0,3).toUpperCase();
        List<Lesson> listOfLessonsByMonth = new ArrayList<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()) {
            if(entry.getKey().endsWith(monthAbbrev.concat(String.valueOf(temp.getYear()%100)))) listOfLessonsByMonth.add(entry.getValue());
        }
        return listOfLessonsByMonth;
    }

    public List<Lesson> getListOfLessonsByGrade(int gradeLevel){
        List<Lesson> listOfLessonsByGrade = new ArrayList<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()) {
            if(entry.getKey().startsWith(gradeLevel + "GR")) listOfLessonsByGrade.add(entry.getValue());
        }
        return listOfLessonsByGrade;
    }

    public List<Lesson> getListOfLessonsByDayName(String dayName){
        String dayNameAbbrev = dayName.substring(0,3).toUpperCase();
        List<Lesson> listOfLessonsByDayName = new ArrayList<>();
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()) {
            String entryDayName = entry.getKey().substring(entry.getKey().toString().indexOf("GR") + 4, entry.getKey().toString().indexOf("GR") + 7).toUpperCase();
            if(entryDayName.equals(dayNameAbbrev)) listOfLessonsByDayName.add(entry.getValue());
        }
        return listOfLessonsByDayName;
    }

    public void removeLearnerFromLesson(String lessonId, String learnerID) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        if(hasLearnerBookedLesson(lesson.getId(), learnerID) && !hasLearnerAttendedOrCanceledLesson(lesson.getId(), learnerID))
            lesson.getListOfLearners().remove(learnerID);
    }

    public boolean hasLearnerBookedLesson(String lessonId, String learnerID) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        if(!lesson.getListOfLearners().contains(learnerID)) throw new Exception("The learner did not book this lesson.");
        return true;
    }

    public boolean hasLearnerAttendedOrCanceledLesson(String lessonId, String learnerID) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        if(getLastEventOfLearner(lessonId, learnerID).getStatus() != 0) throw new Exception("The learner is not booked on this lesson.");
        return true;
    }

    public boolean hasLessonBeenAttended(String lessonId) throws Exception {
        Lesson lesson = getLessonByID(lessonId);
        for(int i = 0 ; i < lesson.getLogOfActions().size(); i++){
            if(lesson.getLogOfActions().get(i).getStatus() == 1) return true;
        }
        return false;
    }

    public HashMap<String, Lesson> getMapOfLessons() {
        return mapOfLessons;
    }

    public void setMapOfLessons(HashMap<String, Lesson> mapOfLessons) {
        this.mapOfLessons = mapOfLessons;
    }
}
