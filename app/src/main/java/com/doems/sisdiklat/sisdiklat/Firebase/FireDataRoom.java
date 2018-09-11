package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FireDataRoom {

    public static final String COLLECTION_USER = "room";
    public static final String NAME = "name";
    public static final String CAPACITY = "capacity";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataRoom(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataRoom(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeRoom(String name, String capacity, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(NAME, name);
        objectMap.put(CAPACITY, capacity);

        ref.child(ref.push().getKey()).updateChildren(objectMap, completionListener);
    }

    public void modifyRoom(String uID, String name, String capacity, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(NAME, name);
        objectMap.put(CAPACITY, capacity);

        ref.child(uID).updateChildren(objectMap, completionListener);
    }
}

