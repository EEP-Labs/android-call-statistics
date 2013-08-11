package org.ktln2.android.callstat.tests;

import android.test.mock.MockContentResolver;
import android.test.mock.MockContentProvider;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.database.Cursor;
import android.database.MatrixCursor;


class ContentResolverBuilder {
    public static MockContentResolver buildWithEmptyProvider(Context context, String authority) {
        // https://gist.github.com/avh4/1450898
        // Instantiate our content provider 
        MockContentProvider mProvider = new MockContentProvider(context) {
            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                return new MatrixCursor(new String[]{""});
            }
        };

        // Create a mock ContentResolver that will give access to our content
        // provider and nothing else
        final MockContentResolver mResolver = new MockContentResolver();
        mResolver.addProvider(authority, mProvider);

        return mResolver;
    }

    public static MockContentResolver buildWithProvider(Context context, String authority, final String[] columns) {
        // https://gist.github.com/avh4/1450898
        // Instantiate our content provider 
        MockContentProvider mProvider = new MockContentProvider(context) {
            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                MatrixCursor fakeCursor = new MatrixCursor(columns);

                fakeCursor.addRow(new Object[]{
                    1,
                    "666",
                    1000
                });

                return fakeCursor;
            }
        };

        // Create a mock ContentResolver that will give access to our content
        // provider and nothing else
        final MockContentResolver mResolver = new MockContentResolver();
        mResolver.addProvider(authority, mProvider);

        return mResolver;
    }
}

