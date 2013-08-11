package org.ktln2.android.callstat.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.widget.ListView;
import android.app.Activity;
import android.util.Log;
import org.ktln2.android.callstat.MainActivity;
import org.ktln2.android.callstat.R;
import org.ktln2.android.callstat.MainActivity.ContactsCallsFragment.CallLoader;

import java.lang.Exception;
import java.lang.Thread;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.ktln2.android.callstat.MainActivityTest \
 * org.ktln2.android.callstat.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
         super.setUp();

        mActivity = getActivity();

        RenamingDelegatingContext renaming = new RenamingDelegatingContext(mActivity, "test_");

        getInstrumentation().waitForIdleSync();

        CallLoader contactLoader = mActivity.getLoader();

        // wait for the loader to finish the loading
        try {contactLoader.get();} catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

    public void testWTF() {
        // http://developer.android.com/reference/android/test/RenamingDelegatingContext.html
        assertTrue(mActivity.getFragment().getAdapter() != null);
    }

}
