package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF-4 on 9/3/2017.
 */

public class AllTeacher {
    private String Dept;
    private String Designation;
    private String Name;
    private String Teacher_id;
    private String Teacher_profile;
    public AllTeacher()
    {

    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTeacher_id() {
        return Teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        Teacher_id = teacher_id;
    }

    public String getTeacher_profile() {
        return Teacher_profile;
    }

    public void setTeacher_profile(String teacher_profile) {
        Teacher_profile = teacher_profile;
    }
}
