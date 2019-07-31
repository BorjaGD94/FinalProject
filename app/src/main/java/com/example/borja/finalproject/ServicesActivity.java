package com.example.borja.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apis.LyftPublicApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServicesActivity extends Activity {

    @SuppressLint("StaticFieldLeak")
    private static ExpandableListView expandableListView;
    @SuppressLint("StaticFieldLeak")
    private static ExpandableListAdapter adapter;
    private double origen_lat;
    private double origen_long;
    private double destino_lat;
    private double destino_long;

    private double divvy_origin_station_lat;
    private double divvy_origin_station_lng;
    private double divvy_destination_station_lat;
    private double divvy_destination_station_lng;

    private String url_driving;
    private String url_walking;
    private String url_bike;
    private String url_public;

    Service svLyft = new Service();

    String uber_high_price_estimate;
    String uber_low_price_estimate;
    String uber_price_estimate;
    String uber_eta;

    ProgressBar pr;
    TextView tx;


    JSONObject walkinginfo;
    JSONObject bikeinfo;
    JSONObject drivinginfo;
    JSONObject publicinfo;
    String time;
    String distance;
    String time_bike;
    String distance_bike;
    Integer distance_car;
    String time_car;
    String time_public;
    String public_fare;
    String distance_public;
    int total_time_divvy;
    int total_distance_divvy;

    AtomicInteger numCompleted = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        pr = (ProgressBar) findViewById(R.id.progressBar6);
        tx = findViewById(R.id.textView);
        pr.getIndeterminateDrawable()
                .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        expandableListView = (ExpandableListView) findViewById(R.id.elistview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        //setItems();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            origen_lat = extras.getDouble("origen_lat");
            origen_long = extras.getDouble("origen_long");
            destino_lat = extras.getDouble("destino_lat");
            destino_long = extras.getDouble("destino_lon");

        }

        /*try {
            uberPriceInfo(new RevealServiceCallbacksUber() {
                @Override
                public void onSuccess(@NonNull String string) {
                    final ObjectMapper mapper = new ObjectMapper();

                    try {
                        UberPriceJSON real = mapper.readValue(string, UberPriceJSON.class);
                        for (int i = 0; i < real.prices.length; i++) {
                            if (real.prices[i].display_name.equals("UberX")) {
                                uber_high_price_estimate = Integer.toString(real.prices[i].high_estimate);
                                uber_low_price_estimate = Integer.toString(real.prices[i].low_estimate);
                                uber_price_estimate = "$" + uber_low_price_estimate + " - " + "$" + uber_high_price_estimate;
                                numCompleted.incrementAndGet();
                                Log.d("Atomic uber_p: ", numCompleted.toString());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull okhttp3.Response response) {
                    Log.d("Error: ", response.toString());
                    if (response.code() == 401) {
                        uber_price_estimate = "0$ - 0$";
                        numCompleted.incrementAndGet();
                        Log.d("Atomic uber_p: ", numCompleted.toString());
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /*try {
            uberEtaInfo(new RevealServiceCallbacksUber() {
                @Override
                public void onSuccess(@NonNull String string) {
                    final ObjectMapper mapper = new ObjectMapper();

                    try {
                        UberEtaJSON real = mapper.readValue(string, UberEtaJSON.class);
                        for (int i = 0; i < real.times.length; i++) {
                            if (real.times[i].display_name.equals("UberX")) {
                                Integer num = (real.times[i].estimate) / 60;
                                uber_eta = "Waiting Time:  " + Integer.toString(num) + " minutes";
                                numCompleted.incrementAndGet();
                                Log.d("Atomic uber_eta: ", numCompleted.toString());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull okhttp3.Response response) {
                    Log.d("Error: ", response.toString());
                    if (response.code() == 401) {
                        uber_eta = "Unauthorized API Access";
                        numCompleted.incrementAndGet();
                        Log.d("Atomic uber_eta: ", numCompleted.toString());
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /*try {
            lyftEtaInfo(new RevealServiceCallbacksLyft() {
                @Override
                public void onSuccess(@NonNull Service service) {
                    //pr.setVisibility(View.INVISIBLE);
                    //tx.setVisibility(View.INVISIBLE);
                    numCompleted.incrementAndGet();
                    Log.d("Atomic Etalyft: ", numCompleted.toString());
                    if (numCompleted.get() == 5) {
                        setItems();
                    }
                    //setListener();
                }

                @Override
                public void onError(@NonNull Service service) {
                    //pr.setVisibility(View.INVISIBLE);
                    //tx.setVisibility(View.INVISIBLE);
                    numCompleted.incrementAndGet();
                    Log.d("Atomic Etalyft: ", numCompleted.toString());
                    if (numCompleted.get() == 5) {
                        setItems();
                    }
                    //setListener();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /*try {
            lyftPriceInfo(new RevealServiceCallbacksLyft() {
                @Override
                public void onSuccess(@NonNull Service service) {
                    //pr.setVisibility(View.INVISIBLE);
                    //tx.setVisibility(View.INVISIBLE);
                    numCompleted.incrementAndGet();
                    Log.d("Atomic Pricelyft: ", numCompleted.toString());
                    if (numCompleted.get() == 5) {
                        setItems();
                        setListener();
                    }
                    //setListener();
                }

                @Override
                public void onError(@NonNull Service service) {
                    //pr.setVisibility(View.INVISIBLE);
                    //tx.setVisibility(View.INVISIBLE);
                    numCompleted.incrementAndGet();
                    Log.d("Atomic Pricelyft: ", numCompleted.toString());
                    if (numCompleted.get() == 5) {
                        setItems();
                        setListener();
                    }
                    //setListener();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        try {
            divvyOriginInfo(new RevealServiceCallbacksDivvy() {
                @Override
                public void onSuccess(@NonNull String string) {

                    final ObjectMapper mapper = new ObjectMapper();

                    DivvyStation[] stations = new DivvyStation[0];

                    try {
                        stations = mapper.readValue(string, DivvyStation[].class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Integer radius = 1000;
                    String stations_strings = "";
                    if (stations.length == 0) {
                        while (radius <= 16000) {
                            try {
                                JSONObject JSON = JsonReader.readJsonFromUrl("https://data.cityofchicago.org/resource/bbyy-e7gq.json?$where=within_circle(location," + origen_lat + "," + origen_long + "," + radius + ")");
                                try {
                                    stations = mapper.readValue(JSON.toString(), DivvyStation[].class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            radius *= 2;
                        }
                        if (radius > 16000) {
                            // Advise the user that there are no near Divvy stations
                            total_distance_divvy = 0;
                            total_time_divvy = 0;

                            numCompleted.incrementAndGet();

                            Log.d("Atomic Divvy ", numCompleted.toString());


                        } else {
                            for (Integer i = 0; i < stations.length; i++) {
                                Log.d("Station ", stations[i].getLocation().toString());
                                String lat = stations[i].getLocation().getLatitude();
                                String lng = stations[i].getLocation().getLongitude();
                                if (i != stations.length - 1) {
                                    stations_strings += lat + "," + lng + "|";
                                } else {
                                    stations_strings += lat + "," + lng;
                                }
                            }
                            Log.d("Origin String ", stations_strings);
                        }
                    } else {
                        for (Integer i = 0; i < stations.length; i++) {
                            Log.d("Station ", stations[i].getLocation().toString());
                            String lat = stations[i].getLocation().getLatitude();
                            String lng = stations[i].getLocation().getLongitude();
                            if (i != stations.length - 1) {
                                stations_strings += lat + "," + lng + "|";
                            } else if (i == stations.length - 1) {
                                stations_strings += lat + "," + lng;
                            }
                        }
                        Log.d("Origin String ", stations_strings);


                        String url_origin = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + stations_strings + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";

                        try {
                            JSONObject JSON = JsonReader.readJsonFromUrl(url_origin);
                            GoogleDistanceMatrixApiResponse new_json = mapper.readValue(JSON.toString(), GoogleDistanceMatrixApiResponse.class);
                            List<Integer> shortest = new ArrayList<Integer>();
                            for (Integer i = 0; i < new_json.rows.get(0).elements.size(); i++) {
                                // Independently of units (metric/Imperial) always expressed in meters
                                Log.d("Distance Origin value", String.valueOf(new_json.rows.get(0).elements.get(i).distance.value));
                                //Imperial units --> miles
                                Log.d("Distance Origin text", String.valueOf(new_json.rows.get(0).elements.get(i).distance.text));
                                shortest.add(new_json.rows.get(0).elements.get(i).distance.value);

                            }

                            List<Integer> distances = new_json.rows.get(0).ElementsToListOfDistances(new_json.rows.get(0).elements);
                            List<Integer> durations = new_json.rows.get(0).ElementsToListOfDurations(new_json.rows.get(0).elements);


                            int minDistance = shortest.stream().sorted().findFirst().get();

                            int minDuration = durations.get(distances.indexOf(minDistance));

                            total_time_divvy += minDuration;
                            total_distance_divvy += minDistance;

                            divvy_origin_station_lat = Double.parseDouble(stations[distances.indexOf(minDistance)].getLocation().getLatitude());
                            divvy_origin_station_lng = Double.parseDouble(stations[distances.indexOf(minDistance)].getLocation().getLongitude());


                            Log.d("Result Origin dist ", String.valueOf(minDistance));
                            Log.d("Result Origin dur ", String.valueOf(minDuration));

                            Log.d("Result Origin lat ", String.valueOf(divvy_origin_station_lat));
                            Log.d("Result Origin long ", String.valueOf(divvy_origin_station_lng));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        numCompleted.incrementAndGet();
                        Log.d("Atomic Divvy ", numCompleted.toString());

                        if (numCompleted.get() == 2) {
                            url_driving = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=driving&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                            url_walking = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                            url_bike = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + divvy_origin_station_lat + "," + divvy_origin_station_lng + "&destinations=" + divvy_destination_station_lat + "," + divvy_destination_station_lng + "&mode=bicycling&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                            url_public = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=transit&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                            BackgroundTask bt = new BackgroundTask();
                            bt.execute(url_walking, url_driving, url_bike, url_public);
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (numCompleted.get() == 3) {
                                    setItems();
                                    setListener();
                                }

                            }
                        });
                    }
                    if (numCompleted.get() == 2) {
                        url_driving = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=driving&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                        url_walking = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                        //url_bike = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + divvy_origin_station_lat + "," + divvy_origin_station_lng + "&destinations=" + divvy_destination_station_lat + "," + divvy_destination_station_lng + "&mode=bicycling&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                        url_public = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=transit&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                        BackgroundTask bt = new BackgroundTask();
                        bt.execute(url_walking, url_driving, url_public);
                    }
                }

                @Override
                public void onError(@NonNull okhttp3.Response response) {
                    Log.d("Error: ", response.toString());
                    if (response.code() == 401) {
                        time_bike = "Unauthorized API Access";
                        numCompleted.incrementAndGet();

                        Log.d("Atomic Divvy_info: ", numCompleted.toString());
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            divvyDestinationInfo(new RevealServiceCallbacksDivvy() {
                @Override
                public void onSuccess(@NonNull String string) {

                    final ObjectMapper mapper = new ObjectMapper();

                    DivvyStation[] stations = new DivvyStation[0];

                    try {
                        stations = mapper.readValue(string, DivvyStation[].class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Integer radius = 1000;
                    String stations_strings = "";
                    if (stations.length == 0) {
                        while (radius <= 16000) {
                            Log.d("Radius ", String.valueOf(radius));
                            try {
                                JSONObject JSON = JsonReader.readJsonFromUrl("https://data.cityofchicago.org/resource/bbyy-e7gq.json?$where=within_circle(location," + destino_lat + "," + destino_long + "," + radius + ")");
                                try {
                                    stations = mapper.readValue(JSON.toString(), DivvyStation[].class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            radius *= 2;
                        }
                        if (radius > 16000) {
                            // Advise the user that there are no near Divvy stations
                            total_distance_divvy = 0;
                            total_time_divvy = 0;

                            numCompleted.incrementAndGet();
                            Log.d("Atomic Divvy ", numCompleted.toString());

                        } else {
                            for (Integer i = 0; i < stations.length; i++) {
                                Log.d("Destination Station ", stations[i].getLocation().toString());
                                String lat = stations[i].getLocation().getLatitude();
                                String lng = stations[i].getLocation().getLongitude();
                                if (i != stations.length - 1) {
                                    stations_strings += lat + "," + lng + "|";
                                } else {
                                    stations_strings += lat + "," + lng;
                                }
                            }
                            Log.d("Destination String ", stations_strings);
                        }
                    } else {
                        for (Integer i = 0; i < stations.length; i++) {
                            Log.d("Destination Station ", stations[i].getLocation().toString());
                            String lat = stations[i].getLocation().getLatitude();
                            String lng = stations[i].getLocation().getLongitude();
                            if (i != stations.length - 1) {
                                stations_strings += lat + "," + lng + "|";
                            } else if (i == stations.length - 1) {
                                stations_strings += lat + "," + lng;
                            }
                        }
                        Log.d("Destination String ", stations_strings);


                        String url_origin = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + destino_lat + "," + destino_long + "&destinations=" + stations_strings + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";

                        try {
                            JSONObject JSON = JsonReader.readJsonFromUrl(url_origin);
                            GoogleDistanceMatrixApiResponse new_json = mapper.readValue(JSON.toString(), GoogleDistanceMatrixApiResponse.class);
                            List<Integer> shortest = new ArrayList<Integer>();
                            for (Integer i = 0; i < new_json.rows.get(0).elements.size(); i++) {
                                // Independently of units (metric/Imperial) always expressed in meters
                                Log.d("Dest Distance value", String.valueOf(new_json.rows.get(0).elements.get(i).distance.value));
                                //Imperial units --> miles
                                Log.d("Dest Distance text", String.valueOf(new_json.rows.get(0).elements.get(i).distance.text));
                                shortest.add(new_json.rows.get(0).elements.get(i).distance.value);

                            }

                            List<Integer> distances = new_json.rows.get(0).ElementsToListOfDistances(new_json.rows.get(0).elements);
                            List<Integer> durations = new_json.rows.get(0).ElementsToListOfDurations(new_json.rows.get(0).elements);


                            int minDistance = shortest.stream().sorted().findFirst().get();

                            int minDuration = durations.get(distances.indexOf(minDistance));

                            total_time_divvy += minDuration;
                            total_distance_divvy += minDistance;

                            Log.d("Dest Result dist ", String.valueOf(minDistance));
                            Log.d("Dest Result dur ", String.valueOf(minDuration));

                            divvy_destination_station_lat = Double.parseDouble(stations[distances.indexOf(minDistance)].getLocation().getLatitude());
                            divvy_destination_station_lng = Double.parseDouble(stations[distances.indexOf(minDistance)].getLocation().getLongitude());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        numCompleted.incrementAndGet();
                        Log.d("Atomic Divvy ", numCompleted.toString());

                        if (numCompleted.get() == 2) {
                            url_driving = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=driving&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                            url_walking = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                            url_bike = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + divvy_origin_station_lat + "," + divvy_origin_station_lng + "&destinations=" + divvy_destination_station_lat + "," + divvy_destination_station_lng + "&mode=bicycling&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                            url_public = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=transit&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                            BackgroundTask bt = new BackgroundTask();
                            bt.execute(url_walking, url_driving, url_bike, url_public);
                        }


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (numCompleted.get() == 3) {
                                    setItems();
                                    setListener();
                                }

                            }
                        });
                    }
                    if (numCompleted.get() == 2) {
                        url_driving = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=driving&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                        url_walking = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                        //url_bike = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + divvy_origin_station_lat + "," + divvy_origin_station_lng + "&destinations=" + divvy_destination_station_lat + "," + divvy_destination_station_lng + "&mode=bicycling&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
                        url_public = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + origen_lat + "," + origen_long + "&destinations=" + destino_lat + "," + destino_long + "&mode=transit&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&\"";
                        BackgroundTask bt = new BackgroundTask();
                        bt.execute(url_walking, url_driving, url_public);
                    }
                }

                @Override
                public void onError(@NonNull okhttp3.Response response) {
                    Log.d("Error: ", response.toString());
                    if (response.code() == 401) {
                        time_bike = "Unauthorized API Access";
                        numCompleted.incrementAndGet();
                        Log.d("Atomic Divvy_info: ", numCompleted.toString());
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    // Setting headers and childs to expandable listview
    void setItems() {


        // Array list for header
        ArrayList<String> header = new ArrayList<String>();

        // Array list for child items
        List<Service> child1 = new ArrayList<Service>();
        List<Service> child2 = new ArrayList<Service>();
        List<Service> child3 = new ArrayList<Service>();
        List<Service> child4 = new ArrayList<Service>();
        List<Service> child5 = new ArrayList<Service>();


        // Hash map for both header and child
        HashMap<String, List<Service>> hashMap = new HashMap<String, List<Service>>();

        // Adding headers to list

        header.add("Ride Sharing");
        header.add("Car Sharing");
        header.add("Bike Sharing");
        header.add("Walking");
        header.add("Public Transport");

        /*try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Log.d("Atomic Total ", numCompleted.toString());

        pr.setVisibility(View.INVISIBLE);
        tx.setVisibility(View.INVISIBLE);

        DecimalFormat dftwo = new DecimalFormat("###.##");

        // Adding child data
        String serviceName = "Uber";
        String servicePrice;
        int serviceLogo = R.drawable.uber_32;
        //String serviceTime = uber_eta;
        String serviceTime = "Travel time: " + time_car;
        //String servicePrice = uber_price_estimate;
        if ((3.2 + 0.24 * Double.parseDouble(time_car.replaceAll("\\D+", "")) + 1.06 * distance_car * 0.000621371) < 6.09){
            servicePrice = "$6.09";
        } else{
            servicePrice = "$" + dftwo.format(3.2 + 0.24 * Double.parseDouble(time_car.replaceAll("\\D+", "")) + 1.06 * distance_car * 0.000621371);
        }
        Service sv = new Service(serviceName, serviceLogo, serviceTime, servicePrice);

        String sN1 = "Lyft";
        String sP1;
        int sL1 = R.drawable.lyft_32;
        //String sT1 = svLyft.getServiceTime();
        String sT1 = "Travel time: " + time_car;
        //String sP1 = svLyft.getServicePrice();
        if ((2.8 + 0.09 * Double.parseDouble(time_car.replaceAll("\\D+", "")) + 0.9 * distance_car * 0.000621371) < 3.5){
            sP1 = "$ 3.50";
        } else {
            sP1 = "$" + dftwo.format(2.8 + 0.09 * Double.parseDouble(time_car.replaceAll("\\D+", "")) + 0.9 * distance_car * 0.000621371);
        }
        Service sv1 = new Service(sN1, sL1, sT1, sP1);

        String sN2 = "Via";
        int sL2 = R.drawable.via_32;
        String sT2 = "API not availabe";
        String sP2 = "Get the app";
        Service sv2 = new Service(sN2, sL2, sT2, sP2);

        child1.add(sv);
        child1.add(sv1);
        child1.add(sv2);

        String sN3 = "Car2Go";
        int sL3 = R.drawable.car2go;
        String sT3 = "API not availabe";
        String sP3 = "Get the app";
        Service sv3 = new Service(sN3, sL3, sT3, sP3);

        String sN4 = "ZityCar";
        int sL4 = R.drawable.zitycar;
        String sT4 = "API not availabe";
        String sP4 = "Get the app";
        Service sv4 = new Service(sN4, sL4, sT4, sP4);

        // Adding child data
        child2.add(sv3);
        child2.add(sv4);

        DecimalFormat dfone = new DecimalFormat("###.#");

        // Adding child data

        String sT5;
        if (total_time_divvy == 0) {
            sT5 = "";
        } else {
            sT5 = "Time: " + Math.rint(total_time_divvy / 60) + " mins";
        }

        int sL5 = R.drawable.divvy_logo;
        String sN5;
        if (total_distance_divvy == 0) {
            sN5 = "Not available in this area";
        } else {
            sN5 = "Distance: " + dfone.format(total_distance_divvy * 0.000621371) + " miles";
        }

        String sP5;
        if (total_distance_divvy == 0) {
            sP5 = "$0";
        }else if (Math.rint(total_time_divvy / 60) <= 30) {
            sP5 = "$3.00";
        } else sP5 = "$15.00";
        Service sv5 = new Service(sN5, sL5, sT5, sP5);

        child3.add(sv5);

        // Adding child data
        String sT6 = "Time: " + time;
        int sL6 = R.drawable.walking_32;
        String sN6 = "Distance " + distance;
        String sP6 = "Free & Healthy";
        Service sv6 = new Service(sN6, sL6, sT6, sP6);

        child4.add(sv6);

        String sT7 = "Time: " + time_public;
        int sL7 = R.drawable.public_32;
        String sN7 = "Distance " + distance_public;
        String sP7 = public_fare;
        Service sv7 = new Service(sN7, sL7, sT7, sP7);

        child5.add(sv7);

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);
        hashMap.put(header.get(4), child5);


        adapter = new ExpandableListAdapter(ServicesActivity.this, header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        /*expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {
                return false;
            }
        });*/

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        /*expandableListView
                .setOnGroupExpandListener(new OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });*/

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {

                if ((groupPos == 0) && (childPos == 0)) {
                    Intent intent = new Intent(ServicesActivity.this, DirectionsActivity.class);
                    intent.putExtra("TransportType", "Uber");
                    intent.putExtra("origen_lat", origen_lat);
                    intent.putExtra("origen_lng", origen_long);
                    intent.putExtra("destino_lat", destino_lat);
                    intent.putExtra("destino_lng", destino_long);
                    startActivity(intent);
                }

                if ((groupPos == 0) && (childPos == 1)) {
                    Intent intent = new Intent(ServicesActivity.this, DirectionsActivity.class);
                    intent.putExtra("TransportType", "Lyft");
                    intent.putExtra("origen_lat", origen_lat);
                    intent.putExtra("origen_lng", origen_long);
                    intent.putExtra("destino_lat", destino_lat);
                    intent.putExtra("destino_lng", destino_long);
                    startActivity(intent);
                }

                if ((groupPos == 0) && (childPos == 2)) {
                    Intent launchViaApp = getPackageManager().getLaunchIntentForPackage("via.rider");
                    if (launchViaApp != null) {
                        startActivity(launchViaApp);
                    } else {
                        launchViaApp = new Intent(Intent.ACTION_VIEW);
                        launchViaApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchViaApp.setData(Uri.parse("market://details?id=" + "via.rider"));
                        startActivity(launchViaApp);
                    }
                }

                if ((groupPos == 1) && (childPos == 0)) {
                    Intent launchCar2GoApp = getPackageManager().getLaunchIntentForPackage("com.car2go");
                    if (launchCar2GoApp != null) {
                        startActivity(launchCar2GoApp);
                    } else {
                        launchCar2GoApp = new Intent(Intent.ACTION_VIEW);
                        launchCar2GoApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchCar2GoApp.setData(Uri.parse("market://details?id=" + "com.car2go"));
                        startActivity(launchCar2GoApp);
                    }
                }

                if ((groupPos == 1) && (childPos == 1)) {
                    Intent launchZipcarApp = getPackageManager().getLaunchIntentForPackage("com.zc.android");
                    if (launchZipcarApp != null) {
                        startActivity(launchZipcarApp);
                    } else {
                        launchZipcarApp = new Intent(Intent.ACTION_VIEW);
                        launchZipcarApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchZipcarApp.setData(Uri.parse("market://details?id=" + "com.zc.android"));
                        startActivity(launchZipcarApp);
                    }
                }

                if ((groupPos == 2) && (childPos == 0)) {
                    if (total_time_divvy == 0){
                        Toast.makeText(ServicesActivity.this, "Route not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(ServicesActivity.this, DirectionsActivity.class);
                        intent.putExtra("TransportType", "Divvy");
                        intent.putExtra("origen_lat", origen_lat);
                        intent.putExtra("origen_lng", origen_long);
                        intent.putExtra("divvyOrigin_lat", divvy_origin_station_lat);
                        intent.putExtra("divvyOrigin_lng", divvy_origin_station_lng);
                        intent.putExtra("divvyDestination_lat", divvy_destination_station_lat);
                        intent.putExtra("divvyDestination_lng", divvy_destination_station_lng);
                        intent.putExtra("destino_lat", destino_lat);
                        intent.putExtra("destino_lng", destino_long);
                        startActivity(intent);
                    }
                }

                if ((groupPos == 3) && (childPos == 0)) {
                    Intent intent = new Intent(ServicesActivity.this, DirectionsActivity.class);
                    intent.putExtra("TransportType", "Walk");
                    intent.putExtra("origen_lat", origen_lat);
                    intent.putExtra("origen_lng", origen_long);
                    intent.putExtra("destino_lat", destino_lat);
                    intent.putExtra("destino_lng", destino_long);
                    startActivity(intent);
                }

                if ((groupPos == 4) && (childPos == 0)) {
                    Intent intent = new Intent(ServicesActivity.this, DirectionsActivity.class);
                    intent.putExtra("TransportType", "Transit");
                    intent.putExtra("origen_lat", origen_lat);
                    intent.putExtra("origen_lng", origen_long);
                    intent.putExtra("destino_lat", destino_lat);
                    intent.putExtra("destino_lng", destino_long);
                    startActivity(intent);
                }


                //Toast.makeText(
                //      ServicesActivity.this,
                //    "You clicked : " + adapter.getChild(groupPos, childPos),
                //  Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    public interface RevealServiceCallbacksLyft {
        void onSuccess(@NonNull Service service);

        void onError(@NonNull Service service);

    }

    public void lyftEtaInfo(@Nullable final RevealServiceCallbacksLyft callbacks) throws InterruptedException {

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("TqXRrq9FM124")
                .setClientToken("/oU5+AqwehTKXFCVQT8D0ZAXwAOVfNTAu+dAxLuPUSnqGN/0JaNI1VX0TnWIcrj+HSawTSgbqSwoMjkkBzzu6sG9M6VFoNtLNB90MuhcWrqKPTjhNAZejls=")
                .build();

        LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();

        //Get driver estimated time of arrival for a location.

        //Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(41.949740, -87.652110, "lyft");
        Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(origen_lat, origen_long, "lyft");

        etaCall.enqueue(new Callback<EtaEstimateResponse>() {

            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {

                Log.d("LyftETA Response code: ", Integer.toString(response.code()));
                // If the response code is 401, I have no authorization to use the Lyft API.
                if (response.code() == 401) {
                    Log.d("Unauthorized request ", Integer.toString(response.code()));
                    svLyft.setServiceTime("Unauthorized API Access");
                    callbacks.onError(svLyft);
                } else {
                    EtaEstimateResponse result = response.body();
                    Log.d("Result: ", result.eta_estimates.toString());
                    for (Eta eta : result.eta_estimates) {
                        Log.d("Lyft ETA ", "ETA for " + eta.ride_type + ": " + (eta.eta_seconds / 60) + " min");
                        if ((eta.eta_seconds / 60) == 1) {
                            svLyft.setServiceTime("Waiting Time:  " + Integer.toString(eta.eta_seconds / 60) + " minute");
                        } else {
                            svLyft.setServiceTime("Waiting Time:  " + Integer.toString(eta.eta_seconds / 60) + " minutes");
                        }
                        if (callbacks != null)
                            callbacks.onSuccess(svLyft);
                    }
                }
            }


            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                Log.d("MyApp", t.toString());
                if (callbacks != null)
                    callbacks.onError(svLyft);
            }
        });
    }

    public void lyftPriceInfo(@Nullable final RevealServiceCallbacksLyft callbacks) throws InterruptedException {

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("TqXRrq9FM124")
                .setClientToken("/oU5+AqwehTKXFCVQT8D0ZAXwAOVfNTAu+dAxLuPUSnqGN/0JaNI1VX0TnWIcrj+HSawTSgbqSwoMjkkBzzu6sG9M6VFoNtLNB90MuhcWrqKPTjhNAZejls=")
                .build();

        LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();

        Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(origen_lat, origen_long, RideTypeEnum.CLASSIC.toString(), destino_lat, destino_long);
        costEstimateCall.enqueue(new Callback<CostEstimateResponse>() {

            @Override
            public void onResponse(Call<CostEstimateResponse> call, Response<CostEstimateResponse> response) {
                // If the response code is 401, I have no authorization to use the Lyft API.
                if (response.code() == 401) {
                    Log.d("LyftPrice response code", Integer.toString(response.code()));
                    svLyft.setServicePrice("0$ - 0$");
                    callbacks.onError(svLyft);
                } else {
                    CostEstimateResponse result = response.body();
                    Log.d("Result: ", result.cost_estimates.toString());
                    for (CostEstimate costEstimate : result.cost_estimates) {
                        Log.d("MyApp", "Min: " + String.valueOf(costEstimate.estimated_cost_cents_min / 100) + "$");
                        Log.d("MyApp", "Max: " + String.valueOf(costEstimate.estimated_cost_cents_max / 100) + "$");
                        Log.d("MyApp", "Distance: " + String.valueOf(costEstimate.estimated_distance_miles) + " miles");
                        Log.d("MyApp", "Duration: " + String.valueOf(costEstimate.estimated_duration_seconds / 60) + " minutes");
                        svLyft.setServicePrice("$" + Integer.toString(costEstimate.estimated_cost_cents_min / 100) + " - $" + Integer.toString(costEstimate.estimated_cost_cents_max / 100));

                        if (callbacks != null)
                            callbacks.onSuccess(svLyft);
                        //Log.d("LyftInfo function 1", "price: "+svLyft.getServicePrice());
                    }
                }
            }

            @Override
            public void onFailure(Call<CostEstimateResponse> call, Throwable t) {
                Log.d("MyApp", t.toString());
                if (callbacks != null)
                    callbacks.onError(svLyft);
            }
        });
    }

    public interface RevealServiceCallbacksUber {
        void onSuccess(@NonNull String string);

        void onError(@NonNull okhttp3.Response response);

    }

    public void uberEtaInfo(@Nullable final RevealServiceCallbacksUber callbacks) throws InterruptedException {

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://api.uber.com/v1.2/estimates/time?start_latitude=" + origen_lat + "&start_longitude=" + origen_long + "")
                .get()
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer JA.VUNmGAAAAAAAEgASAAAABwAIAAwAAAAAAAAAEgAAAAAAAAG8AAAAFAAAAAAADgAQAAQAAAAIAAwAAAAOAAAAkAAAABwAAAAEAAAAEAAAAL8G_maH7uZdBTeNTR3cADFsAAAATnNngEjr0oqyV-arCUc5OWQbCf-i9APk1SJIt2COdsZUuJDdq_1h2eA4bzXnsTgrB5_BL6SuQZ5rsMjkoATDQMSCq8dlEE8kh9vPF36Z6kO0UMbXj06vvYIv9BnTE2cZCXQIk0tdQZq9b5s5DAAAANO4Nzg9VeWXur_X0iQAAABiMGQ4NTgwMy0zOGEwLTQyYjMtODA2ZS03YTRjZjhlMTk2ZWU")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "d33ebdb9-722c-4b54-9276-01091d6f5e75,12e2d824-5576-4a74-a95c-6f7269e3c250")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        callbacks.onError(response);
                        throw new IOException("Unexpected code " + response);
                    }
                    /*Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }*/

                    //System.out.println(responseBody.string());
                    //System.out.println(responseBody.toString());
                    if (callbacks != null)
                        callbacks.onSuccess(responseBody.string());

                }
            }
        });

    }

    public void uberPriceInfo(@Nullable final RevealServiceCallbacksUber callbacks) throws InterruptedException {
        OkHttpClient client1 = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url("https://api.uber.com/v1.2/estimates/price?start_latitude=" + origen_lat + "&start_longitude=" + origen_long + "&end_latitude=" + destino_lat + "&end_longitude=" + destino_long + "")
                .get()
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer JA.VUNmGAAAAAAAEgASAAAABwAIAAwAAAAAAAAAEgAAAAAAAAG8AAAAFAAAAAAADgAQAAQAAAAIAAwAAAAOAAAAkAAAABwAAAAEAAAAEAAAACubZch86F7nPuWF2Md1OqZsAAAA5viiUialU07VQS6xetN2Wr1Z6TLmAjtotoxaxgLc27njC4uM9mFpKRyzxLj0NI0PjW3tq7Z6skrqeodNwK3MeigB4MtkMsIb4n06TJ2L7nVGjwQfoChCjyulakVOv25jAdgO8YZzJNQdgUV2DAAAAMsPW48ZNUaZCgBa1iQAAABiMGQ4NTgwMy0zOGEwLTQyYjMtODA2ZS03YTRjZjhlMTk2ZWU")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "b972ba66-6edf-4f0e-b599-c1d95dcc2ad3,0e82d3ee-9dd9-4b48-a54c-9850ac18cb20")
                .build();

        client1.newCall(request1).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        callbacks.onError(response);
                        throw new IOException("Unexpected code " + response);
                    }

                    if (callbacks != null)
                        callbacks.onSuccess(responseBody.string());

                }
            }
        });
    }

    public interface RevealServiceCallbacksDivvy {
        void onSuccess(@NonNull String string);

        void onError(@NonNull okhttp3.Response response);

    }

    public void divvyOriginInfo(@Nullable final RevealServiceCallbacksDivvy callbacks) throws InterruptedException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://data.cityofchicago.org/resource/bbyy-e7gq.json?$where=within_circle(location," + origen_lat + "," + origen_long + ",500)")
                .get()
                .addHeader("User-Agent", "PostmanRuntime/7.15.0")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "cd658dcc-f83b-44cf-8b81-d1ef6456ac28,5941a02c-b4a2-47dd-9dc9-b554c3e749e5")
                .addHeader("Host", "data.cityofchicago.org")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response divvyResponse) throws IOException {
                try (ResponseBody responseBody = divvyResponse.body()) {
                    if (!divvyResponse.isSuccessful()) {
                        callbacks.onError(divvyResponse);
                        throw new IOException("Unexpected code " + divvyResponse);
                    }

                    if (callbacks != null)
                        callbacks.onSuccess(responseBody.string());
                }
            }
        });
    }

    public void divvyDestinationInfo(@Nullable final RevealServiceCallbacksDivvy callbacks) throws InterruptedException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://data.cityofchicago.org/resource/bbyy-e7gq.json?$where=within_circle(location," + destino_lat + "," + destino_long + ",500)")
                .get()
                .addHeader("User-Agent", "PostmanRuntime/7.15.0")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "cd658dcc-f83b-44cf-8b81-d1ef6456ac28,5941a02c-b4a2-47dd-9dc9-b554c3e749e5")
                .addHeader("Host", "data.cityofchicago.org")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response divvyResponse) throws IOException {
                try (ResponseBody responseBody = divvyResponse.body()) {
                    if (!divvyResponse.isSuccessful()) {
                        callbacks.onError(divvyResponse);
                        throw new IOException("Unexpected code " + divvyResponse);
                    }

                    if (callbacks != null)
                        callbacks.onSuccess(responseBody.string());
                }
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... params) {


            if (params.length == 3){
                url_walking = new String(params[0]);
                url_driving = new String(params[1]);
                url_public = new String(params[2]);
            } else {
                url_walking = new String(params[0]);
                url_driving = new String(params[1]);
                url_bike = new String(params[2]);
                url_public = new String(params[3]);
            }

            try {
                if (params.length == 3){
                    walkinginfo = JsonReader.readJsonFromUrl(url_walking);
                    drivinginfo = JsonReader.readJsonFromUrl(url_driving);
                    publicinfo = JsonReader.readJsonFromUrl(url_public);

                } else {
                    walkinginfo = JsonReader.readJsonFromUrl(url_walking);
                    bikeinfo = JsonReader.readJsonFromUrl(url_bike);
                    drivinginfo = JsonReader.readJsonFromUrl(url_driving);
                    publicinfo = JsonReader.readJsonFromUrl(url_public);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //This is the Firebase URL where data will be fetched from


            return null;

        }

        protected void onPostExecute(Void result) {


            final ObjectMapper mapper = new ObjectMapper();

            try {
                GoogleDistanceMatrixApiResponse real = mapper.readValue(walkinginfo.toString(), GoogleDistanceMatrixApiResponse.class);
                distance = real.rows.get(0).elements.get(0).distance.text;
                //distance = real.rows.get(0).elements.get(0).distance.text;
                time = real.rows.get(0).elements.get(0).duration.text;
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                GoogleDistanceMatrixApiResponse real1 = mapper.readValue(drivinginfo.toString(), GoogleDistanceMatrixApiResponse.class);
                distance_car = real1.rows.get(0).elements.get(0).distance.value;
                time_car = real1.rows.get(0).elements.get(0).duration.text;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (total_time_divvy != 0){
                try {
                    GoogleDistanceMatrixApiResponse real1 = mapper.readValue(bikeinfo.toString(), GoogleDistanceMatrixApiResponse.class);
                    distance_bike = real1.rows.get(0).elements.get(0).distance.text;
                    int dis = real1.rows.get(0).elements.get(0).distance.value;
                    time_bike = real1.rows.get(0).elements.get(0).duration.text;
                    int tim = real1.rows.get(0).elements.get(0).duration.value;
                    total_distance_divvy += dis;
                    total_time_divvy += tim;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            try {
                GoogleDistanceMatrixApiResponse real1 = mapper.readValue(publicinfo.toString(), GoogleDistanceMatrixApiResponse.class);
                distance_public = real1.rows.get(0).elements.get(0).distance.text;
                time_public = real1.rows.get(0).elements.get(0).duration.text;
                if (real1.rows.get(0).elements.get(0).fare == null){
                    public_fare = "2.5$";
                } else {
                    public_fare = real1.rows.get(0).elements.get(0).fare.text;
                }
                Log.d("time transit: ", time_public);
            } catch (IOException e) {
                e.printStackTrace();
            }

            numCompleted.incrementAndGet();
            Log.d("Atomic BackgroundTasks ", numCompleted.toString());

            if (numCompleted.get() == 3) {
                setItems();
                setListener();
            }

        }
    }
}
