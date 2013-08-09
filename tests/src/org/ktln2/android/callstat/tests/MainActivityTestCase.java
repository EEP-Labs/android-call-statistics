package org.ktln2.android.callstat.tests;

import org.ktln2.android.callstat.MainActivity;
import android.test.ActivityUnitTestCase;
import android.test.mock.MockContext;
import android.content.Intent;
import org.ktln2.android.callstat.MainActivity.ContactsCallsFragment.CallLoader;

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

        //setActivityContext(new MockContext());
    }

    public void testUAU() {
        Intent i = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(i, null, null);

        mActivity = getActivity();

        getInstrumentation().waitForIdleSync();

        CallLoader contactLoader = mActivity.getLoader();

        android.util.Log.d("TEST", "" + contactLoader);

        try {contactLoader.get();} catch (Exception e) {e.printStackTrace();}
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        assertTrue(mActivity.getFragment().getAdapter() != null);
    }
}