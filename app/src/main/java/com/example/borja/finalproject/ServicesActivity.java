package com.example.borja.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    Service svLyft = new Service();

    String uber_high_price_estimate;
    String uber_low_price_estimate;
    String uber_price_estimate;
    String uber_eta;

    ProgressBar pr;
    TextView tx;


    JSONObject walkinginfo;
    JSONObject bikeinfo;
    String time;
    String distance;
    String time_bike;
    String distance_bike;

    AtomicInteger numCompleted = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        pr = (ProgressBar)findViewById(R.id.progressBar6);
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

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+origen_lat+","+origen_long+"&destinations="+destino_lat+","+destino_long+"&mode=walking&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
        String url_bike = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+origen_lat+","+origen_long+"&destinations="+destino_lat+","+destino_long+"&mode=bicycling&key=AIzaSyBl5T6cMmQC0Zxi8rY1lXW1KH2sFJkCb6Y&";
        BackgroundTask bt = new BackgroundTask();
        bt.execute(url,url_bike);

        try {
            uberPriceInfo(new RevealServiceCallbacksUber() {
                @Override
                public void onSuccess(@NonNull String string) {
                    final ObjectMapper mapper = new ObjectMapper();

                    try {
                        UberPriceJSON real = mapper.readValue(string, UberPriceJSON.class);
                        for(int i=0; i<real.prices.length; i++){
                            if(real.prices[i].display_name.equals("UberX")){
                                uber_high_price_estimate = Integer.toString(real.prices[i].high_estimate);
                                uber_low_price_estimate = Integer.toString(real.prices[i].low_estimate);
                                uber_price_estimate = "$"+uber_low_price_estimate+" - "+"$"+uber_high_price_estimate;
                                numCompleted.incrementAndGet();
                                Log.d("Atomic uber_p: ",numCompleted.toString());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull Throwable throwable) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            uberEtaInfo(new RevealServiceCallbacksUber() {
                @Override
                public void onSuccess(@NonNull String string) {
                    final ObjectMapper mapper = new ObjectMapper();

                    try {
                        UberEtaJSON real = mapper.readValue(string, UberEtaJSON.class);
                        for(int i=0; i<real.times.length; i++){
                            if(real.times[i].display_name.equals("UberX")){
                                Integer num = (real.times[i].estimate)/60;
                                uber_eta = Integer.toString(num);
                                numCompleted.incrementAndGet();
                                Log.d("Atomic uber_eta: ",numCompleted.toString());
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull Throwable throwable) {

                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            lyftInfo(new RevealServiceCallbacks() {
                @Override
                public void onSuccess(@NonNull Service service) {
                    pr.setVisibility(View.INVISIBLE);
                    tx.setVisibility(View.INVISIBLE);
                    numCompleted.incrementAndGet();
                    Log.d("Atomic lyft: ",numCompleted.toString());
                    setItems();
                    setListener();
                }

                @Override
                public void onError(@NonNull Throwable throwable) {

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


        // Hash map for both header and child
        HashMap<String, List<Service>> hashMap = new HashMap<String, List<Service>>();

        // Adding headers to list

        header.add("Ride Sharing");
        header.add("Car Sharing");
        header.add("Bike Sharing");
        header.add("Walking");

        /*try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if (numCompleted.toString().equals("3")){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setItems();
        }

        // Adding child data
        String serviceName = "Uber";
        int serviceLogo = R.drawable.uber_32;
        String serviceTime = "Waiting Time:  "+uber_eta+" minutes";
        String servicePrice = uber_price_estimate;
        Service sv = new Service(serviceName, serviceLogo, serviceTime, servicePrice);

        String sN1 = "Lyft";
        int sL1 = R.drawable.lyft_32;
        String sT1 = svLyft.getServiceTime();
        String sP1 = svLyft.getServicePrice();
        Service sv1 = new Service(sN1, sL1, sT1, sP1);

        String sN2 = "Via";
        int sL2 = R.drawable.via_32;
        String sT2 = "Waiting Time:  8 minutes";
        String sP2 = "$8 - $11";
        Service sv2 = new Service(sN2, sL2, sT2, sP2);

        child1.add(sv);
        child1.add(sv1);
        child1.add(sv2);

        String sN3 = "Car2Go";
        int sL3 = R.drawable.car2go;
        String sT3 = "Walking Time:  4 minutes";
        String sP3 = "$8 - $11";
        Service sv3 = new Service(sN3, sL3, sT3, sP3);

        String sN4 = "ZityCar";
        int sL4 = R.drawable.zitycar;
        String sT4 = "Walking Time:  4 minutes";
        String sP4 = "$8 - $11";
        Service sv4 = new Service(sN4, sL4, sT4, sP4);

        // Adding child data
        child2.add(sv3);
        child2.add(sv4);

        // Adding child data
        String sT5 = "Time: " + time_bike;
        int sL5 = R.drawable.divvy_logo;
        String sN5 = "Distance " + distance_bike;

        String sP5;
        String numero = time_bike.replaceAll("[^0-9]", "");
        if (Integer.parseInt(numero) <= 30) {
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

        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);


        adapter = new ExpandableListAdapter(ServicesActivity.this, header, hashMap);

        // Setting adpater over expandablelistview
        expandableListView.setAdapter(adapter);
    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {

                //Toast.makeText(ServicesActivity.this,
                  //      "You clicked : " + adapter.getGroup(group_pos),
                    //    Toast.LENGTH_LONG).show();
                return false;
            }
        });

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
              //  if ((groupPos == 3) && (childPos == 0)) {
                //    Intent intent = new Intent(ServicesActivity.this, MapsActivity.class);
                    //intent.putExtra("origen", origen);
                    //intent.putExtra("destino", destino);
                  //  startActivity(intent);
                //}

                //Toast.makeText(
                  //      ServicesActivity.this,
                    //    "You clicked : " + adapter.getChild(groupPos, childPos),
                      //  Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public interface RevealServiceCallbacks {
        void onSuccess(@NonNull Service service);

        void onError(@NonNull Throwable throwable);
    }

    public void lyftInfo(@Nullable final RevealServiceCallbacks callbacks) throws InterruptedException {

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
                EtaEstimateResponse result = response.body();
                for(Eta eta : result.eta_estimates) {
                    Log.d("MyApp", "ETA for " + eta.ride_type + ": " + (eta.eta_seconds/60) + " min");
                    if ((eta.eta_seconds/60) == 1) {
                        svLyft.setServiceTime("Waiting Time:  " + Integer.toString(eta.eta_seconds / 60) + " minute");
                    }
                    else {
                        svLyft.setServiceTime("Waiting Time:  " + Integer.toString(eta.eta_seconds / 60) + " minutes");
                    }
                    if (callbacks != null)
                        callbacks.onSuccess(svLyft);
                }
            }

            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                Log.d("MyApp", t.toString());
                if (callbacks != null)
                    callbacks.onError(t);
            }
        });


        //Get cost, distance, and duration estimates between two locations.

        //Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(41.949740, -87.652110, RideTypeEnum.CLASSIC.toString(), 41.925010, -87.659920);
        Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(origen_lat, origen_long, RideTypeEnum.CLASSIC.toString(), destino_lat, destino_long);
        costEstimateCall.enqueue(new Callback<CostEstimateResponse>() {

            @Override
            public void onResponse(Call<CostEstimateResponse> call, Response<CostEstimateResponse> response) {
                CostEstimateResponse result = response.body();

                for(CostEstimate costEstimate : result.cost_estimates) {
                    Log.d("MyApp", "Min: " + String.valueOf(costEstimate.estimated_cost_cents_min/100) + "$");
                    Log.d("MyApp", "Max: " + String.valueOf(costEstimate.estimated_cost_cents_max/100) + "$");
                    Log.d("MyApp", "Distance: " + String.valueOf(costEstimate.estimated_distance_miles) + " miles");
                    Log.d("MyApp", "Duration: " + String.valueOf(costEstimate.estimated_duration_seconds/60) + " minutes");
                    svLyft.setServicePrice("$"+Integer.toString(costEstimate.estimated_cost_cents_min/100)+" - $"+Integer.toString(costEstimate.estimated_cost_cents_max/100));

                    if (callbacks != null)
                        callbacks.onSuccess(svLyft);
                    //Log.d("LyftInfo function 1", "price: "+svLyft.getServicePrice());
                }
            }

            @Override
            public void onFailure(Call<CostEstimateResponse> call, Throwable t) {
                Log.d("MyApp", t.toString());
                if (callbacks != null)
                    callbacks.onError(t);
            }
        });
        //Log.d("LyftInfo func return", "price: "+svLyft.getServicePrice());
        //return svLyft;
    }

    public interface RevealServiceCallbacksUber {
        void onSuccess(@NonNull String string);

        void onError(@NonNull Throwable throwable);
    }

    public void uberEtaInfo(@Nullable final RevealServiceCallbacksUber callbacks) throws InterruptedException{

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://api.uber.com/v1.2/estimates/time?start_latitude=37.7752315&start_longitude=-122.418075")
                .get()
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer JA.VUNmGAAAAAAAEgASAAAABwAIAAwAAAAAAAAAEgAAAAAAAAG8AAAAFAAAAAAADgAQAAQAAAAIAAwAAAAOAAAAkAAAABwAAAAEAAAAEAAAAPQMWHymBZmztqSqJJZz6WRsAAAA0k-JEnliok6HEjoeFAz8vM8UsZGeC3_KQcxQLo20pMvwL9_NGMNoz6Tb0tVLR1LFnpKJW_9udc3vaEIj3hvpzGNuG0DahI1fH9JKy_AJpixHO0mcztZkVMJyT5qT191ahpriBSkCox3nfTJFDAAAAEv63fw6PHb8e6ZTlSQAAABiMGQ4NTgwMy0zOGEwLTQyYjMtODA2ZS03YTRjZjhlMTk2ZWU")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "44049820-286e-4dac-ae84-d656dc3a6093")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

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
                .url("https://api.uber.com/v1.2/estimates/price?start_latitude=37.7752315&start_longitude=-122.418075&end_latitude=37.7752415&end_longitude=-122.518075")
                .get()
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer JA.VUNmGAAAAAAAEgASAAAABwAIAAwAAAAAAAAAEgAAAAAAAAG8AAAAFAAAAAAADgAQAAQAAAAIAAwAAAAOAAAAkAAAABwAAAAEAAAAEAAAAPQMWHymBZmztqSqJJZz6WRsAAAA0k-JEnliok6HEjoeFAz8vM8UsZGeC3_KQcxQLo20pMvwL9_NGMNoz6Tb0tVLR1LFnpKJW_9udc3vaEIj3hvpzGNuG0DahI1fH9JKy_AJpixHO0mcztZkVMJyT5qT191ahpriBSkCox3nfTJFDAAAAEv63fw6PHb8e6ZTlSQAAABiMGQ4NTgwMy0zOGEwLTQyYjMtODA2ZS03YTRjZjhlMTk2ZWU")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "b6647fde-7ba5-44e8-a1df-88a8ec408f8f")
                .build();

        client1.newCall(request1).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    if (callbacks != null)
                        callbacks.onSuccess(responseBody.string());

                }
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... params) {
            String url;
            String url_bike;

            url = new String(params[0]);
            url_bike = new String(params[1]);
            try {
                walkinginfo = JsonReader.readJsonFromUrl(url);
                bikeinfo = JsonReader.readJsonFromUrl(url_bike);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                GoogleDistanceMatrixApiResponse real1 = mapper.readValue(bikeinfo.toString(), GoogleDistanceMatrixApiResponse.class);
                distance_bike = real1.rows.get(0).elements.get(0).distance.text;
                time_bike = real1.rows.get(0).elements.get(0).duration.text;
            } catch (IOException e) {
                e.printStackTrace();
            }


            /*JSONArray jsonObject1 = null;
            JSONArray jsonObject1b = null;
            try {
                jsonObject1 = (JSONArray) walkinginfo.get("rows");
                jsonObject1b = (JSONArray) bikeinfo.get("rows");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject2 = null;
            JSONObject jsonObject2b = null;
            try {
                jsonObject2 = (JSONObject) jsonObject1.get(0);
                jsonObject2b = (JSONObject) jsonObject1b.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonObject3 = null;
            JSONArray jsonObject3b = null;
            try {
                jsonObject3 = (JSONArray) jsonObject2.get("elements");
                jsonObject3b = (JSONArray) jsonObject2b.get("elements");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject elementObj = null;
            JSONObject elementObjb = null;
            try {
                elementObj = (JSONObject) jsonObject3.get(0);
                elementObjb = (JSONObject) jsonObject3b.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject distanceObj = null;
            JSONObject distanceObjb = null;
            try {
                distanceObj = (JSONObject) elementObj.get("distance");
                distanceObjb = (JSONObject) elementObjb.get("distance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject timeObj = null;
            JSONObject timeObjb = null;
            try {
                timeObj = (JSONObject) elementObj.get("duration");
                timeObjb = (JSONObject) elementObjb.get("duration");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                distance = distanceObj.getString("text");
                distance_bike = distanceObjb.getString("text");
                Log.d("JSON","Distance: "+distance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                time = timeObj.getString("text");
                time_bike = timeObjb.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }

}
