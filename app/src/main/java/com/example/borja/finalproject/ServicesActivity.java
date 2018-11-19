package com.example.borja.finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

public class ServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        String[] spinnerNames;
        String[] emptyName = new String[]{""};;
        String[] spinnerTimes;
        String[] emptyTime = new String[]{""};
        String[] spinnerPrices;
        String[] emptyPrice = new String[]{""};
        int[] spinnerIcons;
        int[] emptyIcon = new int[]{R.drawable.empty};
        Spinner mSpinner;
        boolean isUserInteracting = false;

        mSpinner = (Spinner) findViewById(R.id.spinner);

        spinnerNames = new String[]{"Uber", "Lyft", "Via"};
        spinnerTimes = new String[]{"8 - 10 minutes", "8 - 10 minutes", "8 - 10 minutes"};
        spinnerPrices = new String[]{"$7 - $10",  "$6 - $10,5", "$5 - $9,6"};
        spinnerIcons = new int[]{R.drawable.uber_32
                , R.drawable.lyft_32
                , R.drawable.via_32 };

        CustomAdapter mCustomAdapter = new CustomAdapter(ServicesActivity.this, spinnerNames, spinnerIcons, spinnerTimes, spinnerPrices);
        mSpinner.setAdapter(mCustomAdapter);

        //mSpinner.setAdapter(new CustomAdapter(ServicesActivity.this, emptyName, emptyIcon, emptyTime, emptyPrice));

    }
}
