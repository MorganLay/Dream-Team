package com.example.alphadrawer.ui.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alphadrawer.R;

public class PreferencesFragment extends Fragment {

    private PreferencesViewModel preferencesViewModel;

    TextView tvProgressLabel;
    TextView TvProgressLabel2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        preferencesViewModel =
                ViewModelProviders.of(this).get(PreferencesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_preferences, container, false);
        final TextView textView = root.findViewById(R.id.text_preferences);
        SeekBar seekBar = root.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        tvProgressLabel = root.findViewById(R.id.textViewslider);
        tvProgressLabel.setText("Max Distance (in km): " + progress);

        SeekBar seekBar2 = root.findViewById(R.id.seekBar2);
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