package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF on 2/18/2017.
 */

public class NoticeBoard {
    private String Subject;
    private String Notice;
    private String IssueDate;
    private String EndDate;
    private String Notice_id;

    public NoticeBoard()
    {

    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(String issueDate) {
        IssueDate = issueDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getIntNotice_id() {
        return Notice_id;
    }

    public void setIntNotice_id(String intNotice_id) {
        this.Notice_id = intNotice_id;
    }

}
