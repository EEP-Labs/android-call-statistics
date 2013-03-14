package org.ktln2.android.callstat;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
// factorize code about PhoneLookup in the data model
// don't seem strange these imports with respect the above ones?
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.database.Cursor;


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
        public TextView percentView;
        public ProgressBar pbarView;
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
            holder.percentView = (TextView)view.findViewById(R.id.percent);
            holder.pbarView    = (ProgressBar)view.findViewById(R.id.duration);

            view.setTag(holder);

        } else {
            holder = (Holder)view.getTag();
        }

        CallStat entry = getItem(position);

        String phonenumber = entry.getKey();
        float percent = new Float(entry.getTotalDuration())*100/new Float(mMap.getTotalDuration());

        holder.numberView.setText(phonenumber);
        ProgressBar pb = holder.pbarView;

        // show contact name
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
        Cursor cursor = mContext.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        cursor.moveToFirst();
        holder.contactView.setText(
            cursor.getCount() > 0 ?
                cursor.getString(cursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME)) : ""
        );

        holder.percentView.setText(percent + "%");

        pb.setProgress((int)percent);

        return view;
    }
}
