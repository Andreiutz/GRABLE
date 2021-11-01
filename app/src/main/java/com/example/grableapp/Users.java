package com.example.grableapp;

import android.widget.TextView;
///
public class Users {
    public static String name;
    private String email;
    private String password;
    private String Uid;
    public static  String username;

    public Users() {
    }

    public Users(String email, String password, String uid) {
        this.email = email;
        Uid = uid;
        this.password= password;
    }

    public String getName(){ return name; }

    private void setUsername(String username) {this.username = username;}

    private String getUsername() {return this.username;}

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public String getUid() { return Uid; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) { this.password = password; }

    public void setUid(String uid) { Uid = uid; }


}
