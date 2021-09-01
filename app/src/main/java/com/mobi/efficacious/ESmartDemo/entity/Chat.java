package com.mobi.efficacious.ESmartDemo.entity;

public class Chat {
private String Teacher_id;
    private String UserType_id;
    public String getStandardId() {
        return StandardId;
    }

    public void setStandardId(String standardId) {
        StandardId = standardId;
    }

    public String getDivisionId() {
        return DivisionId;
    }

    public void setDivisionId(String divisionId) {
        DivisionId = divisionId;
    }

    private String StandardId;
    private String DivisionId;
    private boolean selected;
    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    private String User_id;
    private String UserName;
    private String FCMToken;
    public Chat() {
    }
    public Chat(String user_id, String userName, String FCMToken) {
        User_id = user_id;
        UserName = userName;
        this.FCMToken = FCMToken;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUserType_id() {
        return UserType_id;
    }

    public void setUserType_id(String userType_id) {
        UserType_id = userType_id;
    }

    public String getTeacher_id() {
        return Teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        Teacher_id = teacher_id;
    }
}
