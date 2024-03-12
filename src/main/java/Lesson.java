import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Lesson {

    private String id;
    private LocalDate dateOfLesson;
    private int gradeLevel;
    private ArrayList<Learner> listOfLearners;
    private String timeSlot;
    private final int maxNumberOfLearners = 4;

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson
     * @param gradeLevel
     * @param timeSlot
     * @param listOfLearners
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, String timeSlot, ArrayList<Learner> listOfLearners){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setTimeSlot(timeSlot);
        this.listOfLearners = listOfLearners;
        setId(generateLessonID());
    }

    /**
     * Constructor for Lesson where the ID is automatically generated based on input.
     * @param dateOfLesson
     * @param gradeLevel
     * @param timeSlot
     */
    public Lesson(LocalDate dateOfLesson, int gradeLevel, String timeSlot){
        setDateOfLesson(dateOfLesson);
        setGradeLevel(gradeLevel);
        setTimeSlot(timeSlot);
        listOfLearners = new ArrayList<Learner>();
        setId(generateLessonID());
    }

    public void addLearner(Learner inputLearner){
        if(hasAvailableSpot() && !listOfLearners.contains(inputLearner)){
            listOfLearners.add(inputLearner);
        }
    }

    /**
     *
     * @return Return boolean if the list of Learners can be appended.
     */
    public boolean hasAvailableSpot(){
        return listOfLearners.size() < maxNumberOfLearners;
    }

    /**
     *
     * @return Size of Learners
     */
    public int getNumberOfLearners(){
        return listOfLearners.size();
    }

    /**
     * The ID of the Lesson is generated using the Grade Level, Time Slot, Day of the Week, Date, Month and Year.
     * @return ID of Lesson
     */
    public String generateLessonID(){
        return getGradeLevel() + "GR" + getTimeSlot() + getDateOfLesson().getDayOfWeek().toString().substring(0,3).toUpperCase() + String.format("%02d", getDateOfLesson().getDayOfMonth()) + getDateOfLesson().getMonth().toString().substring(0,3).toUpperCase()  + (getDateOfLesson().getYear()%100);
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

    public ArrayList<Learner> getListOfLearners() {
        return listOfLearners;
    }

    public void setListOfLearners(ArrayList<Learner> listOfLearners) {
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
}
