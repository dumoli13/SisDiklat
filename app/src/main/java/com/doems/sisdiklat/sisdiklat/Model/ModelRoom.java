package com.doems.sisdiklat.sisdiklat.Model;

public class ModelRoom {

    private String uID;
    private String name;
    private String capacity;

    public ModelRoom(){

    }

    public ModelRoom(String uID, String name, String capacity){
        this.uID = uID;
        this.name = name;
        this.capacity = capacity;
    }

    public ModelRoom(String name, String capacity){
        this.name = name;
        this.capacity = capacity;
    }

    public String getuID() {
        return uID;
    }

    public String getName() {
        return name;
    }

    public String getCapacity() {
        return capacity;
    }
}
