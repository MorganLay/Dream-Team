package com.example.alphadrawer.ui.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alphadrawer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreferencesFragment extends Fragment {

    private PreferencesViewModel preferencesViewModel;

    TextView tvProgressLabel;
    TextView TvProgressLabel2;
    DatabaseReference reff;
    DatabaseReference reff2;
    Button saveButton;
    Preferences preferences;
    int rad;
    int results;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        preferencesViewModel =
                ViewModelProviders.of(this).get(PreferencesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);
        final TextView textView = root.findViewById(R.id.text_preferences);
        SeekBar seekBar = root.findViewById(R.id.seekBar);
        SeekBar seekBar2 = root.findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        reff = FirebaseDatabase.getInstance().getReference().child("Preferences").child("info1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rad = Integer.valueOf(snapshot.child("rad").getValue().toString());
                results = Integer.valueOf(snapshot.child("results").getValue().toString());
                seekBar.setProgress(rad);
                seekBar2.setProgress(results);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        int progress = seekBar.getProgress();
        tvProgressLabel = root.findViewById(R.id.textViewslider);
        tvProgressLabel.setText("Max Distance (in km): " + progress);

        seekBar2.setOnSeekBarChangeListener(seekBarChangeListener2);

        int progress2 = seekBar2.getProgress();
        TvProgressLabel2 = root.findViewById(R.id.textViewslider2);
        TvProgressLabel2.setText("Limit Search Results: " + progress2);
        preferencesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        reff2 = FirebaseDatabase.getInstance().getReference().child("Preferences");
        preferences = new Preferences();
        saveButton = root.findViewById(R.id.preferences_submit_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.setRad(seekBar.getProgress());
                preferences.setResults(seekBar2.getProgress());
                reff2.child("info1").child("rad").setValue(preferences.getrad());
                reff2.child("info1").child("results").setValue(preferences.getResults());
            }
        });
        return root;
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            tvProgressLabel.setText("Max Distance (in km): " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar2, int progress2, boolean fromUser) {
            // updated continuously as the user slides the thumb
            TvProgressLabel2.setText("Limit Search Results: " + progress2);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar2) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar2) {
            // called after the user finishes moving the SeekBar
        }
    };
}