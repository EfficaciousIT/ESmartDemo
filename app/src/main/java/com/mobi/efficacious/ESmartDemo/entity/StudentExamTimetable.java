package com.mobi.efficacious.ESmartDemo.entity;

public class StudentExamTimetable {
    private String ExamSchedule_id;
    private String Examination_date;
    private String DayName;
    private String SubjectName;
    private String FromTime;
    private String ToTime;
    private String subject_id;
    private String ExamName;
private String Examination_Time;
    public StudentExamTimetable()
    {

    }

    public String getExamSchedule_id() {
        return ExamSchedule_id;
    }

    public void setExamSchedule_id(String examSchedule_id) {
        ExamSchedule_id = examSchedule_id;
    }

    public String getExamination_date() {
        return Examination_date;
    }

    public void setExamination_date(String examination_date) {
        Examination_date = examination_date;
    }

    public String getDayName() {
        return DayName;
    }

    public void setDayName(String dayName) {
        DayName = dayName;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String examName) {
        ExamName = examName;
    }

    public String getExamination_Time() {
        return Examination_Time;
    }

    public void setExamination_Time(String examination_Time) {
        Examination_Time = examination_Time;
    }
}
