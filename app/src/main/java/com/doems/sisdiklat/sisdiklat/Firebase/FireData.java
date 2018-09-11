package com.doems.sisdiklat.sisdiklat.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireData {

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference refDatabase;

    private static FirebaseStorage mStorage;
    private static StorageReference refStorage;

    public static DatabaseReference getRefDatabase() {
        if(mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            refDatabase = mDatabase.getReference("sisdiklat");
        }

        return refDatabase;
    }

    public static StorageReference getRefStorage(){
        if(mStorage == null){
            mStorage = FirebaseStorage.getInstance();
            refStorage = mStorage.getReference();
        }

        return refStorage;
    }
}
