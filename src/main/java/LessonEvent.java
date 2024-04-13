import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LessonEvent {


    private String ID;
    private String learnerID;
    private LocalDate dateOfRecord;
    private int status;
    private String comment;
    //numerical rating of the lesson ranging from 1 to 5 (1: Very dissatisfied, 2: Dissatisfied, 3: Ok, 4: Satisfied, 5: Very Satisfied)
    private int rating;
    private String coachName;

    /**
     *
     * @param learnerID
     * @param status The status should be set as -1 for canceled, 0 for booked and 1 for attended.
     */
    public LessonEvent(String key, String learnerID, int status, String coachName) {
        setID(key);
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
        setCoachName(coachName);
    }

    public LessonEvent(String key, String learnerID, int status, String comment, int rating, String coachName) throws Exception {
        setID(key);
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
        setComment(comment);
        setRating(rating);
        setCoachName(coachName);
    }

    public LessonEvent(){};

    public void setRating(int rating){
        this.rating = rating;
    }

    public String getLearnerID() {
        return learnerID;
    }

    public void setLearnerID(String learnerID) {
        this.learnerID = learnerID;
    }

    public LocalDate getDateOfRecord() {
        return dateOfRecord;
    }

    public void setDateOfRecord(LocalDate dateOfRecord) {
        this.dateOfRecord = dateOfRecord;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        if(ID == null || ID.isEmpty()) return;
        if(ID.startsWith("BREF_")) {
            this.ID = ID;
            return;
        }
        this.ID = "BREF_" + ID;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName.trim();
    }

    public int getRating() {
        return rating;
    }

    public String printDate(){
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return getDateOfRecord().format(inputDateFormatter);
    }
    public String printLessonEvent(){
        String result = "";
        if(getStatus() == 0){
            result = "ID: " + getID() + ", booked with " + getCoachName().trim() + ". Date of recorded event: " + printDate();
        }else if(getStatus() == 1){
            result = "ID: " + getID() + ", attended with " + getCoachName().trim() + ". A rating of " + getRating() + " was added with a comment: " + getComment() + ". Date of recorded event: " + printDate();
        }else if(getStatus() == -1) {
            result = "ID: " + getID() + ", canceled with " + getCoachName().trim() + ". Date of recorded event: " + printDate();
        }
        return result;
    }
}
