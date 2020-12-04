package com.example.alphadrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow, R.id.nav_gallery, R.id.nav_preferences, R.id.nav_maps)
                .setOpenableLayout(drawer)
                .build();
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = ((NavHostFragment) navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logOutAction(MenuItem item){
        startActivity(new Intent(MainActivity.this, WelcomePageActivity.class));
    }

    public void guestLogInAction(MenuItem item){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void settingsAction(MenuItem item){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            MenuItem logout = menu.findItem(R.id.action_userLogout);
            MenuItem guestSignIn = menu.findItem(R.id.action_guestSignIn);
            MenuItem settings = menu.findItem(R.id.action_settings);
            if (extras.getBoolean("user") == true) {
                logout.setVisible(true);
                guestSignIn.setVisible(false);
                settings.setVisible(true);

                Intent intent = getIntent();
                String userName = intent.getStringExtra("userName");
                String email = intent.getStringExtra("email");
                if(userName != null) {
                    ((TextView) findViewById(R.id.userName)).setText(userName);
                }
                if(email != null) {
                    ((TextView) findViewById(R.id.emailAddress)).setText(email);
                }

            } else if (extras.getBoolean("user") == false) {
                logout.setVisible(false);
                guestSignIn.setVisible(true);
                settings.setVisible(false);

                ((TextView) findViewById(R.id.userName)).setText("Guest");
                ((TextView) findViewById(R.id.emailAddress)).setText(" ");
            }

        }
        return true;
    }
}