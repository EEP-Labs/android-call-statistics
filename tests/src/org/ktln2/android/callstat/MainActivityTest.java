package org.ktln2.android.callstat;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;


/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.ktln2.android.callstat.MainActivityTest \
 * org.ktln2.android.callstat.tests/android.test.InstrumentationTestRunner
 *
 * <b>Note:</b> this launches the application and wait all to be loaded.
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
    }

    public void testWTF() {
        assertTrue(((ListView)mActivity.findViewById(R.id.list)).getAdapter() != null);
    }

}
