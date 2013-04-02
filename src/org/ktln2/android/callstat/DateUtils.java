package org.ktln2.android.callstat;

import java.util.ArrayList;
import android.text.TextUtils;


/*
 * Aim of this class is to format the text involving time duration.
 */
public class DateUtils extends android.text.format.DateUtils {
    static private long[] getSplittedDuration(long duration) {
        // let's calculate the various values
        long weeks   = duration/60/60/24/7;
        long days    = (duration - (weeks*7*24*60*60))/60/60/24;
        long hours   = (duration -    (days*24*60*60) - (weeks*7*24*60*60))/60/60;
        long minutes = (duration -      (hours*60*60) - (days*24*60*60) - (weeks*7*24*60*60))/60;
        long seconds = (duration -       (minutes*60) - (hours*60*60) - (days*24*60*60) - (weeks*7*24*60*60));

        return new long[] {
            weeks,
            days,
            hours,
            minutes,
            seconds
        };
    }

    static public String formatElapsedTimeNG(long duration) {
        long[] smart_duration = getSplittedDuration(duration);

        String[] f = new String[] {
            "w",
            "d",
            "h",
            "m",
            "s"
        };

        ArrayList<String> al = new ArrayList<String>();

        for (int cycle = 0 ; cycle < smart_duration.length ; cycle++) {
            if (smart_duration[cycle] > 0) {
                al.add(String.format("%d%s", smart_duration[cycle], f[cycle]));
            }
        }

        return TextUtils.join("", al);
    }
}
