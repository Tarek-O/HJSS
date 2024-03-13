import java.time.LocalDate;

public class LessonEvent {

    private String ID;
    private String learnerID;
    private LocalDate dateOfRecord;
    private int status;
    private String comment;
    //numerical rating of the lesson ranging from 1 to 5 (1: Very dissatisfied, 2: Dissatisfied, 3: Ok, 4: Satisfied, 5: Very Satisfied)
    private int rating;

    /**
     *
     * @param learnerID
     * @param status The status should be set as -1 for canceled, 0 for booked and 1 for attended.
     */
    public LessonEvent(String key, String learnerID, int status) {
        setID(key);
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
    }

    public LessonEvent(String key, String learnerID, int status, String comment, int rating) throws Exception {
        setID(key);
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
        setComment(comment);
        setRating(rating);
    }

    public LessonEvent(){};

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) throws Exception {
        if(rating < 0 && rating > 5) throw new Exception("The rating is invalid. Please select a valid rating between 1 and 5");
        this.rating = rating;
    }
}
