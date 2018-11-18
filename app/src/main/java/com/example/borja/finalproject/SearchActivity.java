package com.example.borja.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    String latitude;
    String longitud;
    TextView txtView;
    TextView txtView2;
    JSONObject dirOri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            latitude = extras.getString("Latitude");
            longitud = extras.getString("longitude");

            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitud + "&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y";
            BackgroundTask bt = new BackgroundTask();
            bt.execute(url);

        }
    }

    //downlaod origin direction pass on the intend
    public class BackgroundTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... params) {
            String url;

            url = new String(params[0]);
            try {
                dirOri =   JsonReader.readJsonFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {


            String origen = "A";
            String des = dirOri.toString();
            try {
               origen =  dirOri.getString("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            txtView = findViewById(R.id.ori);
            txtView.setText(origen);
            txtView2 = findViewById(R.id.des);
            txtView2.setText(des);


            JSONArray jsonArray = null;
            try {
                jsonArray = dirOri.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    JSONObject jsonObjectHijo = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    Log.e("Parser JSON", e.toString());
                }
            }

        }
    }
}
