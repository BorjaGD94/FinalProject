package com.example.borja.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicesActivity extends Activity {
    @SuppressLint("StaticFieldLeak")
    private static ExpandableListView expandableListView;
    @SuppressLint("StaticFieldLeak")
    private static ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        expandableListView = (ExpandableListView) findViewById(R.id.elistview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();
        setListener();

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

        // Adding child data
        String serviceName = "Uber";
        int serviceLogo = R.drawable.uber_32;
        String serviceTime = "8 - 10 minutes";
        String servicePrice = "$8 - $11";
        Service sv = new Service(serviceName,serviceLogo,serviceTime,servicePrice);

        String sN1 = "Lyft";
        int sL1 = R.drawable.lyft_32;
        String sT1 = "8 - 10 minutes";
        String sP1 = "$8 - $11";
        Service sv1 = new Service(sN1,sL1,sT1,sP1);

        String sN2 = "Lyft";
        int sL2 = R.drawable.via_32;
        String sT2 = "8 - 10 minutes";
        String sP2 = "$8 - $11";
        Service sv2 = new Service(sN2,sL2,sT2,sP2);

        child1.add(sv);
        child1.add(sv1);
        child1.add(sv2);

        // Adding child data
        for (int i = 1; i < 5; i++) {
            //child2.add("Group 2  - " + " : Child" + i);

        }
        // Adding child data
        for (int i = 1; i < 6; i++) {
            //child3.add("Group 3  - " + " : Child" + i);

        }
        // Adding child data
        for (int i = 1; i < 7; i++) {
            //child4.add("Group 4  - " + " : Child" + i);

        }

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
        expandableListView
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

                });

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
}
