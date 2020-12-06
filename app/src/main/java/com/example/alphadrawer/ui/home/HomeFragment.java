package com.example.alphadrawer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alphadrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        findWeather();

        return root;
    }

    /*
        Returns the valid API url based on the desired city and country
     */
    public String getWeatherUrl(String city, String country){

        // Check for the condition that they have given permission to use their lat/long. If they have, then use that template: api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key} instead.
        // api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid={API key}

        // Returns based on their default city
        return getString(R.string.weatherAPIurl) + city + ',' + country + "&APPID=" + getString(R.string.weatherAPIkey);
    }

    /*
        Sends a request to Open Weather API and returns a JSON containing information about the weather based on the lat/lon or default location of the user profile
     */
    public void findWeather(){
        // Make default for the user
        String url = getWeatherUrl("Ottawa", "Canada");
        System.out.println(url);

        JsonObjectRequest j_req = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                // On a successful response from the API, handle the JSON response

                System.out.println("THIS WAS DEFINITELY ATTMEPTED");

                JSONObject mainObj = response.getJSONObject("main");         // Main JSON
                JSONArray arr = response.getJSONArray("weather");
                JSONObject obj = arr.getJSONObject(0);
                String temp = String.valueOf(mainObj.getDouble("temp"));     // Gets the temperature
                String description = obj.getString("description");
                String city = response.getString("name");

                System.out.println("The weather in " + city + " is " + temp);

            } catch (JSONException e) {
                System.out.println("FAILEDDDDDDDD");
                e.printStackTrace();
            }
        }, error -> System.out.println("THIS WAS DEFINITELY NOTTTTTTTT ATTMEPTED")
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(j_req);
    }

}