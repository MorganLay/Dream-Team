package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.newAccount.newAccountFragment;
import com.example.alphadrawer.ui.user.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "NewAccountActivity";
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.app_content_main);
        newAccountFragment newAccountFragment = new newAccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, newAccountFragment).commit();
    }

    public void newAccount(String userName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        Log.d(TAG,"Account created successfully");
                        user newUser = new user(userName, email);
                        String userId = mAuth.getCurrentUser().getUid();
                        database.child("users").child(userId).setValue(newUser);
                        Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                        intent.putExtra("user", mAuth.getCurrentUser());
                        startActivity(intent);
                    } else {
                        Toast.makeText(NewAccountActivity.this, "Email address exists, you can login directly",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void alreadyHaveAccount(View view){
        startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
    }
}

