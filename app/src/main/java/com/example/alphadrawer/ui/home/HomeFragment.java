package com.example.alphadrawer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.alphadrawer.ui.user.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        findWeather();
        super.onResume();
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

                JSONObject mainObj = response.getJSONObject("main");         // Main JSON
                JSONArray arr = response.getJSONArray("weather");
                JSONObject obj = arr.getJSONObject(0);
                String temp = String.valueOf(mainObj.getDouble("temp"));     // Gets the temperature
                String desc = obj.getString("description");
                String cit = response.getString("name");

                System.out.println("The weather in " + cit + " is " + temp);

                // Changes the weather in the app
                updateWeather(temp, desc, cit);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> System.out.println("THIS WAS DEFINITELY NOTTTTTTTT ATTMEPTED")
        );

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(j_req);
    }

    public int convertToCelsius(double kelvin){
        return (int) Math.round(kelvin - 273.15);
    }

    public void updateWeather(String t, String d, String c){

        TextView tempView =  this.requireView().findViewById(R.id.weatherTemp);         // Temperature
        TextView descView =  this.requireView().findViewById(R.id.weatherDesc);         // Description of the weather
        ImageView imageView =  this.requireView().findViewById(R.id.weatherSymbol);     // Weather image

        int temp = convertToCelsius(Double.parseDouble(t));
        String proper = convertToTitleCaseIteratingChars(d);

        System.out.println("We are in update weather and::: " + d);

        imageView.setImageResource(getWeatherImage(d, temp));
        tempView.setText(temp + "°C");
        descView.setText(proper);
    }


    private int getWeatherImage(String desc, int temp){
        int imageSource = R.drawable.sun;

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(currentTime);

        boolean daytime = true;

        int hourA = Integer.parseInt(String.valueOf(strDate.charAt(11)));
        int hourB = Integer.parseInt(String.valueOf(strDate.charAt(12)));

        // Setting weather pic
        if((hourA == 0 && hourB < 8) || (hourA == 1 && hourB > 9) || hourA == 2){
            daytime = false;
        } else if (desc.contains("cloud")){
            imageSource = R.drawable.cloud;
        } else if (desc.contains("rain") || desc.contains("drizzle")){
            imageSource = R.drawable.rain;
        } else if (desc.contains("thunder")){
            imageSource = R.drawable.thunder;
        } else if (desc.contains("snow") || desc.contains("sleet")){
            imageSource = R.drawable.snow;
        }

        if(!daytime){
            imageSource = R.drawable.moon;
        }

        return imageSource;
    }

    /*
        https://www.baeldung.com/java-string-title-case
        Used this resource instead of importing a library. All credit goes to Marcos Lopez Gonzalez
        The function is merely used to make characters capitalized for aesthetic purposes and doesn't affect any functionality.
     */
    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

}