package org.ktln2.android.callstat.tests;

import org.ktln2.android.callstat.MainActivity;
import org.ktln2.android.callstat.MainActivity.ContactsCallsFragment.CallLoader;
import org.ktln2.android.callstat.R;

import android.test.ActivityUnitTestCase;
import android.test.mock.MockContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContentProvider;

import android.content.Intent;
import android.content.ContextWrapper;
import android.content.ContentResolver;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.CallLog;

import java.lang.Exception;
import java.lang.Override;


public class MainActivityTestCase extends ActivityUnitTestCase<MainActivity> {
    private MainActivity mActivity;

    public MainActivityTestCase() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // https://gist.github.com/avh4/1450898
        // Instantiate our content provider 
        MockContentProvider mProvider = new MockContentProvider(getInstrumentation().getTargetContext()) {
            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

                return new MatrixCursor(new String[]{""});
            }
        };

        // Create a mock ContentResolver that will give access to our content
        // provider and nothing else
        final MockContentResolver mResolver = new MockContentResolver();
        mResolver.addProvider(CallLog.AUTHORITY, mProvider);


        // http://paulbutcher.com/2011/03/12/mock-objects-on-android-with-borachio-part-2/
        ContextWrapper c = new ContextWrapper(getInstrumentation().getTargetContext()) {
            @Override
            public ContentResolver getContentResolver() {
                return mResolver;
            }
        };

        setActivityContext(c);
    }

    public void testEmpty() {
        Intent i = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(i, null, null);

        mActivity = getActivity();
        getInstrumentation().callActivityOnStart(mActivity);

        CallLoader contactLoader = mActivity.getLoader();

        assertNotNull(mActivity.findViewById(R.id.main_container));
        assertNotNull(mActivity.findViewById(R.id.list));
        assertNotNull(contactLoader);

        try {contactLoader.get();} catch (Exception e) {e.printStackTrace();}
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        assertTrue(mActivity.getFragment().getAdapter() != null);
    }
}
