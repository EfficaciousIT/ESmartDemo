package com.mobi.efficacious.ESmartDemo.entity;

/**
 * Created by EFF on 2/23/2017.
 */

public class Liabrary {

    private String BookassignId;
    private String standardId;
    private String divisionId;
    private String studentId;
    private String BookDetail;
    private String assignedDate;
    private String returnDate;
    private String status;

    public Liabrary()
    {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getBookDetail() {
        return BookDetail;
    }

    public void setBookDetail(String bookDetail) {
        BookDetail = bookDetail;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public String getBookassignId() {
        return BookassignId;
    }

    public void setBookassignId(String bookassignId) {
        BookassignId = bookassignId;
    }

}
