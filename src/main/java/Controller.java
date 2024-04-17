import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class Controller {

    public SchoolController schoolController;
    private String programName;
    private DataCreation dataCreation;
    private Scanner scan;
    private Learner learner;
    private Lesson lesson;
    Controller(String programName){
        schoolController = new SchoolController();
        dataCreation = new DataCreation(schoolController);
        scan = new Scanner(System.in);
        this.programName = programName;
    }

    public Scanner getScan(){
        return scan;
    }

    public DataCreation getDataCreation(){
        return dataCreation;
    }

    public void setProgramName(String programName){
        this.programName = programName;
    }

    public String getProgramName(){
        return programName;
    }

    public void createDataFromFile(String fileDirectory){
        getDataCreation().execute(fileDirectory);
    }


    public void printMenuList(){
        System.out.println(
                "Please select an action:\n"+
                "1. Book a swimming lesson\n" +
                "2. Change/Cancel a booking\n" +
                "3. Attend a swimming lesson\n" +
                "4. Report methods\n" +
                "5. Fetch lesson or learner details\n"+
                "6. Register new record\n"+
                "7. Modify lesson or learner information\n"+
                "0. Exit\n");
    }

    public void runMain(){
        Scanner scan = new Scanner(System.in);
        int command = -1;
        do{
            printMenuList();

            try {
                command = scan.nextInt();
            }catch (Exception e){}
            scan.nextLine();
            if(command == 1)
                bookLesson();
            else if(command == 2)
                changeOrCancelLesson();
            else if(command == 3)
                attendLesson();
            else if(command == 4)
                getReports();
            else if(command == 5)
                getInformation();
            else if(command == 6)
                getRegister();
            else if(command == 7)
                getModification();
        }while (command != 0);
        exitMessage();
    }

    public void deleteLearner(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Are you sure you want to DELETE this learner(y/n):\nLearner: " + learner.printLearner());
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        schoolController.removeLearner(learner);
                        learner = null;
                        System.out.println("The learner's name has been deleted successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println("An error has occurred.");}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void modifyLearnerEmergencyContactNumber(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Please enter the new learner's emergency contact number(E.g 07570123123):");
                String emergencyContactNumber = scan.nextLine();
                System.out.println("Are you sure you want to update this learner's emergency contact number to " + emergencyContactNumber + " (y/n):");
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        learner.setEmergencyContactNumber(emergencyContactNumber);
                        System.out.println("The learner's emergency contact number has been updated successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println(e.getMessage());}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void modifyLearnerName(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Please enter the new learner's name:");
                String learnerName = scan.nextLine();
                System.out.println("Are you sure you want to update this learner's name to " + learnerName +" (y/n):");
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        learner.setName(learnerName);
                        System.out.println("The learner's name has been updated successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println("An error has occurred.");}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void modifyLearner(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Modify learner's name\n"+
                            "2. Modify learner's emergency contact number\n"+
                            "3. Delete learner\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    modifyLearnerName();
                    return;
                }
                else if(userChoice == 2) {
                    modifyLearnerEmergencyContactNumber();
                    return;
                }
                else if(userChoice == 3) {
                    deleteLearner();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void modifyLessonCoach(){
        lesson = getLesson();
        if(lesson == null){
            System.err.println("No lesson was found.");
            return;
        }


        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Please enter the new coach's name:");
                String coachName = scan.nextLine();
                System.out.println("Are you sure you want to update this lesson's coach name (y/n):\nLesson: " + lesson.getId() + " : " + lesson.printDate() + " with new Coach Name: " + coachName);
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        lesson.setCoachName(coachName);
                        System.out.println("The lesson's coach has been updated successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println("An error has occurred.");}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void deleteLesson(){
        Lesson deleteLesson = getLesson();
        if(deleteLesson == null){
            System.err.println("No lesson was found.");
            return;
        }
        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Are you sure you want to DELETE this lesson (y/n):\nLesson: " + deleteLesson.getId() + " : " + deleteLesson.printDate());
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        schoolController.removeLesson(deleteLesson.getId());
                        System.out.println("The lesson has been deleted successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println("An error has occurred.");}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void modifyLesson(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Modify lesson's coach'\n"+
                            "2. Delete lesson\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    modifyLessonCoach();
                    return;
                }
                else if(userChoice == 2) {
                    deleteLesson();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void getModification(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Modify lesson information\n"+
                            "2. Modify learner information\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    modifyLesson();
                    return;
                }
                else if(userChoice == 2) {
                    modifyLearner();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void registerLesson(){
        while(true){
            try {
                System.out.println("Registering new lesson\n" +
                        "Please enter the lesson's date (E.g. 31/5/2015):");
                String userInput = scan.nextLine();
                String[] dateInput = userInput.trim().split("/");
                if (dateInput.length != 3) {
                    System.err.println("You entered an invalid date format.");
                    continue;
                }
                int dayNumber = formatNumberTo2Digits(dateInput[0]);
                int monthNumber = formatNumberTo2Digits(dateInput[1]);
                int yearNumber = Integer.parseInt(dateInput[2].trim());
                if(yearNumber < 1970){
                    System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                    continue;
                }
                LocalTime startTime;
                LocalTime endTime;
                LocalDate dateOfLesson = LocalDate.of(yearNumber, monthNumber, dayNumber);
                try {
                    System.out.println("Please enter the lesson's start time in 24-hour format(E.g. 14:00):");
                    String startTimeInput = scan.nextLine();
                    String startTimeValidInput = makeTime2Digits(startTimeInput);
                    startTime = LocalTime.parse(startTimeValidInput.trim());

                    System.out.println("Please enter the lesson's end time in 24-hour format(E.g. 15:00):");
                    String endTimeInput = scan.nextLine();
                    String endTimeValidInput = makeTime2Digits(endTimeInput);
                    endTime = LocalTime.parse(endTimeValidInput.trim());
                }catch (Exception e){
                    System.err.println("You entered an invalid time.");
                    continue;
                }
                System.out.println("Please enter the lesson grade level: ");
                int gradeLevel = scan.nextInt();

                System.out.println("Please enter the coach name: ");
                String coachName = scan.nextLine();

                Lesson lesson = new Lesson(dateOfLesson, gradeLevel, startTime, endTime, coachName);
                schoolController.addNewLesson(lesson);
                System.out.println("Lesson registered successfully.");
                System.out.println();
                return;
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
            scan.nextLine();
        }
    }

    public void registerLearner(){
        while(true){
            try {
                System.out.println("Registering new learner\n" +
                        "Please enter your name:");
                String name = scan.nextLine();
                if(name.isBlank() || name.isEmpty()) {
                    System.err.println("Please enter a valid name.");
                    continue;
                }

                System.out.println("Please enter your gender(M/F):");
                String gender = scan.next();
                scan.nextLine();

                System.out.println("Please enter your birthday date (E.g. 31/5/2015): ");
                String userInput = scan.nextLine();
                String[] dateInput = userInput.trim().split("/");
                if (dateInput.length != 3) {
                    System.err.println("You entered an invalid date format.");
                    continue;
                }
                int dayNumber = formatNumberTo2Digits(dateInput[0]);
                int monthNumber = formatNumberTo2Digits(dateInput[1]);
                int yearNumber = Integer.parseInt(dateInput[2].trim());
                if(yearNumber < 1970){
                    System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                    continue;
                }
                LocalDate birthDay = LocalDate.of(yearNumber, monthNumber, dayNumber);

                System.out.println("Please enter your emergency phone number (E.g. 07570123123): ");
                String phoneNumber = scan.next();
                scan.nextLine();

                System.out.println("Please enter your grade level: ");
                int gradeLevel = scan.nextInt();
                Learner learner = new Learner(name, gender, birthDay, phoneNumber, gradeLevel);
                schoolController.addNewLearner(learner);
                this.learner = learner;
                System.out.println("Learner registered successfully.");
                System.out.println();
                return;
            }catch (Exception e) {
                System.err.println(e.getMessage());
            }
            scan.nextLine();
        }
    }

    public void getRegister(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Register Learner\n"+
                            "2. Register Lesson\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    registerLearner();
                    return;
                }
                else if(userChoice == 2) {
                    registerLesson();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void getLessonInformation(){
        lesson = getLesson();
        if(lesson == null){
            System.err.println("No lesson was found.");
            return;
        }
        System.out.println(lesson.printFormalLessonInfo());
        System.out.println();
    }

    public void getLearnerInformation(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Learner details\n"+
                            "2. Learner history\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    getLearnerDetails();
                    return;
                }
                else if(userChoice == 2) {
                    getLearnerHistory();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void getLearnerDetails(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }
        System.out.println(learner.printLearner());
        System.out.println();
    }

    public void getLearnerHistory(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }
        try {
            HashMap<Lesson, List<LessonEvent>> listOfLessonsAndEvents = schoolController.getLearnerHistory(learner.getId());
            System.out.println(learner.getName() + "'s booked lessons:");
            List<Lesson> listOfBookedLessons = schoolController.getListOfLessonsByLearner(learner.getId());
            for(int i = 0 ; i < listOfBookedLessons.size(); i++){
                System.out.print((listOfBookedLessons.get(i).printFormalLessonInfo() + "\n").indent(4));
            }
            System.out.println();
            System.out.println("Learner History:");
            int count = 1;
            for(Map.Entry<Lesson, List<LessonEvent>> entry : listOfLessonsAndEvents.entrySet()){
                System.out.println("Lesson: " + entry.getKey().printFormalLessonInfo());
                count = 1;
                if(!entry.getValue().isEmpty()) {
                    System.out.println("Actions:");
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        System.out.print((count + ". " + entry.getValue().get(i).printLessonEvent() + "\n").indent(4));
                        count++;
                    }
                }
                System.out.println();
            }
        }catch (Exception e){System.out.println(e.getMessage());}

    }

    public void getInformation(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Learner information\n"+
                            "2. Lesson information\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    getLearnerInformation();
                    return;
                }
                else if(userChoice == 2) {
                    getLessonInformation();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void getCoachReport(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. A coach's rating report by month\n"+
                            "2. All coaches' rating report\n"+
                            "3. Lessons assigned to coach report\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1) {
                    getCoachReportByMonth();
                    return;
                }
                else if(userChoice == 2){
                    getAllCoachesReport();
                    return;
                }
                else if(userChoice == 3){
                    getCoachLessons();
                    return;
                }
            }catch (Exception e){}
        }
    }

    public void getCoachReportByMonth(){
        while(true) {
            try {
                scan.nextLine();
                System.out.println("Please enter the coach's name:");
                String coachName = scan.nextLine();

                System.out.println("Please enter the month number and year(E.g. 05/2024):");
                String userInput = scan.nextLine();
                String[] dataInput = userInput.trim().split("/");
                if (dataInput.length != 2) continue;

                int monthNumber = Integer.parseInt(dataInput[0].trim());
                if (monthNumber < 1 || monthNumber > 12) {
                    System.err.println("You entered an invalid month number.");
                    continue;
                }

                int yearNumber = Integer.parseInt(dataInput[1].trim());
                if (yearNumber < 1970) {
                    System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                    continue;
                }

                System.out.println(coachName + "'s ratings for that month are:");
                List<LessonEvent> lessonEvents = schoolController.getCoachLessonsEventsByMonth(coachName, monthNumber, yearNumber);
                if (lessonEvents.isEmpty()){
                    System.out.println("No results found.");
                    return;
                }else {
                    double average = 0;
                    List<Double> listOfRatings = new ArrayList<>();
                    for (int i = 1; i < lessonEvents.size(); i++){
                        if(lessonEvents.get(i).getStatus() == 1) {
                            listOfRatings.add((double) lessonEvents.get(i).getRating());
                            average += lessonEvents.get(i).getRating();
                        }
                    }

                    if(listOfRatings.isEmpty()){
                        System.out.println("No results found.");
                        return;
                    }
                    StringBuilder printAverage = new StringBuilder();
                    for (int i = 0 ; i < listOfRatings.size() - 1; i++){
                        if(i == 0){
                            printAverage.append(listOfRatings.get(i));
                        }else{
                            printAverage.append(", ").append(listOfRatings.get(i));
                        }
                    }
                    if(listOfRatings.size() - 1 > 0){
                        printAverage.append(", ").append(listOfRatings.getLast());
                    }
                    System.out.println(printAverage);
                    average = average / listOfRatings.size();
                    System.out.println("The average is: " + average);
                }
                return;
            }catch (Exception e){System.err.println("An error has occurred.");}
        }
    }

    public void getAllCoachesReport(){
        HashMap<String, Double> listOfCoachRatings = schoolController.getAllCoachReport();
        if(listOfCoachRatings.isEmpty()){
            System.out.println("No results found.");
            return;
        }
        System.out.println("The report of all coaches:");
        for(Map.Entry<String, Double> entry : listOfCoachRatings.entrySet()){
            System.out.println(entry.getKey().trim() + " had an average of " + entry.getValue());
        }
        System.out.println();
    }
    public void getCoachLessons(){
        while(true) {
            try {
                System.out.println("Please enter the coach's name:");
                String coachName = scan.nextLine();
                List<Lesson> listOfLessons = schoolController.getListOfLessonsByCoach(coachName);
                if(listOfLessons.isEmpty()){
                    System.out.println("No results found.");
                    return;
                }

                System.out.println("The list of lessons assigned to coach are:");
                for(int i = 0 ; i < listOfLessons.size(); i++){
                    System.out.println(listOfLessons.get(i).printFormalLessonInfo());
                }
                return;
            }catch (Exception e){}
        }
    }

    public void getReports(){
        int userChoice = -1;
        while(true){
            System.out.println(
                    "Please select an action:\n"+
                            "1. Learners report\n"+
                            "2. Coaches report\n"+
                            "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1){
                    getMonthlyLearnerReport();
                    return;
                }else if(userChoice == 2){
                        getCoachReport();
                return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void getLearnerMonthlyReport(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        List<Lesson> listOfLessons;
        while(true) {
            System.out.println("Please enter the month number and year(E.g. 5/2024):");
            try {
                String userInput = scan.nextLine();
                String[] dataInput = userInput.trim().split("/");
                if(dataInput.length != 2) continue;

                int monthNumber = Integer.parseInt(dataInput[0].trim());
                if(monthNumber < 1 || monthNumber > 12){
                    System.err.println("You entered an invalid month number.");
                    continue;
                }

                int yearNumber = Integer.parseInt(dataInput[1].trim());
                if(yearNumber < 1970){
                    System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                    continue;
                }

                listOfLessons = schoolController.getListOfLearnerLessonsByMonth(learner.getId(), monthNumber, yearNumber);
                System.out.println("The monthly report for " + learner.getName() + ":");
                printLearnerLessonReport(learner, listOfLessons);
                return;
            }catch (Exception e){System.err.println("An error has occurred.");}
        }
    }

    public void getAllLearnersMonthlyReport(){
        while(true) {
            scan.nextLine();
            System.out.println("Please enter the month number and year(E.g. 5/2024):");
            try {
                String userInput = scan.nextLine();
                String[] dataInput = userInput.trim().split("/");
                if(dataInput.length != 2) continue;

                int monthNumber = Integer.parseInt(dataInput[0].trim());
                if(monthNumber < 1 || monthNumber > 12){
                    System.err.println("You entered an invalid month number.");
                    continue;
                }

                int yearNumber = Integer.parseInt(dataInput[1].trim());
                if(yearNumber < 1970){
                    System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                    continue;
                }
                List<Lesson> listOfLessons = schoolController.getListOfLessonsByMonth(monthNumber, yearNumber);

                Set<String> listOfLearnerIds = new HashSet<>();
                for(int i = 0 ; i < listOfLessons.size(); i++){
                    for(int j = 0 ; j < listOfLessons.get(i).getLogOfActions().size(); j++){
                        listOfLearnerIds.add(listOfLessons.get(i).getLogOfActions().get(j).getLearnerID());
                    }
                }

                List<Lesson> listOfLessonsPerLearner = new ArrayList<>();
                for (String learnerId : listOfLearnerIds) {
                    System.out.println("*****");
                    listOfLessonsPerLearner = schoolController.getListOfLearnerLessonsByMonth(learnerId, monthNumber, yearNumber);
                    System.out.println("The monthly report for " + schoolController.getLearnerByID(learnerId).getName() + ":");
                    printLearnerLessonReport(schoolController.getLearnerByID(learnerId), listOfLessonsPerLearner);
                    System.out.println("*****");
                }
                return;
            }catch (Exception e){System.err.println("An error has occurred.");}
        }

    }

    public ReportData printLearnerLessonReport(Learner learner, List<Lesson> listOfLessons){
        List<Lesson> listOfBookedLessons = new ArrayList<>();
        List<Lesson> listOfCanceledLessons = new ArrayList<>();
        HashMap<Lesson, LessonEvent> listOfAttendedLessons = new HashMap<>();

        for(int i = 0 ; i < listOfLessons.size(); i++){
            try {
                LessonEvent lessonEvent = schoolController.getLastEventOfLearner(listOfLessons.get(i).getId(), learner.getId());
                if (lessonEvent.getStatus() == 0)
                    listOfBookedLessons.add(listOfLessons.get(i));
                else if (lessonEvent.getStatus() == 1)
                    listOfAttendedLessons.put(listOfLessons.get(i), lessonEvent);
                else if (lessonEvent.getStatus() == -1)
                    listOfCanceledLessons.add(listOfLessons.get(i));
            }catch (Exception e){}
        }

        int numberOfCurrentlyBookedLessons = listOfBookedLessons.size();
        int numberOfCanceledLessons = listOfCanceledLessons.size();
        int numberOfAttendedLessons = listOfAttendedLessons.size();

        System.out.println("List of canceled lessons:");
        if(numberOfCanceledLessons == 0){
            System.out.println("N/A");
        }else {
            for (int i = 0; i < numberOfCanceledLessons; i++) {
                System.out.print(listOfCanceledLessons.get(i).printFormalLessonInfo().indent(4));
            }
        }
        double averageRating = -1;
        System.out.println("List of attended lessons:");
        if(numberOfAttendedLessons == 0){
            System.out.println("N/A");
        }else {
            averageRating = 0;
            for (Map.Entry<Lesson, LessonEvent> entry : listOfAttendedLessons.entrySet()) {
                System.out.print(entry.getKey().printFormalLessonInfo().indent(4));
                System.out.print(("Feedback by learner: " + " comment: " + entry.getValue().getComment() + " | Rating: " + entry.getValue().getRating()).indent(4));
                averageRating += entry.getValue().getRating();
            }
            averageRating = averageRating / numberOfAttendedLessons;
        }

        System.out.println("List of remaining booked lessons:");
        if(numberOfCurrentlyBookedLessons == 0){
            System.out.println("N/A");
        }else {
            for (int i = 0; i < listOfBookedLessons.size(); i++) {
                System.out.print(listOfBookedLessons.get(i).printFormalLessonInfo().indent(4));
            }
        }
        String print = "";
        if(numberOfAttendedLessons == 0){
            print = "\nNumber of total canceled lessons: " + numberOfCanceledLessons +
                    "\nNumber of remaining booked lessons: " + numberOfCurrentlyBookedLessons+
                    "\nNumber of total attended lessons: " + numberOfAttendedLessons;
        }else{
            print = "\nNumber of total canceled lessons: " + numberOfCanceledLessons +
                    "\nNumber of remaining booked lessons: " + numberOfCurrentlyBookedLessons+
                    "\nNumber of total attended lessons: " + numberOfAttendedLessons + " with an average of " + averageRating + " ratings";
        }
        System.out.println(print);

        return new ReportData(numberOfCurrentlyBookedLessons, numberOfCanceledLessons, numberOfAttendedLessons, averageRating);
    }

    public void getMonthlyLearnerReport(){
        int userChoice = -1;
        while(true){
            System.out.println(
                                "Please select an action:\n"+
                                        "1. All learners monthly report\n"+
                                        "2. Specific learner's monthly report\n"+
                                        "0. Exit\n");
            try {
                userChoice = scan.nextInt();
                if(userChoice == 0) return;
                else if(userChoice == 1){
                    getAllLearnersMonthlyReport();
                    return;
                }
                else if(userChoice == 2){
                    getLearnerMonthlyReport();
                    return;
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public void attendLesson(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        lesson = getLessonByLearnerBooked(learner);
        if(lesson == null){
            System.err.println("No lesson was found.");
            return;
        }

        String comment = "";
        int rating = -1;

        boolean flag = true;
        while(flag){
            try{
                System.out.println("Please submit any feedback regarding the lesson:");
                comment = scan.nextLine();
                if(comment.isEmpty() || comment.isBlank())
                    comment = "N/A";
                flag = false;
            }catch (Exception e){}
        }

        flag = true;
        while(flag){
            try{
                System.out.println("Please rate the lesson using 1-5 scale, where 1 is the lowest rating and 5 is the highest:");
                rating = scan.nextInt();
                if(rating <= 5 && rating >= 1){
                    flag = false;
                }
            }catch (Exception e){}
            scan.nextLine();
        }


        System.out.println("Are you sure you want to attend the lesson. Warning: if you are promoted, your invalid bookings will be canceled! (y/n):"+
                "\nLearner: " + learner.getLearnerId() + " : " + learner.getName() +
                "\nLesson: " + lesson.getId() + " : " + lesson.printDate() +
                "\nComment: " + comment +
                "\nRating: " + rating);
        flag = true;
        while(flag) {
            try{
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        boolean gradeUpgraded = schoolController.attendLesson(lesson.getId(), learner.getId(), comment, rating);
                        System.out.println("The lesson has been attended successfully.");
                        if(gradeUpgraded)
                            System.out.println("You have been promoted to a higher grade level. This may affect your previous bookings.");
                        System.out.println();
                    }catch (Exception e){
                        System.err.println(e.getMessage());
                    }
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void changeOrCancelLesson(){

        int userInput = -1;
        Lesson lesson = null;
        while(userInput != 0) {
            System.out.println(
                    "\nPlease select an action:\n"+
                            "1. Change a booking\n"+
                            "2. Cancel a booking\n"+
                            "0. Exit\n");

            try{
                userInput = scan.nextInt();
                if(userInput == 0) return;
                if(userInput == 1){
                    changeBooking();
                    return;
                }else if(userInput == 2){
                    cancelBooking();
                    return;
                }
            }catch (Exception e){System.err.println(e.getMessage());}
        }
    }

    public void cancelBooking(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        System.out.println("\nSelect the lesson to be canceled:");
        lesson = getLessonByLearnerBookedNotAttended(learner);
        if(lesson == null){
            System.err.println("No lesson was found.");
            return;
        }

        boolean flag = true;
        while(flag) {
            try{
                System.out.println("Are you sure you want to cancel the booking (y/n):\nLearner: " + learner.getLearnerId() + " : " + learner.getName() + "\nLesson: " + lesson.getId() + " : " + lesson.printDate());
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        schoolController.cancelLesson(lesson.getId(), learner.getId());
                        System.out.println("The lesson booking has been canceled successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println(e.getMessage());}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void changeBooking(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        System.out.println("\nSelect the lesson to be changed:");
        Lesson previousLesson = getLessonByLearnerBookedNotAttended(learner);
        if(previousLesson == null){
            System.err.println("No lesson was found.");
            return;
        }

        System.out.println("\nSelect the new lesson to book:");
        Lesson newLesson = getLessonByLearner(learner);
        if(newLesson == null){
            System.err.println("No lesson was found.");
            return;
        }

        System.out.println("Are you sure you want to change the booking to (y/n):\nLearner: " + learner.getLearnerId() + " : " + learner.getName() + "\nLesson: " + newLesson.getId() + " : " + newLesson.printDate());
        boolean flag = true;
        while(flag) {
            try{
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    try {
                        schoolController.modifyBooking(learner.getId(), previousLesson, newLesson);
                        lesson = newLesson;
                        System.out.println("The lesson booking has been changed successfully.");
                        System.out.println();
                    }catch (Exception e){System.err.println(e.getMessage());}
                    flag = false;
                }else if (userInput.equalsIgnoreCase("n")) {
                    flag = false;
                }
            }catch (Exception e){}
        }
    }

    public void selectLearner(){
        if(learner != null){
            System.out.println("Would you like to use the previously selected learner (y/n)?\n" +
                    "Learner: " +learner.getLearnerId()+", " + learner.getName());
            try{
                String userInput = scan.next();
                scan.nextLine();
                if (userInput.equalsIgnoreCase("n")){
                    learner = getLearner();
                    return;
                }else if(userInput.equalsIgnoreCase("y")){
                    return;
                }
            }catch (Exception e){System.out.println(e.getMessage());}
        }
        learner = getLearner();
    }

    public void bookLesson(){
        selectLearner();
        if(learner == null){
            System.err.println("No learner was found.");
            return;
        }

        lesson = getLessonByLearner(learner);
        if(lesson == null){
            System.err.println("No lesson was found.");
            return;
        }

        System.out.println("Are you sure you want to book the lesson (y/n):\nLearner: " + learner.getLearnerId() + " : " + learner.getName() + "\nLesson: " + lesson.getId() + " : " + lesson.printDate());
        boolean flag = true;
        while(flag) {
            try{
            String userInput = scan.next();
                scan.nextLine();
            if (userInput.equalsIgnoreCase("y")) {
                try {
                    schoolController.bookLearnerToLesson(learner, lesson);
                    System.out.println("The lesson has been booked successfully.");
                    System.out.println();
                }catch (Exception e){System.err.println(e.getMessage());}
                flag = false;
            }else if (userInput.equalsIgnoreCase("n")) {
                flag = false;
            }
            }catch (Exception e){}
        }
    }

    public Learner getLearner(){
        System.out.println("\nPlease enter the learner's ID:");
        String learnerId = scan.next();
        scan.nextLine();
        List<Learner> listOfLearner = schoolController.getListOfLearnersByLearnerID(learnerId);
        if(listOfLearner.isEmpty()) {
            return null;
        }

        Learner learner = null;
        if(listOfLearner.size() == 1)
            return listOfLearner.getFirst();

        int userInput = 0;

        while(true){
            System.out.println("Multiple learners found with same IDs. Please verify the learner information:");
            for(int i = 0; i < listOfLearner.size(); i++){
                System.out.print(((i+1) + ". " + listOfLearner.get(i).printLearner()).indent(4));
            }
            System.out.println("Select learner number: (Use -1 to exit)");
            try {
                userInput = scan.nextInt();
                if (userInput == -1) return null;
                if((userInput < listOfLearner.size() + 1 && userInput > 0))
                    return listOfLearner.get(userInput - 1);
            }catch (Exception e){ System.err.println("You entered an invalid selection.");}
            scan.nextLine();
        }
    }

    public Lesson getLessonByLearnerBooked(Learner learner){
        int userInput = -1;
        Lesson lesson = null;
        while(true) {
            System.out.println(
                    "Select an option:\n" +
                            "1. Select a lesson through ID\n" +
                            "2. Select a lesson through the date\n" +
                            "3. Find a lesson using the grade\n" +
                            "4. Find a lesson using the day of the week\n" +
                            "5. Find a lesson using the month\n" +
                            "0. Exit\n"
            );
            try{
                userInput = scan.nextInt();
                if (userInput == 0) return null;
                else if(userInput == 1){
                    return getLessonLearnerBookedById(learner);
                }else if(userInput == 2){
                    return getLessonLearnerBookedByDate(learner);
                }else if(userInput == 3){
                    return getLessonLearnerBookedByGrade(learner);
                }else if(userInput == 4){
                    return getLessonLearnerBookedByDayName(learner);
                }else if(userInput == 5){
                    return getLessonLearnerBookedByMonth(learner);
                }else {
                    System.err.println("You entered an invalid option.");
                }
            }catch (Exception e){System.err.println("An error has occurred.");}
            scan.nextLine();
        }
    }

    public Lesson getLessonLearnerBookedByMonth(Learner learner){
        LocalDate localDateNow = LocalDate.now();
        List<Lesson> listOfLesson;
        while(true) {
            System.out.println("Please enter the month number or name(E.g. 5 or May):");
            try {
                String monthName = scan.next();
                scan.nextLine();
                int monthNumber = 0;
                try {
                    monthNumber = Month.valueOf(monthName.trim().toUpperCase()).getValue();
                    listOfLesson = schoolController.getLearnerBookedLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                    return selectLessonFromList(listOfLesson);
                }catch (Exception e){}
                monthNumber = Integer.parseInt(monthName.trim());
                listOfLesson = schoolController.getLearnerBookedLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                return selectLessonFromList(listOfLesson);
            }catch (Exception e){System.err.println("You entered an invalid month.");}
        }
    }

    public Lesson getLessonLearnerBookedByDayName(Learner learner) throws Exception {
        System.out.println("Please enter the day name (E.g. Wednesday):");
        String dayName = scan.next();
        scan.nextLine();
        return selectLessonFromList(schoolController.getLearnerBookedLessonByDayName(learner, dayName));
    }

    public Lesson getLessonLearnerBookedByGrade(Learner learner){
        while(true){
            try {
                scan.nextLine();
                System.out.println("The learner is grade : " + learner.getGradeLevel() + ". Please select a grade level (possible grades are: " + learner.getGradeLevel() + " and " + (learner.getGradeLevel() + 1) + "):");
                String userInput = scan.nextLine();
                int gradeLevel = Integer.parseInt(userInput);
                if (gradeLevel != learner.getGradeLevel() && gradeLevel != learner.getGradeLevel() + 1){
                    System.err.println("You entered an invalid grade level.");
                    return null;
                }
                return selectLessonFromList(schoolController.getLearnerBookedLessonByGrade(learner, Integer.parseInt(userInput.trim())));
            }catch (Exception e){}
        }
    }

    public Lesson getLessonLearnerBookedByDate(Learner learner){
        System.out.println("Please enter lesson date (E.g. 31/5/2024): ");
        String userInput = scan.nextLine();
        String[] dateInput = userInput.trim().split("/");
        int dayNumber;
        int monthNumber;
        int yearNumber;
        try {
            dayNumber = formatNumberTo2Digits(dateInput[0]);
            monthNumber = formatNumberTo2Digits(dateInput[1]);
            yearNumber = Integer.parseInt(dateInput[2].trim());
            if (yearNumber < 1970) {
                System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                return null;
            }
        }catch (Exception e){
            System.err.println("You entered an invalid date.");
            return null;
        }
        try{
            LocalDate localDate = LocalDate.of(yearNumber, monthNumber, dayNumber);
            return schoolController.getLearnerBookedLessonByDate(learner,localDate);
        }catch (Exception e){System.err.println(e.getMessage());};
        return null;
    }

    public Lesson getLessonLearnerBookedById(Learner learner){
        System.out.println("Please enter lesson Id: ");
        String lessonId = scan.next();
        scan.nextLine();
        try{
            return schoolController.getLearnerBookedLessonById(lessonId,learner);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }


    public Lesson getLessonByLearnerBookedNotAttended(Learner learner){
        int userInput = -1;
        Lesson lesson = null;
        while(true) {
            System.out.println(
                    "Select an option:\n" +
                            "1. Select a lesson through ID\n" +
                            "2. Select a lesson through the date\n" +
                            "3. Find a lesson using the grade\n" +
                            "4. Find a lesson using the day of the week\n" +
                            "5. Find a lesson using the month\n" +
                            "0. Exit\n"
            );
            try{
                userInput = scan.nextInt();
                if (userInput == 0) return null;
                else if(userInput == 1){
                    return getLessonLearnerBookedNotAttendedById(learner);
                }else if(userInput == 2){
                    return getLessonLearnerBookedNotAttendedByDate(learner);
                }else if(userInput == 3){
                    return getLessonLearnerBookedNotAttendedByGrade(learner);
                }else if(userInput == 4){
                    return getLessonLearnerBookedNotAttendedByDayName(learner);
                }else if(userInput == 5){
                    return getLessonLearnerBookedNotAttendedByMonth(learner);
                }else {
                    System.err.println("You entered an invalid option.");
                }
            }catch (Exception e){System.err.println("An error has occurred.");}
            scan.nextLine();
        }
    }

    public Lesson getLessonLearnerBookedNotAttendedByMonth(Learner learner){
        LocalDate localDateNow = LocalDate.now();
        List<Lesson> listOfLesson;
        while(true) {
            System.out.println("Please enter the month number or name(E.g. 5 or May):");
            try {
                String monthName = scan.next();
                scan.nextLine();
                int monthNumber = 0;
                try {
                    monthNumber = Month.valueOf(monthName.trim().toUpperCase()).getValue();
                    listOfLesson = schoolController.getLearnerBookedNotAttendedLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                    return selectLessonFromList(listOfLesson);
                }catch (Exception e){}
                monthNumber = Integer.parseInt(monthName.trim());
                listOfLesson = schoolController.getLearnerBookedNotAttendedLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                return selectLessonFromList(listOfLesson);
            }catch (Exception e){System.err.println("You entered an invalid month.");}
        }
    }

    public Lesson getLessonLearnerBookedNotAttendedByDayName(Learner learner){
        System.out.println("Please enter the day name (E.g. Wednesday):");
        String dayName = scan.next();
        scan.nextLine();
        return selectLessonFromList(schoolController.getLearnerBookedNotAttendedLessonByDayName(learner, dayName));
    }

    public Lesson getLessonLearnerBookedNotAttendedByGrade(Learner learner){
        while(true){
            try {
                scan.nextLine();
                System.out.println("The learner is grade : " + learner.getGradeLevel() + ". Please select a grade level (possible grades are: " + learner.getGradeLevel() + " and " + (learner.getGradeLevel() + 1) + "):");
                String userInput = scan.nextLine();
                int gradeLevel = Integer.parseInt(userInput);
                if (gradeLevel != learner.getGradeLevel() && gradeLevel != learner.getGradeLevel() + 1){
                    System.err.println("You entered an invalid grade level.");
                    return null;
                }
                return selectLessonFromList(schoolController.getLearnerBookedNotAttendedLessonByGrade(learner, Integer.parseInt(userInput.trim())));
            }catch (Exception e){}
        }
    }

    public Lesson getLessonLearnerBookedNotAttendedByDate(Learner learner){
        System.out.println("Please enter lesson date (E.g. 31/5/2024): ");
        String userInput = scan.nextLine();
        String[] dateInput = userInput.trim().split("/");
        int dayNumber;
        int monthNumber;
        int yearNumber;
        try {
            dayNumber = formatNumberTo2Digits(dateInput[0]);
            monthNumber = formatNumberTo2Digits(dateInput[1]);
            yearNumber = Integer.parseInt(dateInput[2].trim());
            if (yearNumber < 1970) {
                System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                return null;
            }
        }catch (Exception e){
            System.err.println("You entered an invalid date.");
            return null;
        }
        try{
            LocalDate localDate = LocalDate.of(yearNumber, monthNumber, dayNumber);
            return schoolController.getLearnerBookedNotAttendedLessonByDate(learner,localDate);
        }catch (Exception e){System.err.println(e.getMessage());};
        return null;
    }

    public Lesson getLessonLearnerBookedNotAttendedById(Learner learner){
        System.out.println("Please enter lesson Id: ");
        String lessonId = scan.next();
        scan.nextLine();
        try{
            return schoolController.getLearnerBookedNotAttendedLessonById(lessonId,learner);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }


    public Lesson getLessonByLearner(Learner learner){
        int userInput = -1;
        Lesson lesson = null;
        while(true) {
            System.out.println(
                    "Select an option:\n" +
                            "1. Select a lesson through ID\n" +
                            "2. Select a lesson through the date\n" +
                            "3. Find a lesson using the grade\n" +
                            "4. Find a lesson using the day of the week\n" +
                            "5. Find a lesson using the month\n" +
                            "6. Find a lesson using the coach name\n" +
                            "0. Exit\n"
            );
            try{
                userInput = scan.nextInt();
                if (userInput == 0) return null;
                else if(userInput == 1){
                    return getLessonLearnerById(learner);
                }else if(userInput == 2){
                    return getLessonLearnerByDate(learner);
                }else if(userInput == 3){
                    return getLessonLearnerByGrade(learner);
                }else if(userInput == 4){
                    return getLessonLearnerByDayName(learner);
                }else if(userInput == 5){
                    return getLessonLearnerByMonth(learner);
                }else if(userInput == 6){
                    return getLessonLearnerByCoachName(learner);
                }else {
                    System.err.println("You entered an invalid option.");
                }
            }catch (Exception e){System.err.println("An error has occurred.");}
            scan.nextLine();
        }
    }

    public Lesson getLessonLearnerByCoachName(Learner learner){
        scan.nextLine();
        System.out.println("Please enter the coach name (E.g. John Doe):");
        String coachName = scan.nextLine();
        return selectLessonFromList(schoolController.getLearnerAvailableLessonByCoachName(learner, coachName));
    }

    public Lesson getLessonLearnerByMonth(Learner learner){
        LocalDate localDateNow = LocalDate.now();
        List<Lesson> listOfLesson;
        while(true) {
            System.out.println("Please enter the month number or name(E.g. 5 or May):");
            try {
                String monthName = scan.next();
                scan.nextLine();
                int monthNumber = 0;
                try {
                    monthNumber = Month.valueOf(monthName.trim().toUpperCase()).getValue();
                    listOfLesson = schoolController.getLearnerAvailableLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                    return selectLessonFromList(listOfLesson);
                }catch (Exception e){}
                monthNumber = Integer.parseInt(monthName.trim());
                listOfLesson = schoolController.getLearnerAvailableLessonByMonth(learner, localDateNow.getYear(), monthNumber);
                return selectLessonFromList(listOfLesson);
            }catch (Exception e){System.err.println("You entered an invalid month.");}
        }
    }

    public Lesson getLessonLearnerByDayName(Learner learner){
        System.out.println("Please enter the day name (E.g. Wednesday):");
        String dayName = scan.next();
        scan.nextLine();
        return selectLessonFromList(schoolController.getLearnerAvailableLessonByDayName(learner, dayName));
    }

    public Lesson getLessonLearnerByGrade(Learner learner){
        while(true){
            try {
                scan.nextLine();
                System.out.println("The learner is grade : " + learner.getGradeLevel() + ". Please select a grade level (possible grades are: " + learner.getGradeLevel() + " and " + (learner.getGradeLevel() + 1) + "):");
                String userInput = scan.nextLine();
                int gradeLevel = Integer.parseInt(userInput);
                if (gradeLevel != learner.getGradeLevel() && gradeLevel != learner.getGradeLevel() + 1){
                    System.err.println("You entered an invalid grade level.");
                    return null;
                }
                return selectLessonFromList(schoolController.getLearnerAvailableLessonByGrade(learner, Integer.parseInt(userInput.trim())));
            }catch (Exception e){}
        }
    }

    public Lesson getLessonLearnerByDate(Learner learner){
        System.out.println("Please enter lesson date (E.g. 31/5/2024): ");
        String userInput = scan.nextLine();
        String[] dateInput = userInput.trim().split("/");
        int dayNumber;
        int monthNumber;
        int yearNumber;
        try {
            dayNumber = formatNumberTo2Digits(dateInput[0]);
            monthNumber = formatNumberTo2Digits(dateInput[1]);
            yearNumber = Integer.parseInt(dateInput[2].trim());
            if (yearNumber < 1970) {
                System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                return null;
            }
        }catch (Exception e){
            System.err.println("You entered an invalid date.");
            return null;
        }
        try{
            LocalDate localDate = LocalDate.of(yearNumber, monthNumber, dayNumber);
            return schoolController.getLearnerAvailableLessonByDate(learner, localDate);
        }catch (Exception e){System.err.println(e.getMessage());};
        return null;

    }

    public Lesson getLessonLearnerById(Learner learner){
        System.out.println("Please enter lesson Id: ");
        String lessonId = scan.next();
        scan.nextLine();
        try{
            return schoolController.getLearnerAvailableLessonById(lessonId, learner);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Lesson getLesson(){
        int userInput = -1;
        Lesson lesson = null;
        while(true) {
            System.out.println(
                    "\nSelect an option:\n" +
                            "1. Select a lesson through ID\n" +
                            "2. Select a lesson through the date\n" +
                            "3. Find a lesson using the grade\n" +
                            "4. Find a lesson using the day of the week\n" +
                            "5. Find a lesson using the month\n" +
                            "0. Exit\n"
            );
            try{
                userInput = scan.nextInt();
                if(userInput == 0) return null;
                else if(userInput == 1){
                    return getLessonById();
                }else if(userInput == 2){
                    return getLessonByDate();
                }else if(userInput == 3){
                    return getLessonByGrade();
                }else if(userInput == 4){
                    return getLessonByDayName();
                }else if(userInput == 5){
                    return getLessonByMonth();
                }else {
                    System.err.println("You entered an invalid option.");
                }
            }catch (Exception e){System.err.println("An error has occurred.");}
            scan.nextLine();
        }
    }

    public Lesson getLessonById(){
        System.out.println("Please enter lesson Id: ");
        String lessonId = scan.next();
        scan.nextLine();
        try{
            return schoolController.getLessonById(lessonId);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Lesson getLessonByDate(){
        System.out.println("Please enter lesson date (E.g. 31/5/2024): ");
        String userInput = scan.nextLine();
        String[] dateInput = userInput.trim().split("/");
        int dayNumber;
        int monthNumber;
        int yearNumber;
        try {
            dayNumber = formatNumberTo2Digits(dateInput[0]);
            monthNumber = formatNumberTo2Digits(dateInput[1]);
            yearNumber = Integer.parseInt(dateInput[2].trim());
            if (yearNumber < 1970) {
                System.err.println("You entered an invalid year. Entry should not be less than 1970.");
                return null;
            }
        }catch (Exception e){
            System.err.println("You entered an invalid date.");
            return null;
        }
        try{
            LocalDate localDate = LocalDate.of(yearNumber, monthNumber, dayNumber);
            return schoolController.getLessonByDate(localDate);
        }catch (Exception e){System.err.println(e.getMessage());};
        return null;
    }

    public int formatNumberTo2Digits(String input){
        return Integer.parseInt(String.format("%02d", Integer.parseInt(input)));
    }

    public String makeTime2Digits(String input) throws Exception {
        String[] inputTemp = input.split(":");
        if(inputTemp.length != 2) throw new Exception("Invalid time entry.");
        int startHour = Integer.parseInt(inputTemp[0].trim());
        if(startHour < 0) throw new Exception("Invalid time entry");
        String startHourResult = "";
        if(startHour < 10){
            startHourResult = "0" + startHour;
        }
        int startMinute = Integer.parseInt(inputTemp[1].trim());
        if(startMinute < 0) throw new Exception("Invalid time entry");
        String startMinResult = "";
        if(startMinute < 10){
            startMinResult = "0" + startMinResult;
        }
        return startHourResult.concat(":" + startMinResult);
    }

    public Lesson getLessonByGrade(){
            scan.nextLine();
            System.out.println("Please enter a grade level (E.g. 3):");
            String userInput = scan.nextLine();
            return selectLessonFromList(schoolController.getListOfLessonsByGrade(Integer.parseInt(userInput.trim())));
    }

    public Lesson selectLessonFromList(List<Lesson> listOfLessons){
        if(listOfLessons.isEmpty()) return null;

        if(listOfLessons.size() == 1) return listOfLessons.getFirst();
        while(true) {
            System.out.println("Multiple lessons found through the search. Please select the correct lesson:");

            listOfLessons.sort(Comparator.comparing(Lesson::getDateOfLesson));

            for (int i = 0; i < listOfLessons.size(); i++) {
                System.out.print(((i + 1) + ". " + listOfLessons.get(i).printLesson()).indent(4));
            }
            System.out.println("Select lesson number: (Use -1 to exit)");
            try {
                int userInput = scan.nextInt();
                if (userInput == -1) return null;
                if (userInput < listOfLessons.size() + 1 && userInput > 0) {
                    return listOfLessons.get(userInput - 1);
                } else {
                    System.err.println("You entered an invalid option.");
                }
            }catch (Exception e){}
            scan.nextLine();
        }
    }

    public Lesson getLessonByDayName(){
        System.out.println("Please enter the day name (E.g. Wednesday):");
        String dayName = scan.next();
        scan.nextLine();
        return selectLessonFromList(schoolController.getListOfLessonsByDayName(dayName));
    }

    public Lesson getLessonByMonth(){
        LocalDate localDateNow = LocalDate.now();
        List<Lesson> listOfLesson;
        while(true) {
            System.out.println("Please enter the month number or name(E.g. 5 or May):");
            try {
                String monthName = scan.next();
                scan.nextLine();
                int monthNumber = 0;
                try {
                    monthNumber = Month.valueOf(monthName.trim().toUpperCase()).getValue();
                    listOfLesson = schoolController.getListOfLessonsByMonth(monthNumber, localDateNow.getYear());
                    return selectLessonFromList(listOfLesson);
                }catch (Exception e){}
                monthNumber = Integer.parseInt(monthName.trim());
                listOfLesson = schoolController.getListOfLessonsByMonth(monthNumber, localDateNow.getYear());
                return selectLessonFromList(listOfLesson);
            }catch (Exception e){System.err.println("You entered an invalid month.");}
        }
    }

    public void exitMessage(){
        System.out.println("Exiting program! Thank you for using " + getProgramName());
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public class ReportData{
        int numberOfCurrentlyBookedLessons = 0;
        int numberOfCanceledLessons = 0;
        int numberOfAttendedLessons = 0;
        double averageRating = 0;
        ReportData(int numberOfCurrentlyBookedLessons, int numberOfCanceledLessons,
                   int numberOfAttendedLessons, double averageRating){
            this.numberOfCanceledLessons = numberOfCanceledLessons;
            this.numberOfAttendedLessons = numberOfAttendedLessons;
            this.numberOfCurrentlyBookedLessons = numberOfCurrentlyBookedLessons;
            this.averageRating = averageRating;
        }
    }
}
