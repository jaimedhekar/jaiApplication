package com.example.jaiapp;

public class User {
    public String userName;
    public String email;
    public static int ID = 1111;

    public User(){
        userName = "";
        email = "";
    }

    public User(String uName, String e){
        userName = uName;
        email = e;
        ID++;
    }

    public int getID(){return ID;}
}
