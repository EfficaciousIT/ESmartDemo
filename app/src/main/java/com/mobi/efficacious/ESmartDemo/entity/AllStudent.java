package com.mobi.efficacious.ESmartDemo.entity;

public class AllStudent {
    private int Student_id;
    private int Student_number;
    private int Division_id;
    private int id;
    private int Standard_id;
    private String Name;
    private String student_attendence;
    private boolean selected;
    private String Gender;
    private String FCMToken;
    private boolean p_selected;
    private String Standrad_name;
    private String Division_name;
    private String StudentProfile;
    public AllStudent()
    {

    }

    public AllStudent(int student_id, int student_number, int division_id, int id, int standard_id, String name, String student_attendence, boolean selected, String FCMToken) {
        Student_id = student_id;
        Student_number = student_number;
        Division_id = division_id;
        this.id = id;
        Standard_id = standard_id;
        Name = name;
        this.student_attendence = student_attendence;
        this.selected = selected;
        this.FCMToken=FCMToken;
    }

    public int getStudent_id() {
        return Student_id;
    }

    public void setStudent_id(int student_id) {
        Student_id = student_id;
    }

    public int getStudent_number() {
        return Student_number;
    }

    public void setStudent_number(int student_number) {
        Student_number = student_number;
    }

    public int getDivision_id() {
        return Division_id;
    }

    public void setDivision_id(int division_id) {
        Division_id = division_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStandard_id() {
        return Standard_id;
    }

    public void setStandard_id(int standard_id) {
        Standard_id = standard_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudent_attendence() {
        return student_attendence;
    }

    public void setStudent_attendence(String student_attendence) {
        this.student_attendence = student_attendence;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
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

    public String getStandrad_name() {
        return Standrad_name;
    }

    public void setStandrad_name(String standrad_name) {
        Standrad_name = standrad_name;
    }

    public String getDivision_name() {
        return Division_name;
    }

    public void setDivision_name(String division_name) {
        Division_name = division_name;
    }

    public String getStudentProfile() {
        return StudentProfile;
    }

    public void setStudentProfile(String studentProfile) {
        StudentProfile = studentProfile;
    }
}