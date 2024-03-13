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
        lessonController.getLessonByID(inputLesson.getId()).bookLesson(inputLearner.getId());
    }
}
