package com.example.borja.finalproject;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

//For expandable list view use BaseExpandableListAdapter
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> header; // header titles
    // Child data in format of header title, child title
    private HashMap<String, List<Service>> child;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Service>> listChildData) {
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(
                childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // Getting child text
        //final String childText = (String) getChild(groupPosition, childPosition);
        //Service service = new Service("",0,"","");
        //String childText = service.getServiceName();
        final Service childText = (Service) getChild(groupPosition, childPosition);

        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childs, parent, false);
        }
        TextView child_name = (TextView) convertView.findViewById(R.id.serviceName);
        TextView child_price = (TextView) convertView.findViewById(R.id.priceEstimate);
        TextView child_time = (TextView) convertView.findViewById(R.id.waitingTime);
        ImageView child_logo= (ImageView) convertView.findViewById(R.id.serviceIcon);
        child_name.setText(childText.serviceName);
        child_price.setText(childText.servicePrice);
        child_time.setText(childText.serviceTime);
        child_logo.setImageResource(childText.serviceLogo);

        //child_text.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        // Get header size
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        // Getting header title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);
        if (groupPosition == 0) {
            header_text.setBackground(ContextCompat.getDrawable(_context, R.drawable.spinner_layout));
        }
        if (groupPosition == 1) {
            header_text.setBackground(ContextCompat.getDrawable(_context, R.drawable.spinner_layout1));
        }
        if (groupPosition == 2) {
            header_text.setBackground(ContextCompat.getDrawable(_context, R.drawable.spinner_layout2));
        }
        if (groupPosition == 3) {
            header_text.setBackground(ContextCompat.getDrawable(_context, R.drawable.spinner_layout3));
        }
        header_text.setText(headerTitle);

        // If group is expanded then change the text into bold and change the
        // icon

        Drawable up = ContextCompat.getDrawable(_context, R.drawable.up_arrow_32);
        Drawable down = ContextCompat.getDrawable(_context, R.drawable.down_arrow);

        up.setVisible(false, false);
        if (isExpanded) {
            header_text.setTypeface(null, Typeface.BOLD);
            //header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
            up.setVisible(true, false);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon

            header_text.setTypeface(null, Typeface.NORMAL);
            //header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.down_arrow, 0);
            down.setVisible(true, true);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

/*import android.content.Context;
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

    private static class ViewHolder {
        ImageView mIcon;
        TextView mName;
        TextView mTime;
        TextView mPrice;
    }

}*/
