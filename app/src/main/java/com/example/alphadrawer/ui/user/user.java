package com.example.alphadrawer.ui.user;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class user {

    public String username;
    public String email;

    public user(){}

    public user(String username, String email){
        this.username = username;
        this.email = email;
    }
}
