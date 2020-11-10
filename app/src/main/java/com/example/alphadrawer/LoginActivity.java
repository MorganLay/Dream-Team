package com.example.alphadrawer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, loginFragment).commit();
    }
}

