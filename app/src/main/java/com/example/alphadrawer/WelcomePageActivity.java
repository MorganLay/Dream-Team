package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.login.LoginFragment;

public class WelcomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }

    public void logInAction(View view){
        startActivity(new Intent(WelcomePageActivity.this, LoginActivity.class));
    }

    public void guestLogInAction(View view){
        Intent intent = new Intent(WelcomePageActivity.this, MainActivity.class);
        Boolean user = false;
        intent.putExtra("guest", user);
        startActivity(intent);
    }
}

