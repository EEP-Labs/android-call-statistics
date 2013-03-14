package org.ktln2.android.callstat;

import java.util.TreeSet;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.database.Cursor;
import android.content.Context;


/*
 * This class contains all the statistical data useful to
 * show information about calls.
 *
 * The key argument passed to the constructor is the number called or
 * the number of the callee. Internally this class resolves the contact
 * associated with that number.
 *
 */
public class CallStat {
    private TreeSet<Long> mDurations;
    private long mMax, mMin, mTotal;
    private String mKey;
    private String mContactName;

    public CallStat(String key, Context context) {
        mKey = key;
        mDurations = new TreeSet<Long>();

        /*
         * I don't like very much that we need to pass a Context object
         * around in this class, seems an extraneous object.
         */
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(key));
        Cursor cursor = context.getContentResolver().query(
            uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null
        );
        cursor.moveToFirst();

        mContactName = cursor.getCount() > 0 ?
                cursor.getString(cursor.getColumnIndexOrThrow(PhoneLookup.DISPLAY_NAME)) : "";

        cursor.close();
    }

    public void add(long value) {
        mMin = value < mMin ? value : mMin;
        mMax = value > mMax ? value : mMax;
        mTotal += value;

        mDurations.add(value);
    }

    public String getKey() {
        return mKey;
    }

    public String getContactName() {
        return mContactName;
    }

    public long getMinDuration() {
        return mMin;
    }

    public long getMaxDuration() {
        return mMax;
    }

    public long getTotalDuration() {
        return mTotal;
    }
}
