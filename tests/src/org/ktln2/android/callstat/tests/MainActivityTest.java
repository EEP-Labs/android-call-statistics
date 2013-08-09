package org.ktln2.android.callstat.tests;

import android.test.ActivityInstrumentationTestCase2;
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
        super("org.ktln2.android.callstat", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
         super.setUp();

         mActivity = getActivity();
        getInstrumentation().waitForIdleSync();
    }

    public void testWTF() {
        CallLoader contactLoader = mActivity.getLoader();

        try {contactLoader.get();} catch (Exception e) {}
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        assertTrue(((ListView)mActivity.findViewById(R.id.list)).getAdapter() != null);
    }

}
