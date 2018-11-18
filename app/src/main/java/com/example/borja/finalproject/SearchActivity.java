package com.example.borja.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
/*
        Intent intent = getIntent();
        String latitude = intent.getStringExtra("latitude");
        String longitud = intent.getStringExtra("longitud");
        */
        Bundle extras =getIntent().getExtras();

        if(extras != null) {
            String latitude = extras.getString("Latitude");
            String longitud = extras.getString("longitude");



            try {
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitud + "&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y";
                JSONObject origin = JsonReader.readJsonFromUrl(url);
                Log.d("url", "url");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*
            try {
                String valorOrigen = origin.getString("<formattedAdress>");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }
    }
}
