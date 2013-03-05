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

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        StatSurfaceView ssv = (StatSurfaceView)findViewById(R.id.surface);
        ssv.drawPie();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
