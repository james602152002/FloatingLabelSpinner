package com.james602152002.floatinglabelspinner.popupwindow;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-10.
 */
public class SpinnerPopupWindowTest extends AndroidTestCase {

    private SpinnerPopupWindow popupWindow;

    @Before
    public void setUp() throws Exception {
        popupWindow = new SpinnerPopupWindow(getContext());
    }

    @Test
    public void testSetAdapter() {
        popupWindow.setAdapter(getContext(), null, (short) 0);
    }

    @After
    public void tearDown() throws Exception {
        popupWindow = null;
    }
}