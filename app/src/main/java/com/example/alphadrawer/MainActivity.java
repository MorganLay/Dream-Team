package com.example.alphadrawer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.alphadrawer.ui.user.user;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FusedLocationProviderClient client;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private user currentUser;
    private Location location;
    private double lat, lng = 0;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        FirebaseApp.initializeApp(MainActivity.this);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_gallery, R.id.nav_preferences, R.id.nav_maps)
                .setOpenableLayout(drawer)
                .build();
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = ((NavHostFragment) navHostFragment).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
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
        FirebaseAuth.getInstance().signOut();
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
        MenuItem logout = menu.findItem(R.id.action_userLogout);
        MenuItem guestSignIn = menu.findItem(R.id.action_guestSignIn);
        MenuItem settings = menu.findItem(R.id.action_settings);

        Bundle data = getIntent().getExtras();
        FirebaseUser user = (FirebaseUser) data.get("user");
        if (user != null && !user.getEmail().isEmpty()) {
            logout.setVisible(true);
            guestSignIn.setVisible(false);
            settings.setVisible(true);

            String userId = user.getUid();

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> item = dataSnapshot.getChildren();
                    for(DataSnapshot data : item) {
                        if (data.getKey().equals("users")) {
                            Iterable<DataSnapshot> users = data.getChildren();
                            for (DataSnapshot user : users) {
                                if (user.getKey().equals(userId)) {
                                    user currentUser = user.getValue(user.class);
                                    ((TextView) findViewById(R.id.userName)).setText(currentUser.username);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Ignore
                }
            };
            database.addValueEventListener(postListener);

        } else {
            logout.setVisible(false);
            guestSignIn.setVisible(true);
            settings.setVisible(false);

            ((TextView) findViewById(R.id.userName)).setText("Guest");
        }
        return true;
    }

    public void retrieveProfile(){
        String userId = firebaseUser.getUid();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> item = dataSnapshot.getChildren();
                for(DataSnapshot data : item) {
                    if (data.getKey().equals("users")) {
                        Iterable<DataSnapshot> users = data.getChildren();
                        for (DataSnapshot user : users) {
                            if (user.getKey().equals(userId)) {
                                currentUser = user.getValue(user.class);
                                if(currentUser.username != null) {
                                    ((TextView) findViewById(R.id.profile_name)).setText(currentUser.username);
                                }
                                if(currentUser.email != null) {
                                    ((TextView) findViewById(R.id.profile_email)).setText(currentUser.email);
                                }
                                if(currentUser.age != null) {
                                    ((TextView) findViewById(R.id.profile_age)).setText(currentUser.age);
                                }
                                if(currentUser.gender != null) {
                                    ((TextView) findViewById(R.id.profile_gender)).setText(currentUser.gender);
                                }
                                if(currentUser.address != null) {
                                    ((TextView) findViewById(R.id.profile_address)).setText(currentUser.address);
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Ignore
            }
        };
        database.addValueEventListener(postListener);
    }

    public void UpdateProfile(user userNewInfo) {
        String userId = firebaseUser.getUid();
        if(currentUser.username == null ||userNewInfo.username!= null && !currentUser.username.equals(userNewInfo.username)){
            database.child("users").child(userId).child("username").setValue(userNewInfo.username);
        }

        if(currentUser.email == null || userNewInfo.email != null && !currentUser.email.equals(userNewInfo.email)){
            database.child("users").child(userId).child("email").setValue(userNewInfo.email);
        }

        if(currentUser.age == null ||userNewInfo.age != null && !currentUser.age.equals(userNewInfo.age)){
            database.child("users").child(userId).child("age").setValue(userNewInfo.age);
        }

        if(currentUser.gender == null ||userNewInfo.gender != null && !currentUser.gender.equals(userNewInfo.gender)){
            database.child("users").child(userId).child("gender").setValue(userNewInfo.gender);
        }

        if(currentUser.address == null ||userNewInfo.address != null && !currentUser.address.equals(userNewInfo.address)){
            database.child("users").child(userId).child("address").setValue(userNewInfo.address);
        }
    }
}