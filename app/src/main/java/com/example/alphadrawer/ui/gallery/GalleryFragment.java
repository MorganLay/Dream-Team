package com.example.alphadrawer.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.alphadrawer.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    Button ClickedButton;

    // Array that contains the options for the user to select activities. For testing purposes, these are the following:
    // Array can be changed based on user profile/preferences. Perhaps sorted by their likelihood to use it.
    String[] activityArr = { "Tennis", "Hiking", "Skiing", "Swimming", "Golf" };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        /*  Array that contains the options for the user to select activities. For testing purposes, these are the following:
            Array can be changed based on user profile/preferences. Perhaps sorted by their likelihood to use it.

            activityArr is an array that is used to populate the drop down list.
         */
        Spinner activitySpin = root.findViewById(R.id.spinnerSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, activityArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpin.setAdapter(adapter);

        /*
            Get the value of the array on submit
         */
        ClickedButton = root.findViewById(R.id.search_submit_button);
        ClickedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w){
                System.out.println("THE BUTTON WAS CLICKED");
            }
        });


        return root;
    }
}