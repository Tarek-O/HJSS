public class SchoolController {

    public LearnerController learnerController = new LearnerController();
    public LessonController lessonController = new LessonController();

    public void linkLearnerToLesson(Learner inputLearner, Lesson inputLesson){
        learnerController.addNewLearner(inputLearner);
        lessonController.addNewLesson(inputLesson);
        lessonController.getLessonByID(inputLesson.getId()).bookLesson(inputLearner.getId());
    }
}
