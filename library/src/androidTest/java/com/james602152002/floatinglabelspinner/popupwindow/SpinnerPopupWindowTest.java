package com.james602152002.floatinglabelspinner.popupwindow;

import android.content.Context;
import android.database.DataSetObserver;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

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

    @Before
    public void setUp() throws Exception {
        final Context context = getContext();
        FloatingLabelSpinner spinner = new FloatingLabelSpinner(context);
        popupWindow = new SpinnerPopupWindow(context);
        HintAdapter hintAdapter = new HintAdapter(context, spinner, new SpinnerAdapter() {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

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
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        popupWindow.setAdapter(getContext(), hintAdapter, (short) 0, spinner.getOnItemSelectedListener());
    }

    @Test
    public void testOnItemClick() throws NoSuchFieldException, IllegalAccessException {
        popupWindow.onItemClick(null, null, 0, 0);
        Field field = SpinnerPopupWindow.class.getDeclaredField("listener");
        field.setAccessible(true);
        field.set(popupWindow, null);
        popupWindow.onItemClick(null, null, 0, 0);
    }

    @After
    public void tearDown() throws Exception {
        popupWindow = null;
    }
}