package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser loggedIn = auth.getCurrentUser();
        if(loggedIn != null){
            Intent intent = new Intent(WelcomePageActivity.this, MainActivity.class);
            Boolean user = true;
            String email = loggedIn.getEmail();
            intent.putExtra("user", user);
            intent.putExtra("email", email);
            startActivity(intent);
        }

        setContentView(R.layout.activity_welcome_page);
    }

    public void logInAction(View view){
        startActivity(new Intent(WelcomePageActivity.this, LoginActivity.class));
    }

    public void guestLogInAction(View view){
        Intent intent = new Intent(WelcomePageActivity.this, MainActivity.class);
        intent.putExtra("user", false);
        startActivity(intent);
    }
}

