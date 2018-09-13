package com.doems.sisdiklat.sisdiklat.Model;

import java.util.Date;

public class ModelSchedule {
    private String uID;
    private String event;
    private String roomID;
    private String roomCap;
    private String roomName;
    private String speaker;
    private String participant;
    private Date startDate;
    private Date endDate;

    public ModelSchedule(){

    }

    public ModelSchedule(String uID, String event, String roomID, String roomName, String speaker,
                         String participant, Date startDate, Date endDate){
        this.uID = uID;
        this.event = event;
        this.roomID = roomID;
        this.roomName = roomName;
        this.speaker = speaker;
        this.participant = participant;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ModelSchedule(String uID, String event, String roomID, String roomCap, String roomName, String speaker,
                         String participant, Date startDate, Date endDate){
        this.uID = uID;
        this.event = event;
        this.roomID = roomID;
        this.roomCap = roomCap;
        this.roomName = roomName;
        this.speaker = speaker;
        this.participant = participant;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getuID() {
        return uID;
    }

    public String getEvent() {
        return event;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomCap() {
        return roomCap;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getParticipant() {
        return participant;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
