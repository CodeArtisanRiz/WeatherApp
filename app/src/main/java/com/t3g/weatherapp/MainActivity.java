package com.t3g.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView
            cityNameTV,
            temperatureTV,
            conditionTV,
            feelsLikeTV,
            todayTV,
            todayTV3,todayTV4,todayTV5,todayTV6,
            precipitationTV,
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
            backIV,
            iconIV,
            searchIV,
            precipitationIV,
            pressureIV,
            windIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList2;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList3;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList4;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList5;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList6;
    private WeatherRVAdapter weatherRVAdapter;
    private WeatherRVAdapter2 weatherRVAdapter2;
    private WeatherRVAdapter3 weatherRVAdapter3;
    private WeatherRVAdapter4 weatherRVAdapter4;
    private WeatherRVAdapter5 weatherRVAdapter5;
    private WeatherRVAdapter6 weatherRVAdapter6;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_home);

        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
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
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        todayTV = findViewById(R.id.todayTV);
//        tomTV = findViewById(R.id.tomTV);
        todayTV3 = findViewById(R.id.todayTV3);
        todayTV4 = findViewById(R.id.todayTV4);
        todayTV5 = findViewById(R.id.todayTV5);
        todayTV6 = findViewById(R.id.todayTV6);
        searchIV = findViewById(R.id.idTVSearch);
        dateTV = findViewById(R.id.dateTV);

        precipitationTV = findViewById(R.id.precipitationTV);
        precipitationIV = findViewById(R.id.precipitationIV);
        pressureIV = findViewById(R.id.pressureIV);
        pressureTV = findViewById(R.id.pressureTV);
        windTV = findViewById(R.id.windTV);
        windIV = findViewById(R.id.windIV);

        weatherRVModalArrayList = new ArrayList<>();
        weatherRVModalArrayList2 = new ArrayList<>();
        weatherRVModalArrayList3 = new ArrayList<>();
        weatherRVModalArrayList4 = new ArrayList<>();
        weatherRVModalArrayList5 = new ArrayList<>();
        weatherRVModalArrayList6 = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this,weatherRVModalArrayList);
        weatherRVAdapter2 = new WeatherRVAdapter2(this,weatherRVModalArrayList2);
        weatherRVAdapter3 = new WeatherRVAdapter3(this,weatherRVModalArrayList3);
        weatherRVAdapter4 = new WeatherRVAdapter4(this,weatherRVModalArrayList4);
        weatherRVAdapter5 = new WeatherRVAdapter5(this,weatherRVModalArrayList5);
        weatherRVAdapter6 = new WeatherRVAdapter6(this,weatherRVModalArrayList6);
        weatherRV.setAdapter(weatherRVAdapter);
        weatherRV2.setAdapter(weatherRVAdapter2);
        weatherRV3.setAdapter(weatherRVAdapter3);
        weatherRV4.setAdapter(weatherRVAdapter4);
        weatherRV5.setAdapter(weatherRVAdapter5);
        weatherRV6.setAdapter(weatherRVAdapter6);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);

        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        cityName = getCityName(location.getLongitude(),location.getLatitude());
//        cityName = "Silchar";
        String lat = Double.toString(location.getLatitude());
        String lon = Double.toString(location.getLongitude());
        getWeatherInfoLatLon(cityName, lat, lon);

//        getWeatherInfo(cityName);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = Objects.requireNonNull(cityEdt.getText()).toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                }else{
                    cityNameTV.setText(city);
                    getWeatherInfoCity(city, location.getLatitude(), location.getLongitude());
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

