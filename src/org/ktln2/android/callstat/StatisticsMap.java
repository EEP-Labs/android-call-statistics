package org.ktln2.android.callstat;

import java.util.HashMap;


/*
 * Container for statistical data management.
 */
class StatisticsMap extends HashMap<String, Long> {
    private float mMin, mMax, mTotal;
    /*
     * Return the data divided using some bins.
     *
     * http://stackoverflow.com/questions/10786465/how-to-generate-bins-for-histogram-using-apache-math-3-0-in-java
     */
    public int[] calcHistogram(int numBins) {
        final int[] result = new int[numBins];
        final double binSize = (mMax - mMin)/numBins;

        for (double d : this.values()) {
            int bin = (int) ((d - mMin) / binSize);
            if (bin < 0) { /* this data is smaller than min */ }
            else if (bin >= numBins) { /* this data point is bigger than max */ }
            else {
                result[bin] += 1;
            }
        }

        return result;
    }

    public Long put(String key, Long value) {
        return super.put(key, value);
    }
}
