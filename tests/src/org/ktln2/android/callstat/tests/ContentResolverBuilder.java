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
}

