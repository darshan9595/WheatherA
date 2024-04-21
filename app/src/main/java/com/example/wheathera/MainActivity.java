package com.example.wheathera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etCity, etCountry;
    private TextView tvResult, userName;
    private Button saveButton, logout;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    //private final String appid = "44ace1b1529064e2cb9f1b9f976e4e79";
    private final String appid = "577ca53101ec09425beda2aa2a136786";

    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        readDataFromSharePreference();

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null){
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }

        setOnClickListeners();
    }

    private void initializeUI() {
        saveButton = findViewById(R.id.save_button);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
    }

    private void setOnClickListeners() {
        saveButton.setOnClickListener(v -> {
            String s = tvResult.getText().toString();
            Intent intent = new Intent(getApplicationContext(), DataActivity.class);
            intent.putExtra("message_key", s);
            intent.putExtra("message_key", s);
            startActivity(intent);
            try {
                saveDataInSharedPreferences(tvResult.getText().toString().trim());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }

    private void saveDataInSharedPreferences (String city) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharePref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("city", city);
        myEdit.commit();
        Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();
        tvResult.setText(city);
    }

    private void readDataFromSharePreference() {
        SharedPreferences sh = getSharedPreferences("MySharePref", MODE_PRIVATE);
        String city = sh.getString("CITY", "");
        if(city.isEmpty()){
            saveButton.setText("Enregistrer");
        }
        else {
            saveButton.setText("Mise à jour des données");
        }
    }


    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("Le champ de la ville ne peut pas être vide");
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        double windSpeed = Double.parseDouble(wind);
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        tvResult.setTextColor(Color.rgb(0,0,0));
                        tvResult.setTextSize(19);
                        output += "\nMétéo actuelle de " + cityName + " (" + countryName + ")\n"
                                + "\n Température: " + df.format(temp) + " °C"
                                + "\n Ressenti: " + df.format(feelsLike) + " °C"
                                + "\n Humidité: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Vent: " + wind + " m/s (mètres par seconde)"
                                + "\n Couverture nuageuse: " + clouds + "%"
                                + "\n Pression: " + pressure + " hPa"+"\n";
                        tvResult.setText(output);
                        if ((AlertDegreeMessage.degreeCheck(temp)) == true) {
                            output += " \n                   Alerte Météo : \n    TEMPERATURE EXTREME!!!!!\n";
                            tvResult.setText(output);} ;
                       if ((AlertDegreeMessage.windCheck(windSpeed)) == true) {

                            output += " \n            VIGILENCE : VENTS VIOLENT !!!!!";
                            tvResult.setText(output);} ;
                        if (AlertDegreeMessage.estAlerteTsunami(temp, feelsLike,humidity,pressure, windSpeed) == true) {

                            output += " \n                    VIGILENCE : TSUNAMI !!!!!";
                            tvResult.setText(output);} ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}