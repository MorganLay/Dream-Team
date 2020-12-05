package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.login.LoginFragment;
import com.example.alphadrawer.ui.newAccount.newAccountFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.content_main);
        newAccountFragment newAccountFragment = new newAccountFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, newAccountFragment).commit();
    }

    public void newAccount(String userName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(NewAccountActivity.this, "Account created successfully",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(NewAccountActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(NewAccountActivity.this, "Email address exists, you can login directly",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void alreadyHaveAccount(View view){
        startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
    }
}

