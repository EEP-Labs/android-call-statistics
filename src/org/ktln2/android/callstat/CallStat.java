package org.ktln2.android.callstat;

import java.util.TreeSet;


/*
 * This class contains all the statistical data useful to
 * show information about calls.
 */
public class CallStat {
    private TreeSet<Long> mDurations;
    private long mMax, mMin, mTotal;
    private String mKey;

    public CallStat(String key) {
        mKey = key;
        mDurations = new TreeSet<Long>();
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

    public long getMinDuration() {
        return mMin;
    }

    public long getMaxDuration() {
        return mMax;
    }

    public long getTotal() {
        return mTotal;
    }
}
