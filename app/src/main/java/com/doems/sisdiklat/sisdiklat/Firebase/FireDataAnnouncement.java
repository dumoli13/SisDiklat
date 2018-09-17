package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FireDataAnnouncement {

    public static final String COLLECTION_USER = "announcement";
    public static final String NAME = "name";
    public static final String URL = "url";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataAnnouncement(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataAnnouncement(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeAdress(String name, String url, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(NAME, name);
        objectMap.put(URL, url);

        ref.child(ref.push().getKey()).updateChildren(objectMap, completionListener);
    }
}

