package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomePageActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user = auth.getCurrentUser();
    private String TAG = "WelcomePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
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

