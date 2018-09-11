package com.doems.sisdiklat.sisdiklat.Model;

import java.util.Calendar;

public class ModelUnavailableDate {

    private Calendar[] date;

    public ModelUnavailableDate(){

    }

    public ModelUnavailableDate(Calendar[] date){
        this.date = date;
    }

    public Calendar[] getDate() {
        return date;
    }
}
