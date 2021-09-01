package com.mobi.efficacious.ESmartDemo.entity;

public class LeaveApproval {

    private String LeaveApp_id;
    private String Reason;
    private String From_date;
    private String To_Date;
    private String TotalDays;
    private String Name;
    private String AdminApproval;
    private String TeacherProfile;
    private String IntTeacher_id;
    private String intStudent_id;
    private String StudentProfile;
    public LeaveApproval()
    {

    }

    public String getLeaveApp_id() {
        return LeaveApp_id;
    }

    public void setLeaveApp_id(String leaveApp_id) {
        LeaveApp_id = leaveApp_id;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getFrom_date() {
        return From_date;
    }

    public void setFrom_date(String from_date) {
        From_date = from_date;
    }

    public String getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(String to_Date) {
        To_Date = to_Date;
    }

    public String getTotalDays() {
        return TotalDays;
    }

    public void setTotalDays(String totalDays) {
        TotalDays = totalDays;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdminApproval() {
        return AdminApproval;
    }

    public void setAdminApproval(String adminApproval) {
        AdminApproval = adminApproval;
    }


    public String getTeacherProfile() {
        return TeacherProfile;
    }

    public void setTeacherProfile(String teacherProfile) {
        TeacherProfile = teacherProfile;
    }

    public String getIntTeacher_id() {
        return IntTeacher_id;
    }

    public void setIntTeacher_id(String intTeacher_id) {
        IntTeacher_id = intTeacher_id;
    }

    public String getIntStudent_id() {
        return intStudent_id;
    }

    public void setIntStudent_id(String intStudent_id) {
        this.intStudent_id = intStudent_id;
    }

    public String getStudentProfile() {
        return StudentProfile;
    }

    public void setStudentProfile(String studentProfile) {
        StudentProfile = studentProfile;
    }
}
