package com.example.jaiapp;

public class User {
    public String userName;
    public String email;

    public String password;
    public static int ID = 1111;

    public User(){
        userName = "";
        email = "";
        password ="";
    }

    public User(String uName, String e, String p){
        userName = uName;
        email = e;
        password = p;
        ID++;
    }

    public int getID(){return ID;}

    public String getUserName(){return userName;}

    public String getEmail(){return email;}
}
