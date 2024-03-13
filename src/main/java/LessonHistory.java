import java.time.LocalDate;

public class LessonHistory {

    private String learnerID;
    private LocalDate dateOfRecord;
    private int status;
    private String comment;

    /**
     *
     * @param learnerID
     * @param status The status should be set as -1 for canceled, 0 for booked and 1 for attended.
     */
    public LessonHistory(String learnerID, int status) {
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
    }

    public LessonHistory(String learnerID, int status, String comment) {
        setLearnerID(learnerID);
        dateOfRecord = LocalDate.now();
        setStatus(status);
        setComment(comment);
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
}
