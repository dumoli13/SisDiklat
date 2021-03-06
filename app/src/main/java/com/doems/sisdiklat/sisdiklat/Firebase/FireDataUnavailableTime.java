package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireDataUnavailableTime {

    public static final String COLLECTION_USER = "unavailableTime";
    public static final String DATE = "date";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataUnavailableTime(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataUnavailableTime(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeUnavailable(String roomID, String scheduleID, ArrayList<Date> date, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(DATE, date);

        ref.child(roomID).child(scheduleID).updateChildren(objectMap, completionListener);
    }
}