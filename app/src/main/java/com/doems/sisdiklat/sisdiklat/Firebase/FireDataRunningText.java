package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FireDataRunningText {

    public static final String COLLECTION_USER = "runningtext";
    public static final String TEXT = "text";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataRunningText(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataRunningText(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeText(String text, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(TEXT, text);

        ref.child(ref.push().getKey()).updateChildren(objectMap, completionListener);
    }

}


