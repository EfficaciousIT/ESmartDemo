package com.mobi.efficacious.ESmartDemo.entity;

public class Event {
    private String FromDate;
    private String ToDate;

    public String getEvent_id() {
        return Event_id;
    }

    public void setEvent_id(String event_id) {
        Event_id = event_id;
    }

    private String Event_id;
    public String getDtRegistrartionStartDate() {
        return dtRegistrartionStartDate;
    }

    public void setDtRegistrartionStartDate(String dtRegistrartionStartDate) {
        this.dtRegistrartionStartDate = dtRegistrartionStartDate;
    }

    public String getDtRegistrationEndDate() {
        return dtRegistrationEndDate;
    }

    public void setDtRegistrationEndDate(String dtRegistrationEndDate) {
        this.dtRegistrationEndDate = dtRegistrationEndDate;
    }

    public String getDtEventStartDate() {
        return dtEventStartDate;
    }

    public void setDtEventStartDate(String dtEventStartDate) {
        this.dtEventStartDate = dtEventStartDate;
    }

    public String getDtEventEndDate() {
        return dtEventEndDate;
    }

    public void setDtEventEndDate(String dtEventEndDate) {
        this.dtEventEndDate = dtEventEndDate;
    }

    public String getVchEventName() {
        return vchEventName;
    }

    public void setVchEventName(String vchEventName) {
        this.vchEventName = vchEventName;
    }

    public String getVchEventFees() {
        return vchEventFees;
    }

    public void setVchEventFees(String vchEventFees) {
        this.vchEventFees = vchEventFees;
    }

    public String getVchEventDescription() {
        return vchEventDescription;
    }

    public void setVchEventDescription(String vchEventDescription) {
        this.vchEventDescription = vchEventDescription;
    }

    private String dtRegistrartionStartDate;
    private String dtRegistrationEndDate;
    private String dtEventStartDate;
    private String dtEventEndDate;
    private String vchEventName;
    private String vchEventFees;
    private String vchEventDescription;

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getEventFee() {
        return EventFee;
    }

    public void setEventFee(String eventFee) {
        EventFee = eventFee;
    }

    private String EventName;
    private String EventDescription;
    private String EventFee;
    public Event()
    {

    }
}
