package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_content_main);
        auth = FirebaseAuth.getInstance();
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, loginFragment).commit();
    }

    public void signUp(View view){
        startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));
    }

    public void logIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Log in successfully");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", auth.getCurrentUser());
                startActivity(intent);
            } else {
                Log.d(TAG, "Login failed");
                Toast.makeText(LoginActivity.this, "Incorrect email or password. Please try again.",
                Toast.LENGTH_SHORT).show();
            }
        });
    }
}

