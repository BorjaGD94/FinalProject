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
    private SqlHelper db;
    private TextView mPlaceDetailsText;
    EditText destinotxt;
    private EditText hometxt;
    private EditText worktxt;

    String origen;
    String destino = "B";

    Double latitude;
    Double longitud;
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
        db = new SqlHelper(this);

        String home = db.getHome().toString();
        if(home != "0") {
            hometxt = findViewById(R.id.home);
            hometxt.setText(home);
        }


        String work= db.getWork().toString();
        if(work != "0") {
            worktxt = findViewById(R.id.work);
            worktxt.setText(work);
        }
        if (extras != null) {
            latitude = extras.getDouble("Latitude");
            longitud = extras.getDouble("longitude");

            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitud + "&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y";
            BackgroundTask bt = new BackgroundTask();
            bt.execute(url);

        }

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



        //home autocompete
        hometxt= findViewById(R.id.home);
        hometxt.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(hometxt.getText().length() == 0) {
                        openAutocompleteActivity();
                        estado = 3;
                    }
                    else {
                        //destino = db.getHome();
                        destinotxt.setText(hometxt.getText().toString());
                        destino_lat = Double.parseDouble(db.getlat("home"));
                        destino_long= Double.parseDouble(db.getlong("home"));
                    }
                     return true;
                }
                return false;

            }
        });

        //work autocomplete
        worktxt = findViewById(R.id.work);
        worktxt.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (worktxt.getText().length() == 0) {
                        openAutocompleteActivity();
                        estado = 4;
                    } else {
                        destinotxt.setText(worktxt.getText().toString());
                        destino_lat = Double.parseDouble(db.getlat("work"));
                        destino_long = Double.parseDouble(db.getlong("work"));
                    }
                    return true;
                }
                return false;

            }
        });

        // Retrieve the TextViews that will display details about the selected place.
        //mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        destinotxt = findViewById(R.id.des);
        origentxt = findViewById(R.id.ori);
        hometxt = findViewById(R.id.home);
        worktxt = findViewById(R.id.work);

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
                        latitude =place.getLatLng().latitude;
                        longitud=place.getLatLng().longitude;
                        break;
                    case 2:
                        destinotxt.setText(place.getAddress().toString());
                        destino=place.getAddress().toString();
                        destino_lat=place.getLatLng().latitude;
                        destino_long=place.getLatLng().longitude;
                        break;
                    case 3:
                        hometxt.setText(place.getAddress().toString());
                        String adress = place.getAddress().toString();
                        String lat = Double.toString(place.getLatLng().latitude);
                        String log = Double.toString(place.getLatLng().longitude);
                        String type = "home";
                        destinotxt.setText(place.getAddress().toString());
                        Adress home = new Adress(adress,lat, log, type);
                        db.addPlace(home);
                        destino_lat =place.getLatLng().latitude;
                        destino_long=place.getLatLng().longitude;

                        break;
                    case 4:
                        worktxt.setText(place.getAddress().toString());
                        String adressw = place.getAddress().toString();
                        String latw = Double.toString(place.getLatLng().latitude);
                        String logw = Double.toString(place.getLatLng().longitude);
                        String typew = "work";
                        destinotxt.setText(place.getAddress().toString());
                        Adress homew = new Adress(adressw,latw, logw, typew);
                        db.addPlace(homew);
                        destino_lat =place.getLatLng().latitude;
                        destino_long=place.getLatLng().longitude;

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
            Log.d("String:", des.toString());
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
        if (destinotxt.getText().length() != 0 && origentxt != null) {
            Intent intent = new Intent(SearchActivity.this, ServicesActivity.class);
                intent.putExtra("origen", origen );
                intent.putExtra("destino", destino);
                intent.putExtra("origen_long", longitud );
                intent.putExtra("origen_lat", latitude );
                intent.putExtra("destino_lat",  destino_lat);
                intent.putExtra("destino_lon",  destino_long);
                startActivity(intent) ;

            }
         else {
            Toast.makeText(SearchActivity.this, "You are missig one of your" +
                    " fields",Toast.LENGTH_SHORT).show();
        }

    }

}