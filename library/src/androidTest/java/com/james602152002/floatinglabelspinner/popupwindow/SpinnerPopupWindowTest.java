package com.james602152002.floatinglabelspinner.popupwindow;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;
import com.james602152002.floatinglabelspinner.adapter.HintAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by shiki60215 on 18-1-10.
 */
public class SpinnerPopupWindowTest extends AndroidTestCase {

    private SpinnerPopupWindow popupWindow;
    private FloatingLabelSpinner spinner;

    @Before
    public void setUp() throws Exception {
        final Context context = getContext();
        spinner = new FloatingLabelSpinner(context);
        spinner.setDropDownHintView(new View(context));
        spinner.setHint("hint");
        popupWindow = new SpinnerPopupWindow(context);
        spinner.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
        popupWindow.setAdapter(spinner, (HintAdapter) spinner.getAdapter(), (short) 0, spinner.getOnItemSelectedListener());
    }

    @Test
    public void testOnItemClick() throws NoSuchFieldException, IllegalAccessException {
        popupWindow.onItemClick(null, null, 0, 0);
        Field field = SpinnerPopupWindow.class.getDeclaredField("listener");
        field.setAccessible(true);
        field.set(popupWindow, null);
        spinner.setRecursive_mode(true);
        popupWindow.onItemClick(null, null, 0, 0);
    }

    @After
    public void tearDown() throws Exception {
        popupWindow = null;
        spinner = null;
    }
}