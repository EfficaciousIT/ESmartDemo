package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF-4 on 3/13/2018.
 */

public class Teacher_attendence {
    private String DepartmentName;
    private String TeacherDesignation;
    private String TeacherName;
    private int TeacherId;
    private int ID;
    private String TeacherAttandence;
    private boolean selected;
    private boolean p_selected;
    private String FCMToken;

    public Teacher_attendence(int ID, int teacherId, String teacherDesignation, String departmentName, String teacherName, String teacherAttandence, boolean selected, boolean p_selected, String FCMToken) {
        DepartmentName = departmentName;
        TeacherDesignation = teacherDesignation;
        TeacherName = teacherName;
        TeacherId = teacherId;
        this.ID = ID;
        TeacherAttandence = teacherAttandence;
        this.selected = selected;
        this.p_selected=p_selected;
        this.FCMToken=FCMToken;
    }

    public Teacher_attendence() {

    }

    public Teacher_attendence(int ID, int TeacherId, String TeacherDesignation, String DepartmentName, String TeacherName, String TeacherAttandence )
    {

        this.ID=ID;
        this.TeacherId=TeacherId;
        this.TeacherDesignation=TeacherDesignation;
        this.DepartmentName=DepartmentName;
        this.TeacherName=TeacherName;
        this.TeacherAttandence=TeacherAttandence;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getTeacherDesignation() {
        return TeacherDesignation;
    }

    public void setTeacherDesignation(String teacherDesignation) {
        TeacherDesignation = teacherDesignation;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }


    public int getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(int teacherId) {
        TeacherId = teacherId;
    }

    public String getTeacherAttandence() {
        return TeacherAttandence;
    }

    public void setTeacherAttandence(String teacherAttandence) {
        TeacherAttandence = teacherAttandence;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public boolean isP_selected() {
        return p_selected;
    }

    public void setP_selected(boolean p_selected) {
        this.p_selected = p_selected;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }
}
