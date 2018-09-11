package com.doems.sisdiklat.sisdiklat.Model;

public class ModelRunningText {

    private String text;
    private String uID;

    public ModelRunningText(){

    }

    public ModelRunningText(String text){
        this.text = text;
    }

    public ModelRunningText(String uID, String text){
        this.uID = uID;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getuID() {
        return uID;
    }
}
