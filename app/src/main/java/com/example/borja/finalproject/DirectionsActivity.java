package com.example.borja.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DirectionsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;


    private double origen_lat;
    private double origen_long;
    private double destino_lat;
    private double destino_long;
    private String transportType;
    private String mode;
    private String tp;
    private double divvyOrigin_lat;
    private double divvyOrigin_lng;
    private double divvyDestination_lat;
    private double divvyDestination_lng;
    LatLng org;
    LatLng dest;
    LatLng divvyOrg;
    LatLng divvyDest;

    RouteParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            transportType = extras.getString("TransportType");
            Log.d("Type ", transportType);
            origen_lat = extras.getDouble("origen_lat");
            origen_long = extras.getDouble("origen_lng");
            destino_lat = extras.getDouble("destino_lat");
            destino_long = extras.getDouble("destino_lng");

            if (transportType.equals("Divvy")) {
                divvyOrigin_lat = extras.getDouble("divvyOrigin_lat");
                divvyOrigin_lng = extras.getDouble("divvyOrigin_lng");
                divvyDestination_lat = extras.getDouble("divvyDestination_lat");
                divvyDestination_lng = extras.getDouble("divvyDestination_lng");
                divvyOrg = new LatLng(divvyOrigin_lat, divvyOrigin_lng);
                divvyDest = new LatLng(divvyDestination_lat, divvyDestination_lng);
            }
        }
        org = new LatLng(origen_lat, origen_long);
        dest = new LatLng(destino_lat, destino_long);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.addMarker(new MarkerOptions().position(org).icon(BitmapDescriptorFactory.fromResource(R.drawable.origin_marker_48)));
        mMap.addMarker(new MarkerOptions().position(dest).icon(BitmapDescriptorFactory.fromResource(R.drawable.dest_marker_48)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(org));

        if (transportType.equals("Divvy")) {

            String url = getUrl(org, divvyOrg, "DivvyWalk");
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);

            String url1 = getUrl(divvyOrg, divvyDest, transportType);
            FetchUrl FetchUrl1 = new FetchUrl();
            FetchUrl1.execute(url1);

            String url2 = getUrl(divvyDest, dest, "DivvyWalk");
            FetchUrl FetchUrl2 = new FetchUrl();
            FetchUrl2.execute(url2);

            /*FetchUrl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            FetchUrl1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url1);
            FetchUrl2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url2);*/


        } else if (transportType.equals("Walking")) {

            String url = getUrl(org, dest, transportType);
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);

        } else {

            String url = getUrl(org, dest, transportType);
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);

        }

    }

    private String getUrl(LatLng origin, LatLng dest, String type) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Transport mode
        Log.d("Final Type ", type);

        if (type.equals("Walk")) {
            mode = "mode=walking";
        } else if (type.equals("Divvy")) {
            mode = "mode=bicycling";
        } else if (type.equals("DivvyWalk")) {
            mode = "mode=walking";
        } else if (type.equals("Uber") || type.equals("Lyft")) {
            mode = "mode=driving";
        } else {
            mode = "mode=transit";
        }

        // API key
        String key = "key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y";

        // Building the parameters to the web service
        String parameters = mode + "&" + str_origin + "&" + str_dest + "&" + sensor + "&" + key;

        // Output format
        String output = "json";


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                parser = new RouteParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

                tp = parser.getTransportation();

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            ArrayList<LatLng> pointsTransit;
            ArrayList<LatLng> pointsTransitSecond;
            ArrayList<LatLng> pointsWlkSecond;
            ArrayList<LatLng> pointsWlkThird;
            PolylineOptions lineOptions = null;
            PolylineOptions lineOptionsTransit = null;
            PolylineOptions lineOptionsTransitSecond = null;
            PolylineOptions lineOptionsSec = null;
            PolylineOptions lineOptionsThird = null;
            int iterator = 0;

            //Log.d("Result ", result.get(0).);

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                pointsTransit = new ArrayList<>();
                pointsTransitSecond = new ArrayList<>();
                pointsWlkSecond = new ArrayList<>();
                pointsWlkThird = new ArrayList<>();
                lineOptions = new PolylineOptions();
                lineOptionsTransit = new PolylineOptions();
                lineOptionsTransitSecond = new PolylineOptions();
                lineOptionsSec = new PolylineOptions();
                lineOptionsThird = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);


                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (point.containsKey("transport")) {
                        tp = point.get("transport");
                        iterator = Integer.parseInt(point.get("iterator"));
                        Log.d("iterator", String.valueOf(iterator));
                        continue;
                    }
                    if (tp.equals("TRANSIT") && iterator ==  1) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        pointsTransit.add(position);
                    } else if(tp.equals("TRANSIT") && iterator ==  3){
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        pointsTransitSecond.add(position);
                    } else {
                        
                        if (tp.equals("WALKING") && iterator == 2) {
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            pointsWlkSecond.add(position);
                        } else if (tp.equals("WALKING") && iterator == 4){
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            pointsWlkThird.add(position);
                        }
                        else {
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }

                    }

                }
                Cap roundCap = new RoundCap();
                // Adding all the points in the route to LineOptions
                if (transportType.equals("Uber")) {
                    lineOptions.addAll(points);
                    lineOptions.width(15);
                    lineOptions.color(Color.BLACK);
                } else if (transportType.equals("Lyft")) {
                    lineOptions.addAll(points);
                    lineOptions.width(15);
                    lineOptions.color(Color.rgb(234, 11, 140));
                } else if (transportType.equals("Divvy")) {
                    Log.d("Divvy travel is ", tp);
                    if (tp.equals("BICYCLING")) {
                        mMap.addMarker(new MarkerOptions().position(points.get(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.divvy_station)));
                        mMap.addMarker(new MarkerOptions().position(points.get(points.size()-1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.divvy_station)));
                        lineOptions.addAll(points);
                        lineOptions.width(15);
                        lineOptions.color(Color.rgb(62, 180, 230));
                        lineOptions.startCap(roundCap);
                        lineOptions.endCap(roundCap);
                    } else if (tp.equals("WALKING")) {
                        int PATTERN_DASH_LENGTH_PX = 20;
                        int PATTERN_GAP_LENGTH_PX = 20;
                        PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
                        PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
                        List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
                        lineOptions.addAll(points);
                        lineOptions.width(10);
                        lineOptions.color(Color.BLACK);
                        lineOptions.pattern(PATTERN_POLYGON_ALPHA);
                        lineOptions.startCap(roundCap);
                        lineOptions.endCap(roundCap);
                    }
                } else if (transportType.equals("Walk")) {
                    int PATTERN_DASH_LENGTH_PX = 20;
                    int PATTERN_GAP_LENGTH_PX = 20;
                    PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
                    PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
                    List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.BLACK);
                    lineOptions.pattern(PATTERN_POLYGON_ALPHA);
                    lineOptionsSec.addAll(pointsWlkSecond);
                    lineOptionsSec.pattern(PATTERN_POLYGON_ALPHA);
                } else if ((transportType.equals("Transit"))) {

                    int PATTERN_DASH_LENGTH_PX = 20;
                    int PATTERN_GAP_LENGTH_PX = 20;
                    PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
                    PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
                    List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.BLACK);
                    lineOptions.pattern(PATTERN_POLYGON_ALPHA);

                    lineOptionsTransit.addAll(pointsTransit);
                    lineOptionsTransit.width(15);
                    lineOptionsTransit.color(Color.RED);

                    lineOptionsSec.addAll(pointsWlkSecond);
                    lineOptionsSec.width(10);
                    lineOptionsSec.color(Color.BLACK);
                    lineOptionsSec.pattern(PATTERN_POLYGON_ALPHA);

                    if (!pointsTransitSecond.isEmpty()){
                        lineOptionsTransitSecond.addAll(pointsTransitSecond);
                        lineOptionsTransitSecond.width(15);
                        lineOptionsTransitSecond.color(Color.RED);
                    }

                    if (!pointsWlkThird.isEmpty()){
                        lineOptionsThird.addAll(pointsWlkThird);
                        lineOptionsSec.width(10);
                        lineOptionsSec.color(Color.BLACK);
                        lineOptionsSec.pattern(PATTERN_POLYGON_ALPHA);
                    }

                }

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
                mMap.addPolyline(lineOptionsSec);
                mMap.addPolyline(lineOptionsTransit);
                mMap.addPolyline(lineOptionsThird);
                mMap.addPolyline(lineOptionsTransitSecond);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        /*LatLngBounds route = new LatLngBounds(
                new LatLng(origen_lat, origen_long), new LatLng(destino_lat, destino_long));*/

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(origen_lat, origen_long)).include(new LatLng(destino_lat, destino_long));

        // Set the camera to the greatest possible zoom level that includes the
        // bounds

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 125);
        mMap.moveCamera(cameraUpdate);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(route, 125));

        //Place current location marker
        /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));*/

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


}
