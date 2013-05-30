package org.ktln2.android.callstat;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.*;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import android.database.Cursor;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.provider.CallLog.Calls;

import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;

import com.google.ads.*;


public class MainActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CallStatAdapter mAdapter;
    private ListView mListView;
    private AdView mAdView;
    private View mEmptyView;
    private ProgressBar mSpinner;

    public static StatisticsMap map;

    private final static String mMainHeaderFormatString = "%d calls from %d contacts";
    private final static String mSubHeaderFormatString = "%s (%s in average)";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mListView = (ListView)findViewById(R.id.list);
        mEmptyView = findViewById(android.R.id.empty);
        mSpinner = (ProgressBar)findViewById(R.id.loading_spinner);

        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mAdapter);

        loadData();
    }

    private void loadData() {
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

            case R.id.graph:
                Intent graphIntent = new Intent(this, GraphActivity.class);
                startActivity(graphIntent);
        }
        // FIXME: we don't want to order if R.id.graph was selected
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

    // TODO: create getter/setter for StatisticsMap

    /*
     * When the Cursor is loaded save the value retrieved in an Hashmap
     * with as key the number/contact and as value the duration.
     */
    private StatisticsMap getValuesFromCursor(Cursor cursor) {
        StatisticsMap hm = new StatisticsMap();

        // otherwise CursorIndexOutOfBoundsException: Index -1 requested, with a size of 147
        cursor.moveToFirst();
        while (!cursor.isLast()) {
            long duration = cursor.getLong(
                cursor.getColumnIndexOrThrow(Calls.DURATION)
            );
            String number = cursor.getString(cursor.getColumnIndexOrThrow(Calls.NUMBER));

            cursor.moveToNext();

            // if there is a + as first character then remove it and
            // the following two numbers
            if (number.startsWith("+")) {
                number = number.substring(3);
            }

            hm.put(number, duration, this);
        }

        return hm;
    }

    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        new Thread(new Runnable() {
            public void run() {
                final StatisticsMap hashmap = getValuesFromCursor(cursor);
                MainActivity.map = hashmap;
                mAdapter = new CallStatAdapter(MainActivity.this, hashmap);

                mListView.post(new Runnable() {
                    public void run() {

                        ((TextView)findViewById(R.id.n_calls)).setText(
                            String.format(mMainHeaderFormatString, hashmap.getTotalCalls(), hashmap.getTotalContacts())
                        );
                        ((TextView)findViewById(R.id.n_contacts)).setText(
                        String.format(
                            mSubHeaderFormatString,
                            DateUtils.formatElapsedTimeNG(hashmap.getTotalDuration()),
                            DateUtils.formatElapsedTimeNG(hashmap.getTotalDuration()/hashmap.getTotalCalls()))
                        );
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                            // Animate the loading view to 0% opacity. After the animation ends,
                            // set its visibility to GONE as an optimization step (it won't
                            // participate in layout passes, etc.)
                            mSpinner.animate()
                                .alpha(0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            mSpinner.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            mSpinner.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
