package org.ktln2.android.callstat;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.provider.CallLog.Calls;


public class MainActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CallStatAdapter mAdapter;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mListView = (ListView)findViewById(R.id.list);

        mListView.setEmptyView(findViewById(android.R.id.empty));

        mListView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ordering_type = 0;
        switch (item.getItemId()) {
            case R.id.ordering_total_duration:
                ordering_type = CallStatAdapter.CALL_STAT_ADAPTER_ORDERING_TOTAL_DURATION;
                break;
            case R.id.ordering_total_calls:
                ordering_type = CallStatAdapter.CALL_STAT_ADAPTER_ORDERING_TOTAL_CALLS;
                break;
            case R.id.ordering_avg_duration:
                ordering_type = CallStatAdapter.CALL_STAT_ADAPTER_ORDERING_AVG_DURATION;
                break;
            case R.id.ordering_max_duration:
                ordering_type = CallStatAdapter.CALL_STAT_ADAPTER_ORDERING_MAX_DURATION;
                break;
            case R.id.ordering_min_duration:
                ordering_type = CallStatAdapter.CALL_STAT_ADAPTER_ORDERING_MIN_DURATION;
                break;
        }
        mAdapter.order(ordering_type);

        return super.onOptionsItemSelected(item);
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

            hm.put(number, duration, this);
        }

        return hm;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        LayoutInflater inflater =
            (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        mListView.addHeaderView(inflater.inflate(R.layout.call_stat_surface, null));

        StatSurfaceView ssv = (StatSurfaceView)findViewById(R.id.surface);

        StatisticsMap hashmap = getValues(cursor);

        ((TextView)findViewById(R.id.n_calls)).setText(hashmap.getTotalCalls() + " calls");
        ((TextView)findViewById(R.id.n_contacts)).setText(hashmap.getTotalContacts() + " contacts");
        ((TextView)findViewById(R.id.total_duration)).setText(hashmap.getTotalDuration() + " seconds");
        ((TextView)findViewById(R.id.avg_duration)).setText(hashmap.getTotalDuration()/hashmap.getTotalCalls() + " seconds in average");
        mAdapter = new CallStatAdapter(this, hashmap);
        ((ListView)findViewById(R.id.list)).setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
