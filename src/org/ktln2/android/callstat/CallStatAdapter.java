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
        if (view == null) {
            LayoutInflater inflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_duration_item, null);
        }


        CallStat entry = getItem(position);

        String phonenumber = entry.getKey();
        float percent = new Float(entry.getMaxDuration())*100/new Float(mMap.getTotalDuration());

        ((TextView)view.findViewById(R.id.number)).setText(phonenumber);
        ProgressBar pb = ((ProgressBar)view.findViewById(R.id.duration));

        // show contact name
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
        Cursor cursor = mContext.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            ((TextView)view.findViewById(R.id.contact)).setText(
                cursor.getString(cursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME))
            );
        }

        ((TextView)view.findViewById(R.id.percent)).setText(percent + "%");

        pb.setProgress((int)percent);

        return view;
    }
}
