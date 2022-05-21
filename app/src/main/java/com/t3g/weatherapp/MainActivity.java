package com.t3g.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter3;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter4;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter5;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter6;
import com.t3g.weatherapp.Modals.WeatherRVModal;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter;
import com.t3g.weatherapp.Adapters.WeatherRVAdapter2;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ScrollView SVMain;
    private ConstraintLayout splashCL;
    private TextView
            dayOfWeekTV,
            cityNameTV,
            temperatureTV,
            conditionTV,
            feelsLikeTV,
            todayTV,
            todayTV3,todayTV4,todayTV5,todayTV6,
            humidityTV,
            pressureTV,
            windTV,
            dateTV;
    private RecyclerView
            weatherRV,
            weatherRV2,
            weatherRV3,
            weatherRV4,
            weatherRV5,
            weatherRV6;
    private TextInputEditText cityEdt;
    private ImageView
            iconIV,
            representationIV;
    private ArrayList<WeatherRVModal>
            weatherRVModalArrayList,
            weatherRVModalArrayList2,
            weatherRVModalArrayList3,
            weatherRVModalArrayList4,
            weatherRVModalArrayList5,
            weatherRVModalArrayList6;
    private WeatherRVAdapter weatherRVAdapter;
    private WeatherRVAdapter2 weatherRVAdapter2;
    private WeatherRVAdapter3 weatherRVAdapter3;
    private WeatherRVAdapter4 weatherRVAdapter4;
    private WeatherRVAdapter5 weatherRVAdapter5;
    private WeatherRVAdapter6 weatherRVAdapter6;
    private final int PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);

        resourceLinking();
        adaptersInitLink();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                    },PERMISSION_CODE);
        }

//        Getting precise gps coordinates from device
        Location location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String lat = Double.toString(location.getLatitude());
        String lon = Double.toString(location.getLongitude());
        String urlForecastFromLatLon = "https://api.openweathermap.org/data/2.5/forecast?" +
                "lat=" + lat + "&lon=" + lon +
                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=40" +
                "&units=metric";
        getWeather(urlForecastFromLatLon);

//        Search by City
        cityEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String city = Objects.requireNonNull(cityEdt.getText()).toString();
                    if(city.isEmpty()){
                        Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                    }else{
                        cityNameTV.setText(city);
                        String urlForecastByCityName = "https://api.openweathermap.org/data/2.5/forecast?q="
                                + city +
                                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=2" +
                                "&units=metric";
                        getWeather(urlForecastByCityName);
                        splashCL.setVisibility(View.VISIBLE);
                        SVMain.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });


    }




    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please allow permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

