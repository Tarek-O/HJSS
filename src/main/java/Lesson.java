import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Lesson {

    private final int maxNumberOfLearners = 4;

    private String id;
    private LocalDate dateOfLesson;
    private int gradeLevel;
    private String timeSlot;
    private ArrayList<String> listOfLearners;
    private ArrayList<LessonHistory> logOfActions;

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson Date of when the Lesson will occur
     * @param gradeLevel The grade level of which this Lesson is
     * @param timeSlot The time slot of the Lesson
     * @param listOfLearners An array of String consisting of the IDs of the Learners
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, String timeSlot, ArrayList<String> listOfLearners){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setTimeSlot(timeSlot);
        setListOfLearners(listOfLearners);
        setId(generateLessonID());
        logOfActions = new ArrayList<LessonHistory>();
    }

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson Date of when the Lesson will occur
     * @param gradeLevel The grade level of which this Lesson is
     * @param timeSlot The time slot of the Lesson
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, String timeSlot){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setTimeSlot(timeSlot);
        listOfLearners = new ArrayList<String>();
        setId(generateLessonID());
        logOfActions = new ArrayList<LessonHistory>();
    }

    /**
     *
     * @return Return boolean if the list of Learners can be appended.
     */
    public boolean hasAvailableSpot(){
        return listOfLearners.size() < maxNumberOfLearners;
    }


    /**
     * The ID of the Lesson is generated using the Grade Level, Time Slot, Day of the Week, Date, Month and Year.
     * @return ID of Lesson
     */
    public String generateLessonID(){
        return getGradeLevel() + "GR" + getTimeSlot() + getDateOfLesson().getDayOfWeek().toString().substring(0,3).toUpperCase() + String.format("%02d", getDateOfLesson().getDayOfMonth()) + getDateOfLesson().getMonth().toString().substring(0,3).toUpperCase()  + (getDateOfLesson().getYear()%100);
    }

    /**
     * If the Lesson has capacity available it will append the Learner's ID as one of its Learners. Then it adds the action in its logs.
     * @param inputLearnerID The ID of the Learner
     */
    public void bookLesson(String inputLearnerID){
        if(hasAvailableSpot() && !listOfLearners.contains(inputLearnerID)){
            listOfLearners.add(inputLearnerID);
            logOfActions.add(new LessonHistory(inputLearnerID, 0));
        }
    }

    public void cancelLesson(String learnerID){
        if(getListOfLearners().isEmpty()) {
            return;
        }
        getListOfLearners().remove(learnerID);
        logOfActions.add(new LessonHistory(learnerID, -1));
    }

    public void attendLesson(String learnerID, String comment){
        if(getListOfLearners().isEmpty() || !getListOfLearners().contains(learnerID)){
            return;
        }
        logOfActions.add(new LessonHistory(learnerID, 1, comment));
    }

    /**
     *
     * @return Size of Learners
     */
    public int getNumberOfLearners(){
        return listOfLearners.size();
    }

    public LocalDate getDateOfLesson() {
        return dateOfLesson;
    }

    public void setDateOfLesson(LocalDate dateOfLesson) {
        this.dateOfLesson = dateOfLesson;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public ArrayList<String> getListOfLearners() {
        return listOfLearners;
    }

    public void setListOfLearners(ArrayList<String> listOfLearners) {
        this.listOfLearners = listOfLearners;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public ArrayList<LessonHistory> getLogOfActions() {
        return logOfActions;
    }

    public void setLogOfActions(ArrayList<LessonHistory> logOfActions) {
        this.logOfActions = logOfActions;
    }
}
