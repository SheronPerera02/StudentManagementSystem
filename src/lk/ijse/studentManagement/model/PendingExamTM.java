package lk.ijse.studentManagement.model;

public class PendingExamTM {
    private String subject;
    private String exam;
    private String date;
    private String start;
    private String end;

    public PendingExamTM(String subject, String exam, String date, String start, String end) {
        this.subject = subject;
        this.exam = exam;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public PendingExamTM() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "PendingExamTM{" +
                "subject='" + subject + '\'' +
                ", exam='" + exam + '\'' +
                ", date='" + date + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
