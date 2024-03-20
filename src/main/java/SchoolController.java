import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

        lessonController.bookLesson(key.generateUniqueKey(),lessonController.getLessonByID(inputLesson.getId()), inputLearner.getId());
    }

    public void cancelLesson(Lesson lesson, String learnerID){
        try {
            lessonController.cancelLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void cancelLesson(String lessonID, String learnerID){
        try{
            Lesson lesson = lessonController.getLessonByID(lessonID);
            lessonController.cancelLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void attendLesson(Lesson lesson, String learnerID, String comment, int rating) throws Exception {
        try {
            lessonController.attendLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID, comment, rating);

            Learner tempLearnerObject = learnerController.getLearnerByID(learnerID);
            if (lesson.getGradeLevel() == tempLearnerObject.getGradeLevel() + 1) {
                incrementLearnerGrade(tempLearnerObject);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void attendLesson(String lessonID, String learnerID, String comment, int rating){
        try {
            Lesson lesson = lessonController.getLessonByID(lessonID);
            lessonController.attendLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID, comment, rating);

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
            previousBookingID = lessonController.getLastEventOfLearner(previousLesson, learnerID).getID();
            lessonController.bookLesson(lessonController.getLastEventOfLearner(previousLesson,learnerID).getID(), newLesson, learnerID);
            lessonController.cancelLesson("EXPIRED", previousLesson, learnerID);
        }catch (Exception e){
            System.err.println(e.getMessage());
            try {
                lessonController.bookLesson(previousBookingID, previousLesson, learnerID);
                lessonController.cancelLesson("ABORT", newLesson, learnerID);
            }catch(Exception e2){
                System.err.println(e2.getMessage());
            }
        }
    }

    /**
     * Gets the list of all lessons the learner has a valid booking or previous attendance.
     * @param learnerID The learner's ID
     * @return List of lessons
     */
    public List<Lesson> getLearnerLessonsList(String learnerID){
        List<Lesson> lessonList = new ArrayList<Lesson>();
        for(Map.Entry<String, Lesson> entry : getLessonController().getMapOfLessons().entrySet()){
            if(entry.getValue().getListOfLearners().contains(learnerID)){
                lessonList.add(entry.getValue());
            }
        }
        return lessonList;
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

    public LearnerController getLearnerController() {
        return learnerController;
    }

    public void setLearnerController(LearnerController learnerController) {
        this.learnerController = learnerController;
    }

    public LessonController getLessonController() {
        return lessonController;
    }

    public void setLessonController(LessonController lessonController) {
        this.lessonController = lessonController;
    }
}
