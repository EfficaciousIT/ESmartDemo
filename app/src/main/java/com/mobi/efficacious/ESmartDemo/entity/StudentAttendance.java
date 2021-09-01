package com.mobi.efficacious.ESmartDemo.entity;

public class StudentAttendance {

    private String Date;
    private String StudentId;
    private String Name;
    private String Status;

    public StudentAttendance()
    {

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
