import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolController {

    public LearnerController learnerController = new LearnerController();
    public LessonController lessonController = new LessonController();
    private Key key = new Key();


    public void bookLearnerToLesson(Learner inputLearner, Lesson inputLesson) throws Exception {
        if(inputLearner == null || inputLesson == null){
            return;
        }

        if(inputLearner.getGradeLevel() != inputLesson.getGradeLevel() && (inputLearner.getGradeLevel() + 1) != inputLesson.getGradeLevel()){
            throw new Exception("The learner is ineligible to book the lesson due to the grade level restriction. Lesson's grade is " + inputLesson.getGradeLevel() + " while the learner is a grade " + inputLearner.getGradeLevel() + " swimmer.");
        }

        learnerController.addNewLearner(inputLearner);
        lessonController.addNewLesson(inputLesson);

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
        lessonController.attendLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID, comment, rating);
    }

    public void attendLesson(String lessonID, String learnerID, String comment, int rating){
        try {
            Lesson lesson = lessonController.getLessonByID(lessonID);
            lessonController.attendLesson(lessonController.getLastEventOfLearner(lesson, learnerID).getID(), lesson, learnerID, comment, rating);
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
