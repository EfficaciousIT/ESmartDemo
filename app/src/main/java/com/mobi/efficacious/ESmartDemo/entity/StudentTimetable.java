package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF on 2/15/2017.
 */

public class StudentTimetable {

    private String Day;
    private String LectureName;
    private String SubjectName;
    private String FromTime;
    private String Totime;
    private String Time;
    private String Recess;
    private String TeacherName;

    public StudentTimetable()
    {

    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getLectureName() {
        return LectureName;
    }

    public void setLectureName(String lectureName) {
        LectureName = lectureName;
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

    public String getTotime() {
        return Totime;
    }

    public void setTotime(String totime) {
        Totime = totime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getRecess() {
        return Recess;
    }

    public void setRecess(String recess) {
        Recess = recess;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }


}
