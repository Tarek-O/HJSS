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
    private Learner deletedLearnerMock;
    SchoolController(){}
    SchoolController(LearnerController learnerController, LessonController lessonController){
        setLearnerController(learnerController);
        setLessonController(lessonController);
        try {
            deletedLearnerMock = new Learner("DELETED LEARNER", "M", LocalDate.now(), "07570492050", 1);
        }catch (Exception e){}
    }

    public void bookLearnerToLesson(Learner inputLearner, Lesson inputLesson) throws Exception {
        if(inputLearner == null || inputLesson == null){
            return;
        }

        double learnerAge = getLearnerAgeWhen(inputLearner, inputLesson.getDateOfLesson());
        if(learnerAge> 11 || learnerAge < 4) throw new Exception("The learner is inelligible to book the lesson due to the age restriction.");

        if(inputLearner.getGradeLevel() != inputLesson.getGradeLevel() && (inputLearner.getGradeLevel() + 1) != inputLesson.getGradeLevel()){
            throw new Exception("The learner is ineligible to book the lesson due to the grade level restriction. Lesson's grade is " + inputLesson.getGradeLevel() + " while the learner is a grade " + inputLearner.getGradeLevel() + " swimmer.");
        }

        if(hasLessonBeenAttended(inputLesson)) throw new Exception("The lesson has already finished.");

        addNewLearner(inputLearner);
        addNewLesson(inputLesson);

        lessonController.bookLesson(key.generateUniqueKey(),inputLesson.getId(), inputLearner.getId(), inputLesson.getCoachName());
    }

    public void cancelLesson(String lessonId, String learnerID){
        try {
            Lesson lesson = lessonController.getLessonByID(lessonId);
            lessonController.cancelLesson(getLastEventOfLearner(lessonId, learnerID).getID(), lesson, learnerID, lesson.getCoachName());
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }


    public boolean attendLesson(String lessonID, String learnerID, String comment, int rating) throws Exception {
        if(rating > 5 || rating < 1) throw new Exception("The please enter a valid rating, where 5 is the highest rating.");
        Lesson lesson = lessonController.getLessonByID(lessonID);
        lessonController.attendLesson(getLastEventOfLearner(lessonID, learnerID).getID(), lesson, learnerID, comment, rating, lesson.getCoachName());

        Learner tempLearnerObject = learnerController.getLearnerByID(learnerID);
        if(lesson.getGradeLevel() == tempLearnerObject.getGradeLevel() + 1){
            incrementLearnerGrade(tempLearnerObject);
            revalidateLearnerBookings(tempLearnerObject);
            return true;
        }

        return false;
    }

    public void modifyBooking(String learnerID, Lesson previousLesson, Lesson newLesson){
        String previousBookingID = "";
        try{
            previousBookingID = getLastEventOfLearner(previousLesson.getId(), learnerID).getID();
            lessonController.bookLesson(getLastEventOfLearner(previousLesson.getId(),learnerID).getID(), newLesson.getId(), learnerID, newLesson.getCoachName());
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


    public List<LessonEvent> getCoachLessonsEventsByMonth(String coachName, int numberOfMonth, int year) {
        return lessonController.getCoachLessonsEventsByMonth(coachName,numberOfMonth,year);
    }

    public void removeLesson(String lessonId) throws Exception {
        lessonController.removeLesson(lessonId);
    }

    public void removeLearner(Learner learner){
        for(Map.Entry<String, Lesson> entry : getMapOfLessons().entrySet()){
            entry.getValue().getListOfLearners().remove(learner.getId());
            for(int i = 0 ; i < entry.getValue().getLogOfActions().size(); i++){
                LessonEvent lessonEvent = entry.getValue().getLogOfActions().get(i);
                if(lessonEvent.getLearnerID().equals(learner.getId())){
                    lessonEvent.setLearnerID(deletedLearnerMock.getId());
                }
            }
        }
        learnerController.removeLearner(learner);
    }

    public void incrementLearnerGrade(Learner learner){
        if(learner.getGradeLevel() == 5){
            return;
        }

        if(learner.getGradeLevel() > 5){
            learner.setGradeLevel(5);
            return;
        }
        if(learner.getGradeLevel() < 1){
            learner.setGradeLevel(1);
            return;
        }
        learner.setGradeLevel(learner.getGradeLevel() + 1);
    }

    public void revalidateLearnerBookings(Learner learner){
        List<Lesson> listOfLessons = lessonController.getListOfLessonsByLearner(learner.getId());
        if(listOfLessons == null || listOfLessons.isEmpty()) return;

        for(int i = 0; i < listOfLessons.size(); i++){
            if(listOfLessons.get(i).getGradeLevel() < learner.getGradeLevel())
                cancelLesson(listOfLessons.get(i).getId(), learner.getId());
        }
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

    public LessonEvent getLastEventOfLearner(String lessonId, String learnerID) throws Exception {
        return lessonController.getLastEventOfLearner(lessonId, learnerID);
    }

    public HashMap<String, Double> getAllCoachReport(){
        return lessonController.getAllCoachReport();
    }

    public List<Lesson> getListOfLearnerLessonsByMonth(String learnerId, int numberOfMonth, int year){
        return lessonController.getListOfLearnerLessonsByMonth(learnerId, numberOfMonth, year);
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

    public Lesson getLessonById(String id) throws Exception {
        return lessonController.getLessonByID(id);
    }
    public Lesson getLearnerBookedLessonById(String inputLessonId, Learner inputLearner) throws Exception {
        Lesson inputLesson = getLessonById(inputLessonId);

        if(inputLesson == null || inputLearner == null){
            return null;
        }

        if(!inputLesson.getListOfLearners().contains(inputLearner.getId())) return null;

        if(hasLearnerAttendedLesson(inputLessonId, inputLearner.getId())) return null;

        return inputLesson;
    }

    public boolean hasLearnerAttendedLesson(String lessonId, String learnerId) throws Exception {
        return lessonController.hasLearnerAttendedLesson(lessonId, learnerId);
    }

    public List<Lesson> getLearnerBookedLessonByMonth(Learner learner, int year, int month) throws Exception {
        List<Lesson> listOfLesson = getListOfLessonsByMonth(month, year);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLearnerAttendedLesson(listOfLesson.get(i).getId(), learner.getId())) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerBookedLessonByDayName(Learner learner, String dayName) throws Exception {
        List<Lesson> listOfLesson = getListOfLessonsByDayName(dayName);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLearnerAttendedLesson(listOfLesson.get(i).getId(), learner.getId())) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerBookedLessonByGrade(Learner learner, int gradeLevel) throws Exception {
        List<Lesson> listOfLesson = getListOfLessonsByGrade(gradeLevel);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            else if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLearnerAttendedLesson(listOfLesson.get(i).getId(), learner.getId())) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public Lesson getLearnerBookedLessonByDate(Learner learner, LocalDate lessonDate) throws Exception {
        Lesson lesson = getLessonByDate(lessonDate);
        if(!lesson.getListOfLearners().contains(learner.getId())) return null;
        if(hasLearnerAttendedLesson(lesson.getId(), learner.getId())) return null;
        return lesson;
    }

    public boolean hasLessonBeenAttended(Lesson lesson){
        return lessonController.hasLessonBeenAttended(lesson);
    }

    public Lesson getLearnerBookedNotAttendedLessonById(String inputLessonId, Learner inputLearner) throws Exception {
        Lesson inputLesson = getLessonById(inputLessonId);

        if(inputLesson == null || inputLearner == null){
            return null;
        }
        if(!inputLesson.getListOfLearners().contains(inputLearner.getId())) return null;
        if(hasLessonBeenAttended(inputLesson)) return null;

        return inputLesson;
    }


    public List<Lesson> getLearnerBookedNotAttendedLessonByMonth(Learner learner, int year, int month) throws Exception {
        List<Lesson> listOfLesson = getListOfLessonsByMonth(month, year);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            else if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLessonBeenAttended(listOfLesson.get(i))) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerBookedNotAttendedLessonByDayName(Learner learner, String dayName){
        List<Lesson> listOfLesson = getListOfLessonsByDayName(dayName);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            else if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLessonBeenAttended(listOfLesson.get(i))) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerBookedNotAttendedLessonByGrade(Learner learner, int gradeLevel){
        List<Lesson> listOfLesson = getListOfLessonsByGrade(gradeLevel);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            if(listOfLesson.get(i).getListOfLearners().isEmpty()){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
            else if(!listOfLesson.get(i).getListOfLearners().contains(learner.getId())){
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }else if(hasLessonBeenAttended(listOfLesson.get(i))) {
                listOfLesson.remove(listOfLesson.get(i));
                i--;
            }
        }
        return listOfLesson;
    }

    public Lesson getLearnerBookedNotAttendedLessonByDate(Learner learner, LocalDate lessonDate) throws Exception {
        Lesson lesson = getLessonByDate(lessonDate);
        if(!lesson.getListOfLearners().contains(learner.getId())) return null;
        if(hasLessonBeenAttended(lesson)) return null;
        return lesson;
    }


    public Lesson getLearnerAvailableLessonById(String inputLessonId, Learner inputLearner) throws Exception {
        Lesson inputLesson = getLessonById(inputLessonId);

        if(inputLesson == null || inputLearner == null){
            return null;
        }

        canLearnerBookLesson(inputLesson, inputLearner);

        return inputLesson;
    }

    public List<Lesson> getLearnerAvailableLessonByMonth(Learner learner, int year, int month){
        List<Lesson> listOfLesson = getListOfLessonsByMonth(month, year);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            try {
                canLearnerBookLesson(listOfLesson.get(i), learner);
            }catch (Exception e){listOfLesson.remove(listOfLesson.get(i)); i--;}
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerAvailableLessonByDayName(Learner learner, String dayName){
        List<Lesson> listOfLesson = getListOfLessonsByDayName(dayName);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            try {
                canLearnerBookLesson(listOfLesson.get(i), learner);
            }catch (Exception e){listOfLesson.remove(listOfLesson.get(i)); i--;}
        }
        return listOfLesson;
    }

    public List<Lesson> getLearnerAvailableLessonByGrade(Learner learner, int gradeLevel){
        List<Lesson> listOfLesson = getListOfLessonsByGrade(gradeLevel);
        for(int i = 0 ; i < listOfLesson.size(); i++){
            try {
                canLearnerBookLesson(listOfLesson.get(i), learner);
            }catch (Exception e){listOfLesson.remove(listOfLesson.get(i)); i--;}
        }
        return listOfLesson;
    }

    public Lesson getLearnerAvailableLessonByDate(Learner learner, LocalDate lessonDate) throws Exception {
        Lesson lesson = getLessonByDate(lessonDate);
        canLearnerBookLesson(lesson, learner);
        return lesson;
    }

    public boolean canLearnerBookLesson(Lesson inputLesson, Learner inputLearner) throws Exception {
        double learnerAge = getLearnerAgeWhen(inputLearner, inputLesson.getDateOfLesson());
        if(learnerAge > 11 || learnerAge < 4) throw new Exception("The learner is inelligible to book the lesson due to the age restriction.");
        if(inputLearner.getGradeLevel() != inputLesson.getGradeLevel() && (inputLearner.getGradeLevel() + 1) != inputLesson.getGradeLevel()){
            throw new Exception("The learner is ineligible to book the lesson due to the grade level restriction. Lesson's grade is " + inputLesson.getGradeLevel() + " while the learner is a grade " + inputLearner.getGradeLevel() + " swimmer.");
        }

        if(inputLesson.getListOfLearners().contains(inputLearner.getId())) throw new Exception("The learner has already booked this lesson!");
        if(!inputLesson.hasAvailableSpot()) throw new Exception("The lesson is fully booked! You can not book a spot.");

        return true;
    }

    public Lesson getLessonByDate(LocalDate localDate) throws Exception {
        Lesson lesson = lessonController.getLessonByDate(localDate);
        if(lesson == null) throw new Exception("No lesson is registered with such date.");
        return lesson;
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
