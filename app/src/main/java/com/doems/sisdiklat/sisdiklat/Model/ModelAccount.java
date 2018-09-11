package com.doems.sisdiklat.sisdiklat.Model;

public class ModelAccount {
    private String uID;
    private String email;
    private String password;
    private String phone;

    public ModelAccount(){

    }

    public ModelAccount(String uID, String email, String password, String phone){
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public ModelAccount(String password, String phone){
        this.password = password;
        this.phone = phone;
    }

    public String getuID() {
        return uID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
