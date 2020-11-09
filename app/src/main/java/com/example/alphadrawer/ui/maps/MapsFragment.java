package com.example.alphadrawer.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.alphadrawer.R;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {




        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Default location if the user doesn't configure their settings        -------- In the future, make an if statement to check if a value in values is configured to the user. Make a file that stores user held data. ------
            LatLng ottawa = new LatLng(45.42, -75.69);
            googleMap.addMarker(new MarkerOptions().position(ottawa).title("Marker in Ottawa"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ottawa));

            // Configure zoom min and max values here
            googleMap.setMinZoomPreference(12);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        try {

            int value = getArguments().getInt("max");
            System.out.println("Maps printing");
            System.out.println(value);
            for (int i = 0; i < value; i++) {
                System.out.println(getArguments().getString(Integer.toString(i)));
            }

        } catch (Exception e){
            System.out.println(e);
        }

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}