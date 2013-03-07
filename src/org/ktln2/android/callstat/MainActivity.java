package org.ktln2.android.callstat;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.provider.CallLog.Calls;
// FIXME: use support library


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(
            this, // context
            android.R.layout.simple_list_item_1, // layout
            null, // cursor
            new String[] {
                Calls.NUMBER
            }, // from
            new int[] {
                android.R.id.text1
            }
        );
        setContentView(R.layout.main);

        getLoaderManager().initLoader(0, null, this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(
            this,              // context
            Calls.CONTENT_URI, // Uri for the Call log
            null,              // projection
            null,              // selection
            null,              // selectionArgs
            null               // sortOrder
        );

        return cl;
    }

    /*
     * When the Cursor is loaded save the value retrieved in an Hashmap
     * with as key the number/contact and as value the duration.
     */
    private StatisticsMap getValues(Cursor cursor) {
        StatisticsMap hm = new StatisticsMap();

        // otherwise CursorIndexOutOfBoundsException: Index -1 requested, with a size of 147
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(Calls.DURATION));
            String number = cursor.getString(cursor.getColumnIndexOrThrow(Calls.NUMBER));

            cursor.moveToNext();

            hm.put(number, duration);
        }

        return hm;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        StatSurfaceView ssv = (StatSurfaceView)findViewById(R.id.surface);

        StatisticsMap hashmap = getValues(cursor);

    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
