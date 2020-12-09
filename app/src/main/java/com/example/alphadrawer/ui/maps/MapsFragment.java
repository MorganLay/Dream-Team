package com.example.alphadrawer.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.alphadrawer.R;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment {

    GoogleMap mMap;
    ArrayList<LatLng> locations = new ArrayList<>();
    ArrayList<String> locationNames = new ArrayList<>();
    private FusedLocationProviderClient client;
    private Location location;
    private double lat, lng = 0;
    private DatabaseReference reff;

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


            mMap = googleMap;

            // Default location if the user doesn't configure their settings        -------- In the future, make an if statement to check if a value in values is configured to the user. Make a file that stores user held data. ------

            double latitude = 45.425446;
            double longitude = -75.692375;


            if (ActivityCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},8000);
            } else {
                System.out.println("Location is granted");
                mMap.setMyLocationEnabled(true);
                Criteria criteria = new Criteria();
                getContext();
                LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, true);
                Location locationH = locationManager.getLastKnownLocation(provider);
                latitude = locationH.getLatitude();
                longitude = locationH.getLongitude();
            }

            reff = FirebaseDatabase.getInstance().getReference().child("Preferences").child("info1");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int limit = Integer.valueOf(snapshot.child("results").getValue().toString());
                    for(int i = 0; i < locations.size();i++){
                        if(i > limit){
                            break;
                        }
                        System.out.println("Added Marker");
                        mMap.addMarker(new MarkerOptions().position(locations.get(i)).title(locationNames.get(i)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            System.out.println("This seems to have worked, wow ");
            LatLng ottawa = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(ottawa).title("Marker in Ottawa"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ottawa));

            // Configure zoom min and max values here
            mMap.setMinZoomPreference(12);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        try {

          //S  assert getArguments() != null;
            int value = getArguments().getInt("max");
            System.out.println("Maps printing");
            System.out.println(value);
            for (int i = 0; i < value; i++) {
                locations.add(getLocationFromAddress(getContext(),(getArguments().getString(Integer.toString(i)))));
                locationNames.add(getArguments().getString(Integer.toString(i)));
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

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}