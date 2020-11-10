package com.example.alphadrawer.ui.gallery;

import android.os.Bundle;
import android.util.Log;
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
import com.example.alphadrawer.ui.maps.MapsFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
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
    public static final String TAG = "YOUR-TAG-NAME";

    ArrayList<String> idArr = new ArrayList<>();
    ArrayList<String> nameArr = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MapsFragment fragment = new MapsFragment ();
        Bundle args = new Bundle();
        args.putString("test", "Vladaisdiadhsaiuhdasuidhasiuhdisauhiu");
        args.putInt("max", 0);
        fragment.setArguments(args);
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        // Initialize the SDK
        Places.initialize(root.getContext(), "AIzaSyASB_iNXNrvejnHEvRwmtXtmNWhn--h-_U");

        // Create a new PlacesClient instance
        final PlacesClient placesClient = Places.createClient(root.getContext());

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
                Place.Field.NAME,Place.Field.ADDRESS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

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
        ClickedButton.setOnClickListener(w -> {
            activityName = activitySpin.getSelectedItem().toString();
            System.out.println("THE BUTTON WAS CLICKED and it says: " + activityName);

       //     String nearby_url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+ "input=golf&"+"location="+50+","+50+"&radius=5000";
       //     String url ="https://maps.googleapis.com/maps/api/place/findplacefromtext/json?"+ "input=park&"+"inputtype=textquery&"+ "location="+50+","+50+"&radius=5000"+"&key=AIzaSyASB_iNXNrvejnHEvRwmtXtmNWhn--h-_U";

            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            // Create a RectangularBounds object.
            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(45.411532,-75.707189),
                    new LatLng(45.431532,-75.627189));
            // Use the builder to create a FindAutocompletePredictionsRequest.

            String query = "Park";


            FindAutocompletePredictionsRequest request1 = FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                    .setLocationBias(bounds)
                    //.setLocationRestriction(bounds)
                    .setOrigin(new LatLng(45.421532,-75.697189))
                    //.setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(query)
                    .build();

            System.out.println(request1);

            placesClient.findAutocompletePredictions(request1).addOnSuccessListener((response) -> {
                System.out.println("This seems to have worked, wow");
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    Log.i(TAG, prediction.getPlaceId());
                    Log.i(TAG, prediction.getPrimaryText(null).toString());
                    idArr.add(prediction.getPlaceId());
                    nameArr.add(prediction.getPrimaryText(null).toString() + " " + prediction.getSecondaryText(null).toString());
                }

                int size = idArr.size();
                args.putInt("max", size);
                for(int i = 0; i < size; i++){
                    args.putString(Integer.toString(i), nameArr.get(i));
                }


                getParentFragmentManager().beginTransaction().add(R.id.mail_container, fragment).commit();
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            });



        });

        return root;
    }


}