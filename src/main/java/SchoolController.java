import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolController {

    private LearnerController learnerController = new LearnerController();
    private LessonController lessonController = new LessonController();
    private Key key = new Key();
    private final double maxHoursPerClass = 1;
    private final int maxNumberOfLearnerPerClass = 4;
    SchoolController(){}
    SchoolController(LearnerController learnerController, LessonController lessonController){
        setLearnerController(learnerController);
        setLessonController(lessonController);
    }
    SchoolController(LearnerController learnerController){
        setLearnerController(learnerController);
    }
    SchoolController(LessonController lessonController){
        setLessonController(lessonController);
    }

    public void bookLearnerToLesson(Learner inputLearner, Lesson inputLesson) throws Exception {
        if(inputLearner == null || inputLesson == null){
            return;
        }

        if(inputLearner.getGradeLevel() != inputLesson.getGradeLevel() && (inputLearner.getGradeLevel() + 1) != inputLesson.getGradeLevel()){
            throw new Exception("The learner is ineligible to book the lesson due to the grade level restriction. Lesson's grade is " + inputLesson.getGradeLevel() + " while the learner is a grade " + inputLearner.getGradeLevel() + " swimmer.");
        }

        addNewLearner(inputLearner);
        addNewLesson(inputLesson);

        lessonController.bookLesson(key.generateUniqueKey(),inputLesson.getId(), inputLearner.getId(), inputLesson.getCoachName());
    }

    public void cancelLesson(String lessonId, String learnerID){
        try {
            Lesson lesson = lessonController.getLessonByID(lessonId);
            lessonController.cancelLesson(lessonController.getLastEventOfLearner(lessonId, learnerID).getID(), lesson, learnerID, lesson.getCoachName());
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


    public void attendLesson(String lessonID, String learnerID, String comment, int rating) throws Exception {
        if(rating > 5 || rating < 1) throw new Exception("The please enter a valid rating, where 5 is the highest rating.");
        try {
            Lesson lesson = lessonController.getLessonByID(lessonID);
            lessonController.attendLesson(lessonController.getLastEventOfLearner(lessonID, learnerID).getID(), lesson, learnerID, comment, rating, lesson.getCoachName());

            Learner tempLearnerObject = learnerController.getLearnerByID(learnerID);
            if(lesson.getGradeLevel() == tempLearnerObject.getGradeLevel() + 1){
                incrementLearnerGrade(tempLearnerObject);
            }

        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void modifyBooking(String learnerID, Lesson previousLesson, Lesson newLesson){
        String previousBookingID = "";
        try{
            previousBookingID = lessonController.getLastEventOfLearner(previousLesson.getId(), learnerID).getID();
            lessonController.bookLesson(lessonController.getLastEventOfLearner(previousLesson.getId(),learnerID).getID(), newLesson.getId(), learnerID, newLesson.getCoachName());
            lessonController.cancelLesson("EXPIRED", previousLesson, learnerID, previousLesson.getCoachName());
        }catch (Exception e){
            System.err.println(e.getMessage());
            try {
                lessonController.bookLesson(previousBookingID, previousLesson.getId(), learnerID, previousLesson.getCoachName());
                lessonController.cancelLesson("ABORT", newLesson, learnerID, newLesson.getCoachName());
            }catch(Exception e2){}
        }
    }


    public List<Lesson> getListOfLessonsByLearner(String learnerID){
        return lessonController.getListOfLessonsByLearner(learnerID);
    }

    public List<Lesson> getListOfLessonsByCoach(String coachName){
        return lessonController.getListOfLessonsByCoach(coachName);
    }

    public HashMap<String, List<Lesson>> getListOfLessonsPerCoach(){
        return lessonController.getListOfLessonsPerCoach();
    }

    public void incrementLearnerGrade(Learner learner){
        if(learner.getGradeLevel() == 5){
            return;
        }

        if(learner.getGradeLevel() > 5){
            learner.setGradeLevel(5);
            return;
        }
        learner.setGradeLevel(learner.getGradeLevel() + 1);
    }

    public void addNewLearner(Learner learner) throws Exception {
        if(learner == null) throw new Exception("Invalid learner entry.");
        if(learner.getGradeLevel() > 5 || learner.getGradeLevel() < 1) throw new Exception("Learner's grade does not match this school's grades list!");
        if(!isLearnerAgeValid(learner, LocalDate.now())) throw new Exception("Learner's age does not match this school's registering age policy!");
        learnerController.addNewLearner(learner);
    }

    public void addNewLesson(Lesson lesson) throws Exception{
        if(lesson == null) throw new Exception("Invalid lesson entry.");
        if(lesson.getGradeLevel() > 5 || lesson.getGradeLevel() < 1) throw new Exception("Lesson's grade does not match this school's grades list!");
        if(lesson.getNumberOfLearners() > maxNumberOfLearnerPerClass) throw new Exception("This lesson does not match this school's learner capacity. We expect 4 learners per lesson.");
        if(getHoursBetween(lesson.getStartTime(), lesson.getEndTime()) > maxHoursPerClass) throw new Exception("The lesson is more than " + maxHoursPerClass + " hours.");
        lessonController.addNewLesson(lesson);
    }

    public HashMap<Lesson, List<LessonEvent>> getLearnerHistory(String learnerId) throws Exception {
        if(learnerId.isEmpty() || learnerId.isBlank()) throw new Exception("Please enter a valid learner ID.");
        return lessonController.getLearnerHistory(learnerId);
    }

    public HashMap<String, Double> getAllCoachReport(){
        return lessonController.getAllCoachReport();
    }

    public List<Lesson> getListOfLessonsByMonth(int numberOfMonth, int year){
        return lessonController.getListOfLessonsByMonth(numberOfMonth, year);
    }
    public List<Lesson> getListOfLessonsByGrade(int gradeLevel){
        return lessonController.getListOfLessonsByGrade(gradeLevel);
    }

    public List<Lesson> getListOfLessonsByDayName(String dayName){
        return lessonController.getListOfLessonsByDayName(dayName);
    }

    public List<Learner> getListOfLearnersByLearnerID(String learnerId){
        return learnerController.getListOfLearnersByLearnerID(learnerId);
    }

    public Learner getLearnerByID(String id){
        return learnerController.getLearnerByID(id);
    }

    /**
     * The age of the learner of when they will potentially attend an event at a specific date.
     *
     * @param dateThen The date you want to validate
     * @return Age as a double
     */
    public boolean isLearnerAgeValid(Learner learner, LocalDate dateThen){
        double age = getLearnerAgeWhen(learner, dateThen);
        if(age >= 4 && age <= 11){
            return true;
        }
        return false;
    }

    public double getLearnerAge(Learner learner){
        return Period.between(learner.getBirthDate(), LocalDate.now()).getYears();
    }

    public double getLearnerAgeWhen(Learner learner, LocalDate dateThen){
        return Period.between(learner.getBirthDate(), dateThen).getYears();
    }

    public double getHoursBetween(LocalTime firstInput, LocalTime secondInput){
        return (double) firstInput.until(secondInput, ChronoUnit.MINUTES)/60;
    }

    private LearnerController getLearnerController() {
        return learnerController;
    }

    private void setLearnerController(LearnerController learnerController) {
        this.learnerController = learnerController;
    }

    private LessonController getLessonController() {
        return lessonController;
    }

    private void setLessonController(LessonController lessonController) {
        this.lessonController = lessonController;
    }

    public List<String> getFamiliarKeys(String search){
        return lessonController.getFamiliarKeys(search);
    }

    public HashMap<String, Lesson> getMapOfLessons() {
        return lessonController.getMapOfLessons();
    }

    public List<Learner> getListOfLearners() {
        return learnerController.getListOfLearners();
    }
}