//    Retrieve & Put Weather Data
    private void getWeather(String apiUrl){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                String currentTime1 = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
                splashCL.setVisibility(View.GONE);
                SVMain.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();
                weatherRVModalArrayList2.clear();
                weatherRVModalArrayList3.clear();
                weatherRVModalArrayList4.clear();
                weatherRVModalArrayList5.clear();
                weatherRVModalArrayList6.clear();
                try {
                    JSONArray list = response.getJSONArray("list");
                        JSONObject cityJsonObject = response.getJSONObject("city");
                        String city = cityJsonObject.getString("name");
                    cityNameTV.setText(city);
                        JSONObject main = list.getJSONObject(1).getJSONObject("main");
                        JSONObject winObject1 = list.getJSONObject(1).getJSONObject("wind");
                        String pressure = main.getString("pressure");
                    pressureTV.setText(pressure+" hPa");
                        String humidity = main.getString("humidity");
                    humidityTV.setText(humidity+"%");

                    JSONObject time1 = list.getJSONObject(1);
                        String dateTime1 = time1.getString("dt_txt");
                        String temp = main.getString("temp");
                    temperatureTV.setText(temp+"°C");
                        String feels_like = main.getString("feels_like");
                    feelsLikeTV.setText("Feels like : "+feels_like+"°C");
                    String dayOfWeek = "Today: "+
                            (LocalDate.now().getDayOfWeek().name().toLowerCase())
                            .substring(0,1).toUpperCase() +
                            (LocalDate.now().getDayOfWeek().name().toLowerCase())
                            .substring(1);
                    dayOfWeekTV.setText(dayOfWeek);
                        String windS = winObject1.getString("speed");
                        String windD = winObject1.getString("deg");
                        float windDegVal = Float.parseFloat(windD);
                        String windDirection;


                    if (windDegVal >337.5) {
                        windDirection = "N";
                    }
                    if (windDegVal>292.5){
                        windDirection = "NW";
                    }
                    if(windDegVal>247.5){
                        windDirection = "W";
                    }
                    else if(windDegVal>202.5){
                        windDirection = "SW";
                    }
                    else if(windDegVal>157.5){
                        windDirection = "S";
                    }
                    else if(windDegVal>122.5){
                        windDirection = "SE";
                    }
                    else if(windDegVal>67.5){
                        windDirection = "E";
                    }
                    else if(windDegVal>22.5){
                        windDirection = "NE";
                    }
                    else {
                            windDirection = "N";
                    }
                        windTV.setText(windS+"m/s "+windDirection);
                        dateTV.setText(currentDate + " : "+currentTime1);

                        JSONObject weather = list
                                .getJSONObject(1).getJSONArray("weather").getJSONObject(0);
                        String condition = weather.getString("main");
                        String description = weather.getString("description");
//                        String id = weather.getString("id");
                            conditionTV.setText(" "+condition+" : "+description+" ");
//              Call weatherCondition function with parameters condition & description
                    weatherCondition(condition,description);
                        String icon = weather.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/"+icon+"@4x.png";
                            Picasso
                                    .get()
                                    .load(iconUrl)
                                    .into(iconIV);

//              Feed data from api & fill UI
                    for (int i = 1; i < list.length(); i++) {
                        JSONObject time = list.getJSONObject(i);
                        String dateTime = time.getString("dt_txt");
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        SimpleDateFormat outputDate = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat outputDate1 = new SimpleDateFormat("dd");
                        SimpleDateFormat outputTime = new SimpleDateFormat("HH");
                        try {
                            Date t = input.parse(dateTime);
                            assert t != null;
                            int cd1 = Integer.parseInt(currentDay);
                            int cd2 = Integer.parseInt(currentDay) + 1;
                            int cd3 = Integer.parseInt(currentDay) + 2;
                            int cd4 = Integer.parseInt(currentDay) + 3;
                            int cd5 = Integer.parseInt(currentDay) + 4;
                            int cd6 = Integer.parseInt(currentDay) + 5;
                            int outD = Integer.parseInt(outputDate1.format(t));

                            if(cd1==outD) {
                                int jT = Integer.parseInt(outputTime.format(t));
                                int cT = Integer.parseInt(currentTime);

//                            Later Today
                                if (jT > cT) {
//                                    Toast.makeText(MainActivity.this, jT+" - "+cT, Toast.LENGTH_SHORT).show();
                                    todayTV.setVisibility(View.VISIBLE);
                                    JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                    String temp1 = main1.getString("temp");
                                    JSONObject weather1 = list.getJSONObject(i)
                                            .getJSONArray("weather").getJSONObject(0);
                                    String iconCode = weather1.getString("icon");
                                    String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                    JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
//                                    String wind = windObj.getString("speed");
                                    weatherRVModalArrayList.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                                }
                            }

//                            Tomorrow
                            if(cd2==outD) { // convert to dd and compare
                                    JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                    String temp1 = main1.getString("temp");
                                    JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                    String iconCode = weather1.getString("icon");
                                    String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                    JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                    weatherRVModalArrayList2.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                            }
//                            Day after Tomorrow
                            if(cd3==outD) { // convert to dd and compare
                                todayTV3.setVisibility(View.VISIBLE);
                                todayTV3.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                weatherRVModalArrayList3.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                            }

//                            Day after Tomorrow + 1
                            if(cd4==outD) { // convert to dd and compare
                                todayTV4.setVisibility(View.VISIBLE);
                                todayTV4.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather")
                                        .getJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList4.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                            }

//                            Day after Tomorrow + 2
                            if(cd5==outD) { // convert to dd and compare
                                todayTV5.setVisibility(View.VISIBLE);
                                todayTV5.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList5.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                            }

//                            Day after Tomorrow + 3
                            if(cd6==outD) { // convert to dd and compare
                                todayTV6.setVisibility(View.VISIBLE);
                                todayTV6.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList6.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
                            }

                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                    }

                    weatherRVAdapter.notifyDataSetChanged();
                    weatherRVAdapter2.notifyDataSetChanged();
                    weatherRVAdapter3.notifyDataSetChanged();
                    weatherRVAdapter4.notifyDataSetChanged();
                    weatherRVAdapter5.notifyDataSetChanged();
                    weatherRVAdapter6.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void weatherCondition(String condition, String description){
        switch (condition) {
            case "Thunderstorm":
                representationIV
                        .setImageResource(R.drawable.thunderstorm);
                break;
            case "Drizzle":
                representationIV
                        .setImageResource(R.drawable.rain);
                break;
            case "Rain":
                representationIV
                        .setImageResource(R.drawable.rain);
                break;
            case "Snow":
                representationIV
                        .setImageResource(R.drawable.snow);
                break;
            case "Mist":
                break;
            case "Smoke":
                break;
            case "Haze":
                break;
            case "Dust":
                break;
            case "Fog":
                break;
            case "Sand":
                break;
            case "Ash":
                break;
            case "Squall":
                break;
            case "Tornado":
                break;
            case "Clear":
                representationIV
                        .setImageResource(R.drawable.sunny_day);
                break;
            case "Clouds":
                representationIV
                        .setImageResource(R.drawable.cloud);
                break;
            default:
                representationIV
                        .setImageResource(R.drawable.logo);
                break;
        }
    }
    private void resourceLinking() {
        SVMain = findViewById(R.id.SVMain);
        splashCL = findViewById(R.id.splashCL);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        feelsLikeTV = findViewById(R.id.idTVFeels);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRVWeather);
        weatherRV2 = findViewById(R.id.idRVWeather2);
        weatherRV3 = findViewById(R.id.idRVWeather3);
        weatherRV4 = findViewById(R.id.idRVWeather4);
        weatherRV5 = findViewById(R.id.idRVWeather5);
        weatherRV6 = findViewById(R.id.idRVWeather6);
        cityEdt = findViewById(R.id.idEdtCity);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);

        iconIV = findViewById(R.id.idIVIcon);
        todayTV = findViewById(R.id.todayTV);
        todayTV3 = findViewById(R.id.todayTV3);
        todayTV4 = findViewById(R.id.todayTV4);
        todayTV5 = findViewById(R.id.todayTV5);
        todayTV6 = findViewById(R.id.todayTV6);
        dateTV = findViewById(R.id.dateTV);

        humidityTV = findViewById(R.id.humidityTV);
        representationIV = findViewById(R.id.representationIV);
        pressureTV = findViewById(R.id.pressureTV);
        windTV = findViewById(R.id.windTV);
    }
    private void adaptersInitLink() {
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVModalArrayList2 = new ArrayList<>();
        weatherRVModalArrayList3 = new ArrayList<>();
        weatherRVModalArrayList4 = new ArrayList<>();
        weatherRVModalArrayList5 = new ArrayList<>();
        weatherRVModalArrayList6 = new ArrayList<>();

        weatherRVAdapter = new WeatherRVAdapter(this,weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);
        weatherRVAdapter2 = new WeatherRVAdapter2(this,weatherRVModalArrayList2);
        weatherRV2.setAdapter(weatherRVAdapter2);
        weatherRVAdapter3 = new WeatherRVAdapter3(this,weatherRVModalArrayList3);
        weatherRV3.setAdapter(weatherRVAdapter3);
        weatherRVAdapter4 = new WeatherRVAdapter4(this,weatherRVModalArrayList4);
        weatherRV4.setAdapter(weatherRVAdapter4);
        weatherRVAdapter5 = new WeatherRVAdapter5(this,weatherRVModalArrayList5);
        weatherRV5.setAdapter(weatherRVAdapter5);
        weatherRVAdapter6 = new WeatherRVAdapter6(this,weatherRVModalArrayList6);
        weatherRV6.setAdapter(weatherRVAdapter6);
    }
}