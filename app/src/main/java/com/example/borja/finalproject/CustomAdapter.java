package com.example.borja.finalproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

    int[] spinnerIcons;
    String[] spinnerNames;
    String[] spinnerTimes;
    String[] spinnerPrices;
    Context mContext;

    public CustomAdapter(@NonNull Context context, String[] serviceNames, int[] serviceIcons, String[] serviceTimes, String[] servicePrices) {
        super(context, R.layout.custom_spinner_layout);
        this.spinnerIcons = serviceIcons;
        this.spinnerNames = serviceNames;
        this.spinnerTimes = serviceTimes;
        this.spinnerPrices = servicePrices;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return spinnerNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_layout, parent, false);
            mViewHolder.mIcon = (ImageView) convertView.findViewById(R.id.serviceIcon);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.serviceName);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.waitingTime);
            mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.priceEstimate);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mIcon.setImageResource(spinnerIcons[position]);
        mViewHolder.mName.setText(spinnerNames[position]);
        mViewHolder.mTime.setText(spinnerTimes[position]);
        mViewHolder.mPrice.setText(spinnerPrices[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    private static class ViewHolder {
        ImageView mIcon;
        TextView mName;
        TextView mTime;
        TextView mPrice;
    }
}
