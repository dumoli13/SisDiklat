package com.doems.sisdiklat.sisdiklat.Model;

import java.util.Date;
import java.util.ArrayList;

public class ModelUnavailableDate {

    private ArrayList<Date> date;

    public ModelUnavailableDate(){

    }

    public ModelUnavailableDate(ArrayList<Date> date){
        this.date = date;
    }

    public ArrayList<Date> getDate() {
        return date;
    }
}
