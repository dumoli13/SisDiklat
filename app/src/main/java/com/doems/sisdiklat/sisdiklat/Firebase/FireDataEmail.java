package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FireDataEmail {

    public static final String COLLECTION_USER = "email";
    public static final String EMAIL = "email";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataEmail(){
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public FireDataEmail(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeEmail(String email, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        String _key = ref.push().getKey();
        objectMap.put(_key, email);

        ref.updateChildren(objectMap, completionListener);
    }
}

