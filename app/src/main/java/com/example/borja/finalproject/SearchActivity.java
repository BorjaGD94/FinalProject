package com.example.borja.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private TextView mPlaceDetailsText;
    private EditText destinotxt;
    private EditText hometext;
    private EditText oritext;

    String origen;
    String destino = "B";

    String latitude;
    String longitud;
    Double  destino_lat;
    Double  destino_long;
    TextView origentxt;
    JSONObject dirOri = null;
    int estado;

    @SuppressLint("ClickableViewAccessibility")
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

        //autocompleteactivity destino

        destinotxt = findViewById(R.id.des);
        destinotxt.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    openAutocompleteActivity();
                    destinotxt.setText("");
                    estado = 2;
                    return true;
                }
                return false;

            }
        });

        //origen autocomple
        origentxt = findViewById(R.id.ori);
        origentxt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    openAutocompleteActivity();
                    origentxt.setText("");
                    estado = 1 ;
                    return true;
                }
                return false;

            }
        });

        // Retrieve the TextViews that will display details about the selected place.
        //mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        destinotxt = findViewById(R.id.des);
        origentxt = findViewById(R.id.ori);

    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                //Log.i(TAG, "Place Selected: " + place.getName());


                // Format the place's details and display them in the TextView.
                switch(estado) {
                    case 1:
                        origentxt.setText("");
                        origentxt.setText(place.getAddress().toString());
                        origen=place.getAddress().toString();
                        break;
                    case 2:
                        destinotxt.setText(place.getAddress().toString());
                        destino=place.getAddress().toString();
                        destino_lat=place.getLatLng().latitude;
                        destino_long=place.getLatLng().longitude;
                        break;
                    case 3:
                        break;
                    case 4:
                } //end switch

           //mPlaceDetailsText.setText(destino+","+ origen);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                //Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }


    //downlaod origin direction pass on the intend
    public class BackgroundTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... params) {
            String url;

            url = new String(params[0]);
            try {
                dirOri = JsonReader.readJsonFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {
            final ObjectMapper mapper = new ObjectMapper();

            String des = dirOri.toString();
            Log.d("String:", des);
            try {
                GoogleGeoCodeResponse result1 = mapper.readValue(des, GoogleGeoCodeResponse.class);
                origen = result1.results[0].formatted_address;
            } catch (IOException e) {
                e.printStackTrace();
            }

            origentxt = findViewById(R.id.ori);
            origentxt.setText(origen);
            

/*
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
*/
        }
    }
   //change activity.
    public void onClick(View view) {

        if (destinotxt != null && origentxt != null) {
            Intent intent = new Intent(SearchActivity.this, ServicesActivity.class);
                intent.putExtra("origen", origen );
                intent.putExtra("destino", destino);
                intent.putExtra("origen_long", longitud );
                intent.putExtra("origen_lat", latitude );
                intent.putExtra("destino_lat",  destino_lat);
                intent.putExtra("destino_lon",  destino_long);
                startActivity(intent) ;

            }

    }

}