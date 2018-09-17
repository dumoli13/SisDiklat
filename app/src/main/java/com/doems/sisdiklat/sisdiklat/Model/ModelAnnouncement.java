package com.doems.sisdiklat.sisdiklat.Model;

import android.graphics.Bitmap;

public class ModelAnnouncement {
    private String uID;
    private String name;
    private String url;
    private Bitmap bitmapImage;

    public ModelAnnouncement(){

    }

    public ModelAnnouncement(String name, String url){
        this.name = name;
        this.url = url;
    }

    public ModelAnnouncement(String uID, String name, String url){
        this.uID = uID;
        this.name = name;
        this.url = url;
    }

    public ModelAnnouncement(String uID, String name, String url,Bitmap bitmapImage){
        this.uID = uID;
        this.name = name;
        this.url = url;
        this.bitmapImage = bitmapImage;
    }

    public String getuID() {
        return uID;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }
}
