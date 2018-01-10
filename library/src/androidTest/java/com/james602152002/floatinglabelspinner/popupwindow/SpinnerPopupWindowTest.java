package com.james602152002.floatinglabelspinner.popupwindow;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Created by shiki60215 on 18-1-10.
 */
public class SpinnerPopupWindowTest extends AndroidTestCase {

    private SpinnerPopupWindow popupWindow;

    @Before
    public void setUp() throws Exception {
        popupWindow = new SpinnerPopupWindow(getContext());
    }

    @After
    public void tearDown() throws Exception {
        popupWindow = null;
    }
}