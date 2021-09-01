package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by Hemant on 01-10-17.
 */

public class TeacherAttendaceDetail {
    private String Date;
    private String TeacherId;
    private String Name;
    private String Status;

    public TeacherAttendaceDetail()
    {

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(String teacherId) {
        TeacherId = teacherId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
