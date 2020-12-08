package com.example.alphadrawer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alphadrawer.ui.profile.profileFragment;
import com.example.alphadrawer.ui.user.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_content_main);
        profileFragment profileFragment = new profileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFragment).commit();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = auth.getCurrentUser();
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
                                if(currentUser.username != null) {
                                    ((TextView) findViewById(R.id.profile_name)).setText(currentUser.username);
                                }
                                if(currentUser.email != null) {
                                    ((TextView) findViewById(R.id.profile_email)).setText(currentUser.email);
                                }
                                if(currentUser.age != null) {
                                    ((TextView) findViewById(R.id.profile_age)).setText(currentUser.username);
                                }
                                if(currentUser.gender != null) {
                                    ((TextView) findViewById(R.id.profile_gender)).setText(currentUser.username);
                                }
                                if(currentUser.address != null) {
                                    ((TextView) findViewById(R.id.profile_address)).setText(currentUser.username);
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
}
