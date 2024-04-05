import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Lesson {

    private final int maxNumberOfLearners = 4;

    private String id;
    private LocalDate dateOfLesson;
    private int gradeLevel;
    private LocalTime startTime;
    private LocalTime endTime;
    private ArrayList<String> listOfLearners;
    private ArrayList<LessonEvent> logOfActions;
    private String coachName;

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson Date of when the Lesson will occur
     * @param gradeLevel The grade level of which this Lesson is
     * @param startTime The time slot of the Lesson
     * @param endTime The time slot of the Lesson
     * @param listOfLearners An array of String consisting of the IDs of the Learners
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, LocalTime startTime, LocalTime endTime, ArrayList<String> listOfLearners, String coachName){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setStartTime(startTime);
        setEndTime(endTime);
        setListOfLearners(listOfLearners);
        setId(generateLessonID());
        logOfActions = new ArrayList<LessonEvent>();
        setCoachName(coachName);
    }

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson Date of when the Lesson will occur
     * @param gradeLevel The grade level of which this Lesson is
     * @param startTime The time slot of the Lesson
     * @param endTime The time slot of the Lesson
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, LocalTime startTime, LocalTime endTime, String coachName){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setStartTime(startTime);
        setEndTime(endTime);
        listOfLearners = new ArrayList<String>();
        setId(generateLessonID());
        logOfActions = new ArrayList<LessonEvent>();
        setCoachName(coachName);
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
        return getGradeLevel() + "GR" + String.format("%02d",startTime.getHour()) + getDateOfLesson().getDayOfWeek().toString().substring(0,3).toUpperCase() + String.format("%02d", getDateOfLesson().getDayOfMonth()) + getDateOfLesson().getMonth().toString().substring(0,3).toUpperCase()  + (getDateOfLesson().getYear()%100);
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

    public ArrayList<LessonEvent> getLogOfActions() {
        return logOfActions;
    }

    public void setLogOfActions(ArrayList<LessonEvent> logOfActions) {
        this.logOfActions = logOfActions;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
}
