package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class FireDataSchedule {

    public static final String COLLECTION_USER = "schedule";
    public static final String EVENT = "event";
    public static final String ROOMID = "roomID";
    public static final String ROOMCAP = "roomCap";
    public static final String ROOMNAME = "roomName";
    public static final String SPEAKER = "speaker";
    public static final String PARTICIPANT = "participant";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataSchedule(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataSchedule(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeSchedule(String event, String roomID,String roomCap, String roomName, String speaker, String participant,
            Date startDate, Date endDate, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        String uID = startDate.getYear()*10000+startDate.getMonth()*100+startDate.getDate()+roomName;
        objectMap.put(EVENT, event);
        objectMap.put(ROOMID, roomID);
        objectMap.put(ROOMCAP, roomCap);
        objectMap.put(ROOMNAME, roomName);
        objectMap.put(SPEAKER, speaker);
        objectMap.put(PARTICIPANT, participant);
        objectMap.put(STARTDATE, startDate);
        objectMap.put(ENDDATE, endDate);
        ref.child(uID).updateChildren(objectMap, completionListener);
    }

    public void editSchedule(String uID, String event, String roomID,String roomCap, String roomName, String speaker, String participant,
                              Date startDate, Date endDate, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(EVENT, event);
        objectMap.put(ROOMID, roomID);
        objectMap.put(ROOMCAP, roomCap);
        objectMap.put(ROOMNAME, roomName);
        objectMap.put(SPEAKER, speaker);
        objectMap.put(PARTICIPANT, participant);
        objectMap.put(STARTDATE, startDate);
        objectMap.put(ENDDATE, endDate);

        ref.child(uID).updateChildren(objectMap, completionListener);
    }
}