//    private String getCityName(double longitude, double latitude){
//        String cityName = "Not Found";
//
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        try {
//           List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
//
//           for(Address adr : addresses){
//               String city = adr.getLocality();
//               if(city!=null && !city.equals("")){
//                   cityName =city;
//               }else{
//                   Log.d("TAG", "City Not Found");
//                   Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
//               }
//           }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return cityName;
//    }
    private void getWeatherInfoLatLon(String cityName, String lat, String lon){
        String urlForecastFromLatLon = "https://api.openweathermap.org/data/2.5/forecast?" +
                "lat=" + lat + "&lon=" + lon +
                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=40" +
                "&units=metric";
        String urlFromCityName = "https://api.openweathermap.org/data/2.5/forecast?q=" +
                cityName +
                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=2" +
                "&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, urlForecastFromLatLon, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
//                Toast.makeText(MainActivity.this, currentTime, Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();
                weatherRVModalArrayList2.clear();
                weatherRVModalArrayList3.clear();
                weatherRVModalArrayList4.clear();
                weatherRVModalArrayList5.clear();
                weatherRVModalArrayList6.clear();
                try {
                    JSONArray list = response.getJSONArray("list");
//                    for (int i = 0; i < list.length();){
                        JSONObject cityJsonObject = response.getJSONObject("city");
                        String city = cityJsonObject.getString("name");
                        cityNameTV.setText(city);
                        JSONObject main = response.getJSONArray("list").getJSONObject(1).getJSONObject("main");
                        JSONObject winObject1 = response.getJSONArray("list").getJSONObject(1).getJSONObject("wind");

                    JSONObject time1 = list.getJSONObject(1);
                    String dateTime1 = time1.getString("dt_txt");
                    String temp = main.getString("temp");
                    String windS = winObject1.getString("speed");
                        temperatureTV.setText(temp);
                    String feels_like = main.getString("feels_like");
                        feelsLikeTV.setText("Feels like : "+feels_like);
                        windTV.setText(windS+" m/s");


                        dateTV.setText(currentDate);
//                        String temp_min = main.getString("temp_min");
//                        String temp_max = main.getString("temp_max");
//                        String pressure = main.getString("pressure");
//                        String humidity = main.getString("humidity");
                    Toast.makeText(MainActivity.this, lat + " : " +lon, Toast.LENGTH_LONG).show();

                        JSONObject weather = response.getJSONArray("list")
                                .getJSONObject(0).getJSONArray("weather").getJSONObject(0);
                        String condition = weather.getString("main");
                        String description = weather.getString("description");
                        conditionTV.setText(condition+" : "+description);
                        String icon = weather.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/"+icon+".png";
                        Picasso.get().load(iconUrl).into(iconIV);
//                        JSONObject windObject = weather.getJSONObject("wind");
//                        String windSpeed = windObject.getString("speed");
//                        windTV.setText(windSpeed);
//                    Toast.makeText(MainActivity.this, windSpeed, Toast.LENGTH_SHORT).show();
//                    }


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
//                            Toast.makeText(MainActivity.this, output.format(t), Toast.LENGTH_SHORT).show();
                            int cd2 = Integer.parseInt(currentDay) + 1;
                            int cd3 = Integer.parseInt(currentDay) + 2;
                            int cd4 = Integer.parseInt(currentDay) + 3;
                            int cd5 = Integer.parseInt(currentDay) + 4;
                            int cd6 = Integer.parseInt(currentDay) + 5;
                            int outD = Integer.parseInt(outputDate1.format(t));

//                            Toast.makeText(MainActivity.this, cd1 + " : "+ outD1, Toast.LENGTH_SHORT).show();
//                            Later Today
                            if(currentDate.equals(outputDate.format(t))) {
                                int jT = Integer.parseInt(outputTime.format(t));
                                int cT = Integer.parseInt(currentTime);


                                if (jT > cT) {
                                    todayTV.setVisibility(View.VISIBLE);
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                    JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                    String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                    JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                    String iconCode = weather1.getString("icon");
                                    String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                    JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                    String wind = windObj.getString("speed");
                                    weatherRVModalArrayList.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
                                }
                            }

//                            Tomorrow
                            if(cd2==outD) { // convert to dd and compare
//                                int jT = Integer.parseInt(outputTime.format(t));
//                                int cT = Integer.parseInt(currentTime);
//                                tomTV.setVisibility(View.VISIBLE);
//                                tomTV.setText(outputDate.format(t));

//                                Toast.makeText(MainActivity.this, cd1 +" tom "+outD1, Toast.LENGTH_SHORT).show();
//                                if (jT >= cT) {
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                    JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                    String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                    JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                    String iconCode = weather1.getString("icon");
                                    String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                    JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                    String wind = windObj.getString("speed");
                                Toast.makeText(MainActivity.this, wind, Toast.LENGTH_SHORT).show();
                                    weatherRVModalArrayList2.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
//                                }
                            }
//                            Day after Tomorrow
                            if(cd3==outD) { // convert to dd and compare
//                                int jT = Integer.parseInt(outputTime.format(t));
//                                int cT = Integer.parseInt(currentTime);

//                                Toast.makeText(MainActivity.this, cd1 +" tom "+outD1, Toast.LENGTH_SHORT).show();
//                                if (jT >= cT) {
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                todayTV3.setVisibility(View.VISIBLE);
//                                String dz = outputDate
                                todayTV3.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList3.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
//                                }
                            }

//                            Day after Tomorrow + 1
                            if(cd4==outD) { // convert to dd and compare
//                                int jT = Integer.parseInt(outputTime.format(t));
//                                int cT = Integer.parseInt(currentTime);

//                                Toast.makeText(MainActivity.this, cd1 +" tom "+outD1, Toast.LENGTH_SHORT).show();
//                                if (jT >= cT) {
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                todayTV4.setVisibility(View.VISIBLE);
//                                String dz = outputDate
                                todayTV4.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList4.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
//                                }
                            }

//                            Day after Tomorrow + 2
                            if(cd5==outD) { // convert to dd and compare
//                                int jT = Integer.parseInt(outputTime.format(t));
//                                int cT = Integer.parseInt(currentTime);

//                                Toast.makeText(MainActivity.this, cd1 +" tom "+outD1, Toast.LENGTH_SHORT).show();
//                                if (jT >= cT) {
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                todayTV5.setVisibility(View.VISIBLE);
//                                String dz = outputDate
                                todayTV5.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList5.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
//                                }
                            }

//                            Day after Tomorrow + 3
                            if(cd6==outD) { // convert to dd and compare
//                                int jT = Integer.parseInt(outputTime.format(t));
//                                int cT = Integer.parseInt(currentTime);

//                                Toast.makeText(MainActivity.this, cd1 +" tom "+outD1, Toast.LENGTH_SHORT).show();
//                                if (jT >= cT) {
//                                    Toast.makeText(MainActivity.this, cT + " : " + jT, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, output.format(t) + i, Toast.LENGTH_SHORT).show();
                                todayTV6.setVisibility(View.VISIBLE);
//                                String dz = outputDate
                                todayTV6.setText(outputDate.format(t));
                                JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                                String temp1 = main1.getString("temp");
//                                    String feels_like1 = main.getString("feels_like");
//                                    String temp_min1 = main.getString("temp_min");
//                                    String temp_max1 = main.getString("temp_max");
//                                    String pressure1 = main.getString("pressure");
//                                    String humidity1 = main.getString("humidity");
                                JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                                String iconCode = weather1.getString("icon");
                                String iconUrlLoop = "https://openweathermap.org/img/wn/" + iconCode + ".png";
                                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                                String wind = windObj.getString("speed");
                                weatherRVModalArrayList6.add(new WeatherRVModal(dateTime, temp1, iconUrlLoop));
//                                            , wind));
//                                }
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
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void getWeatherInfoCity(String cityName, double longitude, double latitude){
//        cityNameTV.setText(cityName);


//        String url ="http://api.weatherapi.com/v1/forecast.json?key=1d29e97bb19046909ac195720222504&q=silchar&days=1&aqi=no&alerts=no";
//        String url  = "https://api.openweathermap.org/data/2.5/weather?q="
//                + cityName +
//                ",&units=metric&appid=caf89aee48527f1ef55a54cba7d2e51e";

        String urlForecastFromLatLon = "https://api.openweathermap.org/data/2.5/forecast?" +
                "lat=24.8167" +
                "&lon=92.8" +
                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=40&units=metric";
        String urlFromCityName = "https://api.openweathermap.org/data/2.5/forecast?q=" +
                cityName +
                "&appid=caf89aee48527f1ef55a54cba7d2e51e&cnt=40&units=metric";


//        String urlFromLatLon = "https://api.openweathermap.org/data/2.5/weather?lat="
//                + latitude +
//                + longitude +"&appid=caf89aee48527f1ef55a54cba7d2e51e";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        Picasso.get().load("https://images.unsplash.com/photo-1507502707541-f369a3b18502?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=388&q=80").into(backIV);
//

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, urlFromCityName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();

                try {
                    JSONObject cityJsonObject = response.getJSONObject("city");
                    String city = cityJsonObject.getString("name");
                    cityNameTV.setText(city);

                    JSONObject coordJsonObject = cityJsonObject.getJSONObject("coord");
                    String lat = coordJsonObject.getString("lat");
                    String lon = coordJsonObject.getString("lon");
                    String country = cityJsonObject.getString("country");
                    String population = cityJsonObject.getString("population");

                    JSONObject main = response.getJSONArray("list").getJSONObject(0).getJSONObject("main");
                    String temp = main.getString("temp");
                    temperatureTV.setText(temp);
                    String feels_like = main.getString("feels_like");
                    feelsLikeTV.setText("Feels like : "+feels_like);
                    String temp_min = main.getString("temp_min");
                    String temp_max = main.getString("temp_max");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");



                    JSONArray list = response.getJSONArray("list");
                    for (int i = 1; i < list.length(); i++) {
//                        JSONObject token = list.getJSONObject(i);
//                        Toast.makeText(MainActivity.this, "helo"+i, Toast.LENGTH_SHORT).show();
                        JSONObject main1 = list.getJSONObject(i).getJSONObject("main");
                        String temp1 = main1.getString("temp");
                        JSONObject time = list.getJSONObject(i);
                        String date = time.getString("dt_txt");
//                        Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
                        String feels_like1 = main.getString("feels_like");
                        String temp_min1 = main.getString("temp_min");
                        String temp_max1 = main.getString("temp_max");
                        String pressure1 = main.getString("pressure");
                        String humidity1 = main.getString("humidity");

                        JSONObject weather1 = list.getJSONObject(i).getJSONArray("weather").optJSONObject(0);
                        String iconCode = weather1.getString("icon");
                        String iconUrlLoop = "https://openweathermap.org/img/wn/"+iconCode+".png";
//                        Toast.makeText(MainActivity.this, iconCode, Toast.LENGTH_SHORT).show();
//                        String all = feels_like1+" "+temp_min1+" "+pressure1+" "+humidity1;
//                        temperatureTV.setText(all);
//                        Toast.makeText(MainActivity.this, all, Toast.LENGTH_SHORT).show();
//                        String img = hourObj.getJSONObject("condition").getString("icon");
////                        String wind = hourObj.getString("wind_kph");
////
                        weatherRVModalArrayList.add(new WeatherRVModal(date,temp1,iconUrlLoop));
//                                            , wind));
//                        time,temper,img,wind
                    }

                    JSONObject weather = response.getJSONArray("list")
                            .getJSONObject(0).getJSONArray("weather").getJSONObject(0);
                    String condition = weather.getString("main");
                    String description = weather.getString("description");
                    conditionTV.setText(condition+" : "+description);
                    String icon = weather.getString("icon");
                    String iconUrl = "https://openweathermap.org/img/wn/"+icon+".png";
//                        Toast.makeText(MainActivity.this, icon, Toast.LENGTH_SHORT).show();
                    Picasso.get().load(iconUrl).into(iconIV);
//





//                    JSONArray weather = response.getJSONArray("list");
//                    for (int i = 0; i < weather.length(); i++) {
//                        JSONObject token = weather.getJSONObject(i);
//
//                        JSONObject main = cityJsonObject.getJSONObject("main");
//                        String temp = "nehi chala";
//                        temp = main.getString("temp");
//                    String array = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0)
//                            .getString("description");
//                    Toast.makeText(MainActivity.this, array, Toast.LENGTH_SHORT).show();

//                    if (array != null) {
//

//                    }
//                    else {
//                        temperatureTV.setText("Nehi chala bro");
//                    }


//                        String main = token.getString("main");
//                        String desc = token.getString("description");
//                        String icon = token.getString("icon");
//                        String iconUrl = "https://openweathermap.org/img/wn/"+icon+".png";
////                        Toast.makeText(MainActivity.this, icon, Toast.LENGTH_SHORT).show();
//                        Picasso.get().load(iconUrl).into(iconIV);
//                        conditionTV.setText(main+" : "+desc);
//                        Toast.makeText(MainActivity.this, desc, Toast.LENGTH_SHORT).show();
//                    }





//                        temperatureTV.setText(country);







//                    JSONObject coordJsonObject = response.getJSONObject("coord");
//                    String longitude = coordJsonObject.getString("lon");
//                    String latitude = coordJsonObject.getString("lat");
//
//                    JSONArray weather = response.getJSONArray("weather");
//                    for (int i = 0; i < weather.length(); i++) {
//                        JSONObject token = weather.getJSONObject(i);
//
//                        String main = token.getString("main");
//                        String desc = token.getString("description");
//                        String icon = token.getString("icon");
//                        String iconUrl = "https://openweathermap.org/img/wn/"+icon+".png";
////                        Toast.makeText(MainActivity.this, icon, Toast.LENGTH_SHORT).show();
//                        Picasso.get().load(iconUrl).into(iconIV);
//                        conditionTV.setText(main+" : "+desc);
////                        Toast.makeText(MainActivity.this, desc, Toast.LENGTH_SHORT).show();
//                    }

//                    JSONObject mainJsonObject = response.getJSONObject("main");
//                    String temp = mainJsonObject.getString("temp");
////                    Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
//                    temperatureTV.setText(temp + "°c");
//                    String feels_like = mainJsonObject.getString("feels_like");
//                    String temp_min = mainJsonObject.getString("temp_min");
//                    String temp_max = mainJsonObject.getString("temp_max");
//                    String pressure = mainJsonObject.getString("pressure");
//                    String humidity = mainJsonObject.getString("humidity");
//
//                    JSONObject windJsonObject = response.getJSONObject("wind");
//                    String speed = mainJsonObject.getString("speed");

//***************************************************************************************************************



//                        Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
//                try {
//                    String temperature = response.getJSONObject("current").getString("temp_c");
//                    temperatureTV.setText(temperature+"°c");
//                    int isDay = response.getJSONObject("current").getInt("is_day");
//                    String condition = response.getJSONObject("current")
//                            .getJSONObject("condition").getString("text");
//                    String conditionIcon = response.getJSONObject("current")
//                            .getJSONObject("condition").getString("icon");
//                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
//                    conditionTV.setText(condition);
//                    if(isDay==1){
//                        Picasso.get().load("https://images.unsplash.com/photo-1600528771981-74677b0a0824?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80").into(backIV);
//                    }else{
////                        night
//                        Picasso.get().load("https://images.unsplash.com/photo-1507502707541-f369a3b18502?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=388&q=80").into(backIV);
//                    }

//                    JSONObject forecastObj = response.getJSONObject("forecast");
//                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
//                    JSONArray hourArray = forecast0.getJSONArray("hour");

//                    for (int i=0; i<hourArray.length(); i++){
//                        JSONObject hourObj = hourArray.getJSONObject(i);
//                        String time = hourObj.getString("time");
//                        String temper = hourObj.getString("temp_c");
//                        String img = hourObj.getJSONObject("condition").getString("icon");
//                        String wind = hourObj.getString("wind_kph");
//
//                        weatherRVModalArrayList.add(new WeatherRVModal(time,temper,img,wind));
//
//                    }
                    weatherRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}