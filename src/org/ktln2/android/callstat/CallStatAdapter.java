package org.ktln2.android.callstat;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


/*
 * This adapter bind the data to the list view.
 *
 * There is only a problem: from the same data you can calculate several
 * different list ordering and visualization.
 *
 * The common point is the layout of the cell: a contact information
 * and a value side by side.
 */
public class CallStatAdapter extends ArrayAdapter<CallStat> {
    // the only role of this class is to maintain
    // the expensive information about list item
    // without quering everytime the layout
    private class Holder {
        public TextView numberView;
        public TextView contactView;
        public TextView contactTotalCallsView;
        public TextView contactTotalDurationView;
        public TextView contactAvgDurationView;
        public TextView contactMaxDurationView;
        public TextView contactMinDurationView;
        public ImageView photoView;
    }

    Context mContext;
    StatisticsMap mMap;

    public CallStatAdapter(Context context, StatisticsMap data) {
        super(
            context,
            R.layout.list_duration_item,
            data.getCallStatOrderedByMaxDuration()
        );

        mContext = context;
        mMap = data;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            LayoutInflater inflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_duration_item, null);

            holder = new Holder();
            holder.numberView  = (TextView)view.findViewById(R.id.number);
            holder.contactView = (TextView)view.findViewById(R.id.contact);
            holder.contactTotalCallsView = (TextView)view.findViewById(R.id.contact_total_calls);
            holder.contactTotalDurationView = (TextView)view.findViewById(R.id.contact_total_duration);
            holder.contactAvgDurationView = (TextView)view.findViewById(R.id.contact_avg_duration);
            holder.contactMaxDurationView = (TextView)view.findViewById(R.id.contact_max_duration);
            holder.contactMinDurationView = (TextView)view.findViewById(R.id.contact_min_duration);
            holder.photoView   = (ImageView)view.findViewById(R.id.photo);

            view.setTag(holder);

        } else {
            holder = (Holder)view.getTag();
        }

        CallStat entry = getItem(position);

        String phonenumber = entry.getKey();
        float percent = new Float(entry.getTotalDuration())*100/new Float(mMap.getTotalDuration());

        holder.numberView.setText(phonenumber);

        // show contact name
        holder.contactView.setText(entry.getContactName());

        // fill various statistical data
        holder.contactTotalCallsView.setText(entry.getTotalCalls()+ " calls");
        holder.contactTotalDurationView.setText(entry.getTotalDuration() + " seconds");
        holder.contactAvgDurationView.setText("Average call: " + entry.getAverageDuration());
        holder.contactMaxDurationView.setText("Max call: " + entry.getMaxDuration());
        holder.contactMinDurationView.setText("Min call: " + entry.getMinDuration());


        // show contact photo
        holder.photoView.setImageBitmap(entry.getContactPhoto());

        return view;
    }
}
