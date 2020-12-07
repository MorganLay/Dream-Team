package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePageActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private final String TAG = "WelcomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(WelcomePageActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        setContentView(R.layout.activity_welcome_page);
    }

    public void logInAction(View view){
        startActivity(new Intent(WelcomePageActivity.this, LoginActivity.class));
    }

    public void guestLogInAction(View view){
        auth.signInAnonymously().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Log.d(TAG, "sign in anonymous successfully");
                Intent intent = new Intent(WelcomePageActivity.this, MainActivity.class);
                intent.putExtra("guest", user);
                startActivity(intent);
            } else {
                Log.d(TAG, "sign in anonymous failed");
            }
        });
    }
}

