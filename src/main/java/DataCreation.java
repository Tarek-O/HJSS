import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataCreation {
    SchoolController schoolController;
    BufferedReader br;
    String breakLine = "********";
    ArrayList<String> listOfPreviousKeys;

    public DataCreation(SchoolController schoolController){
        this.schoolController = schoolController;
        listOfPreviousKeys = new ArrayList<>();
    }
    public void execute(String fileDirectory) {
        FileReader file;
        try {
            file = new FileReader(fileDirectory);
        }catch (Exception e){
            System.err.println("Error reading from file.");
            return;
        }
        br = new BufferedReader(file);
        String readLine;
        try {
            while ((readLine = br.readLine()) != null) {
                if (readLine.trim().equalsIgnoreCase("Learner:")) {
                    readLearner();
                } else if (readLine.trim().equalsIgnoreCase("Lesson:")) {
                    readLesson();
                }
            }
        }catch (Exception e){}
    }

    public void readLearner(){
    String readLine;
    try {
        while ((readLine = br.readLine()) != null) {
            if (readLine.trim().equalsIgnoreCase(breakLine)) break;
            try {
                schoolController.addNewLearner(createLearner(readLine));
            } catch (Exception learnerException) {
                System.err.println(learnerException.getMessage());
            }
        }
    }catch (Exception e){}
    }

    public void readLesson(){
        Lesson lesson = null;
        try{
            String inputLessonFromFile = br.readLine();
            try{
                lesson = createLesson(inputLessonFromFile);
                if(lesson == null) return;
                schoolController.addNewLesson(lesson);
                try {
                    String nextStep;
                    boolean endOfLogActions = false;
                    while(!endOfLogActions){
                        nextStep = br.readLine();
                        if(nextStep.trim().equalsIgnoreCase(breakLine)) return;
                        if(nextStep.trim().equalsIgnoreCase("Learners attending:"))
                            readLearnersAttending(lesson);
                        else if (nextStep.trim().equalsIgnoreCase("Log of Actions:"))
                            endOfLogActions = readLogOfActions(lesson);
                    }
                }catch (Exception a){}
            }catch (Exception lessonException){
                System.err.println(lessonException.getMessage());
            }
        }catch (Exception e){}
    }

    public void readLearnersAttending(Lesson lesson){
        try{
            String inputLearnersAttendingFromFile = br.readLine();
            appendLearnerToLesson(lesson, inputLearnersAttendingFromFile);
        }catch (Exception e){}
    }

    public boolean readLogOfActions(Lesson lesson){
        try{
            String inputLog;
            while ((inputLog = br.readLine()) != null){
                if(inputLog.trim().equalsIgnoreCase(breakLine)) break;
                try{
                    appendLogActions(lesson, inputLog);
                }catch (Exception a){System.err.println(a.getMessage());}
            }
        }catch (Exception e){ return true;}
        return true;
    }

    public static Learner createLearner(String inputLearnerFromFile) throws Exception{
        //Learner: Tarek Omran, Male, 19/5/2018, 07570492050, 2
        String inputData[] = inputLearnerFromFile.split(",");
        if(inputData.length != 5) throw new Exception("The data entered is not sufficient to create a learner.");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth = LocalDate.parse(inputData[2].trim(), inputDateFormatter);
        return new Learner(inputData[0].trim(), inputData[1].trim(), dateOfBirth, inputData[3].trim(), Integer.parseInt(inputData[4].trim()));
    }

    public Lesson createLesson(String inputLessonFromFile) throws Exception{
        //10/4/2024, 11:30, 12:30, 1, Coach Ahmad
        String inputData[] = inputLessonFromFile.split(",");
        if(inputData.length < 5) throw new Exception("The data entered is not sufficient to create a lesson.");
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfLesson = LocalDate.parse(inputData[0].trim(), inputDateFormatter);
        LocalTime startTime = LocalTime.parse(inputData[1].trim());
        LocalTime endTime = LocalTime.parse(inputData[2].trim());
        return new Lesson(dateOfLesson, Integer.parseInt(inputData[3].trim()), startTime, endTime, inputData[4]);
    }

    public void appendLearnerToLesson(Lesson lesson, String inputLearnerIds){
        //TO180519, MT190131
        String[] listOfLearnerIds = inputLearnerIds.split(",");
        for(int i = 0 ; i < listOfLearnerIds.length; i++){
            if(!lesson.hasAvailableSpot()) break;
            Learner learner = schoolController.getListOfLearnersByLearnerID(listOfLearnerIds[i].trim()).getFirst();
            lesson.getListOfLearners().add(learner.getId());
        }
    }

    public void appendLogActions(Lesson lesson, String inputLogAction) throws Exception {
        //ID, TO180519, 1, comment, rating, coachName
        //ID, TO180519, 0, coachName
        String[] logData = inputLogAction.split(",");
        if(logData.length < 4 ) throw new Exception("The data entered is not sufficient to create a log action.");
        int status = Integer.parseInt(logData[2].trim());
        if(status > 1 || status < -1) throw new Exception("The status entered is invalid.");
        LessonEvent lessonEvent = null;
        if(status == 0 || status == -1){
            lessonEvent = new LessonEvent(logData[0].trim(), schoolController.getListOfLearnersByLearnerID(logData[1].trim()).getFirst().getId(), status, logData[3].trim());
        }else if(status == 1){
            int rating = Integer.parseInt(logData[4].trim());
            if(rating > 5 || rating < 1) throw new Exception("The rating entered is invalid.");
            lessonEvent = new LessonEvent(logData[0].trim(), schoolController.getListOfLearnersByLearnerID(logData[1].trim()).getFirst().getId(),
                    status, logData[3].trim(), Integer.parseInt(logData[4].trim()), logData[5].trim());
        }
        if(lessonEvent != null){
            lesson.getLogOfActions().add(lessonEvent);
            listOfPreviousKeys.add(lessonEvent.getID());
        }
    }
}
