package com.example.alphadrawer.ui.user;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class user {

    public String username;
    public String email;
    @Nullable
    public Integer age;
    @Nullable
    public String gender;
    @Nullable
    public String address;

    public user(){}

    public user(String username, String email){
        this.username = username;
        this.email = email;
        this.age = null;
        this.gender = null;
        this.address = null;
    }
}
