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

import com.example.alphadrawer.MainActivity;
import com.example.alphadrawer.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    Button ClickedButton;
    String activityName;

    /*  Array that contains the options for the user to select activities. For testing purposes, these are the following:
        Array can be changed based on user profile/preferences. Perhaps sorted by their likelihood to use it.
     */
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
        final Spinner activitySpin = root.findViewById(R.id.spinnerSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, activityArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpin.setAdapter(adapter);

        /*
            Get the value of the array on submit.
            When the submit button is clicked, it gets the current value from the drop down menu (spinner).
            This onClick can also be used to get values from the various filters if they're not-null/unused.

            Used the following resource for additional help:
            https://www.android-examples.com/how-to-create-onclick-event-in-android-on-button-click/
         */
        ClickedButton = root.findViewById(R.id.search_submit_button);
        ClickedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w){
                activityName = activitySpin.getSelectedItem().toString();
                System.out.println("THE BUTTON WAS CLICKED and it says: " + activityName);

            }
        });

        // Initialize the SDK
        Places.initialize(root.getContext(), "AIzaSyASB_iNXNrvejnHEvRwmtXtmNWhn--h-_U");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(root.getContext());

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
                Place.Field.NAME,Place.Field.ADDRESS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();




        return root;
    }

}