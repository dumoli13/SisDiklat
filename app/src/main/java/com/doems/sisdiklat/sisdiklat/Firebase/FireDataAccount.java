package com.doems.sisdiklat.sisdiklat.Firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FireDataAccount {

    public static final String COLLECTION_USER = "account";
    public static final String EMAIL = "email";
    public static final String UID = "uID";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";

    public DatabaseReference ref;
    private Context mcontext;

    public FireDataAccount(String uID){
        ref = FireData.getRefDatabase().child(uID).child(COLLECTION_USER);
    }

    public FireDataAccount(Context mContext){
        this.mcontext = mContext;
        ref = FireData.getRefDatabase().child(COLLECTION_USER);
    }

    public void writeAccount(String uID, String email, String password, String phone, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(UID, uID);
        objectMap.put(EMAIL, email);
        objectMap.put(PASSWORD, password);
        objectMap.put(PHONE, phone);

        ref.updateChildren(objectMap, completionListener);
    }

    public void writePhone(String phone, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(PHONE, phone);

        ref.updateChildren(objectMap, completionListener);
    }

    public void writePassword(String password, DatabaseReference.CompletionListener completionListener){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(PASSWORD, password);

        ref.updateChildren(objectMap, completionListener);
    }
}
