public class SchoolController {

    public LearnerController learnerController = new LearnerController();
    public LessonController lessonController = new LessonController();

    public void bookLearnerToLesson(Learner inputLearner, Lesson inputLesson) throws Exception {
        if(inputLearner == null || inputLesson == null){
            return;
        }

        if(inputLearner.getGradeLevel() != inputLesson.getGradeLevel() && (inputLearner.getGradeLevel() + 1) != inputLesson.getGradeLevel()){
            throw new Exception("The learner is ineligible to book the lesson due to the grade level restriction. Lesson's grade is " + inputLesson.getGradeLevel() + " while the learner is a grade " + inputLearner.getGradeLevel() + " swimmer.");
        }

        learnerController.addNewLearner(inputLearner);
        lessonController.addNewLesson(inputLesson);

        lessonController.bookLesson(lessonController.getLessonByID(inputLesson.getId()), inputLearner.getId());
    }

    public void cancelLesson(Lesson lesson, String learnerID){
        lessonController.cancelLesson(lesson, learnerID);
    }

    public void cancelLesson(String lessonID, String learnerID){
        lessonController.cancelLesson(lessonController.getLessonByID(lessonID), learnerID);
    }

    public void attendLesson(Lesson lesson, String learnerID, String comment){
        lessonController.attendLesson(lesson, learnerID, comment);
    }

    public void attendLesson(String lessonID, String learnerID, String comment){
        lessonController.attendLesson(lessonController.getLessonByID(lessonID), learnerID, comment);
    }
}
