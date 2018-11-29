package com.example.borja.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.CostEstimate;
import com.lyft.networking.apiObjects.CostEstimateResponse;
import com.lyft.networking.apiObjects.Eta;
import com.lyft.networking.apiObjects.EtaEstimateResponse;
import com.lyft.networking.apis.LyftPublicApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesActivity extends Activity {
    @SuppressLint("StaticFieldLeak")
    private static ExpandableListView expandableListView;
    @SuppressLint("StaticFieldLeak")
    private static ExpandableListAdapter adapter;
    private String origen_lat;
    private String origen_long;
    private double destino_lat;
    private double destino_long;

    Service svLyft = new Service();


    JSONObject walkinginfo;
    String time;
    String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        expandableListView = (ExpandableListView) findViewById(R.id.elistview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);
        //setItems();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            origen_lat = extras.getString("origen_lat");
            origen_long = extras.getString("origen_long");
            destino_lat = extras.getDouble("destino_lat");
            destino_long = extras.getDouble("destino_lon");
        }

        //DownloadApiInfo api = new DownloadApiInfo();
        //api.execute();
        try {
            lyftInfo(new RevealServiceCallbacks() {
                @Override
                public void onSuccess(@NonNull Service service) {
                    setItems(service);
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


    /*public class DownloadApiInfo extends AsyncTask<Void,Void,Service> {


        protected void onPreExecute(){
            pr = (ProgressBar)findViewById(R.id.progressBar3);
            //pr.isShown();
        }

        //@Override
        protected Service doInBackground(Void... voids) {
            svLyft = lyftInfo();
            Log.d("lyftInfo background", "Price: " + svLyft.getServicePrice());
            return svLyft;
        }

        protected void onPostExecute(Service svx){
            Log.d("lyftInfo PostExecute", "Price: " + svx.getServicePrice());
            setItems(svx);
            setListener();
            pr.setVisibility(View.INVISIBLE);
        }
    }*/

    // Setting headers and childs to expandable listview
    void setItems(Service svLyft) {

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

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Adding child data
        String serviceName = "Uber";
        int serviceLogo = R.drawable.uber_32;
        String serviceTime = "Waiting time: 8 - 10 minutes";
        String servicePrice = "$8 - $11";
        Service sv = new Service(serviceName,serviceLogo,serviceTime,servicePrice);

        String sN1 = "Lyft";
        int sL1 = R.drawable.lyft_32;
        String sT1 = svLyft.getServiceTime();
        String sP1 = svLyft.getServicePrice();
        Service sv1 = new Service(sN1,sL1,sT1,sP1);

        String sN2 = "Via";
        int sL2 = R.drawable.via_32;
        String sT2 = "Waiting time: 8 - 10 minutes";
        String sP2 = "$8 - $11";
        Service sv2 = new Service(sN2,sL2,sT2,sP2);

        child1.add(sv);
        child1.add(sv1);
        child1.add(sv2);

        String sN3 = "Car2Go";
        int sL3 = R.drawable.car2go;
        String sT3 = "Walking time: 4 minutes";
        String sP3 = "$8 - $11";
        Service sv3 = new Service(sN3,sL3,sT3,sP3);

        String sN4 = "ZityCar";
        int sL4 = R.drawable.zitycar;
        String sT4 = "Walking time: 4 minutes";
        String sP4 = "$8 - $11";
        Service sv4 = new Service(sN4,sL4,sT4,sP4);

        // Adding child data
        child2.add(sv3);
        child2.add(sv4);

        // Adding child data
        for (int i = 1; i < 6; i++) {
            //child3.add("Group 3  - " + " : Child" + i);

        }
        // Adding child data
        String sN5 = "Walking";
        int sL5 = R.drawable.walking_32;
        String sT5 = distance;
        String sP5 = "Free";
        Service sv5 = new Service(sN5, sL5, sT5, sP5);

        child4.add(sv5);

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

                Toast.makeText(ServicesActivity.this,
                        "You clicked : " + adapter.getGroup(group_pos),
                        Toast.LENGTH_SHORT).show();
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
                Toast.makeText(
                        ServicesActivity.this,
                        "You clicked : " + adapter.getChild(groupPos, childPos),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public interface RevealServiceCallbacks {
        void onSuccess(@NonNull Service service);

        void onError(@NonNull Throwable throwable);
    }

    public void lyftInfo(@Nullable final RevealServiceCallbacks callbacks) throws InterruptedException {

        ApiConfig apiConfig = new ApiConfig.Builder().setClientId("TqXRrq9FM124").setClientToken("/oU5+AqwehTKXFCVQT8D0ZAXwAOVfNTAu+dAxLuPUSnqGN/0JaNI1VX0TnWIcrj+HSawTSgbqSwoMjkkBzzu6sG9M6VFoNtLNB90MuhcWrqKPTjhNAZejls=").build();

        LyftPublicApi lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();

        //Get driver estimated time of arrival for a location.

        Call<EtaEstimateResponse> etaCall = lyftPublicApi.getEtas(41.949740, -87.652110, "lyft");

        etaCall.enqueue(new Callback<EtaEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                EtaEstimateResponse result = response.body();
                for(Eta eta : result.eta_estimates) {
                    Log.d("MyApp", "ETA for " + eta.ride_type + ": " + (eta.eta_seconds/60) + " min");
                    if ((eta.eta_seconds/60) == 1) {
                        svLyft.setServiceTime("Waiting Time: " + Integer.toString(eta.eta_seconds / 60) + " minute");
                    }
                    else {
                        svLyft.setServiceTime("Waiting Time: " + Integer.toString(eta.eta_seconds / 60) + " minutes");
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

        Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(41.949740, -87.652110, RideTypeEnum.CLASSIC.toString(), 41.925010, -87.659920);
        //Call<CostEstimateResponse> costEstimateCall = lyftPublicApi.getCosts(Double.parseDouble(origen_lat), Double.parseDouble(origen_long), RideTypeEnum.CLASSIC.toString(), destino_lat, destino_long);
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

    public class BackgroundTask extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... params) {
            String url;

            url = new String(params[0]);
            try {
                walkinginfo = JsonReader.readJsonFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(Void result) {
            final ObjectMapper mapper = new ObjectMapper();

            String des = walkinginfo.toString();
            Log.d("String:", des);


/* forma bonita
                Gson gson= null;
                GoogleDistanceMatrixApiResponse response = mapper.readValue(des, GoogleDistanceMatrixApiResponse.class);
                GoogleDistanceMatrixApiResponse response2 = gson.fromJson(walking, GoogleDistanceMatrixApiResponse.class);
                distance = response.getRows().get(0).getElements().get(0).getDistance().getText();
                distance = response2.getRows().get(0).getElements().get(0).getDistance().getText();

*/

            JSONArray jsonObject1 = null;
            try {
                jsonObject1 = (JSONArray) walkinginfo.get("rows");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject2 = null;
            try {
                jsonObject2 = (JSONObject) jsonObject1.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonObject3 = null;
            try {
                jsonObject3 = (JSONArray) jsonObject2.get("elements");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject elementObj = null;
            try {
                elementObj = (JSONObject) jsonObject3.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject distanceObj = null;
            try {
                distanceObj = (JSONObject) elementObj.get("distance");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject timeObj = null;
            try {
                timeObj = (JSONObject) elementObj.get("duration");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                distance = distanceObj.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                time = timeObj.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
