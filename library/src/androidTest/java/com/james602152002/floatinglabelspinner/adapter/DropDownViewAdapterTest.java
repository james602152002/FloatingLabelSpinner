package com.james602152002.floatinglabelspinner.adapter;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-11.
 */
public class DropDownViewAdapterTest extends AndroidTestCase {

    private DropDownViewAdapter dropDownViewAdapter;
    private HintAdapter hintAdapter;

    @Before
    public void setUp() throws Exception {
        final Context context = getContext();
        FloatingLabelSpinner spinner = new FloatingLabelSpinner(context);
        spinner.setDropDownHintView(new View(context));
        spinner.setHint("hint");
        hintAdapter = new HintAdapter(context, spinner, new BaseAdapter() {
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
        dropDownViewAdapter = new DropDownViewAdapter(hintAdapter);
    }

    @Test
    public void getCount() throws Exception {
        assertEquals(hintAdapter.getCount(), dropDownViewAdapter.getCount());
    }

    @Test
    public void testItem() throws Exception {
        final Object item = dropDownViewAdapter.getItem(0);
        assertEquals(hintAdapter.getItem(0), item);
    }

    @Test
    public void testItemId() throws Exception {
        final long itemID = dropDownViewAdapter.getItemId(0);
        assertEquals(hintAdapter.getItemId(0), itemID);
    }

    @Test
    public void testItemViewType() throws Exception {
        final int itemViewType = dropDownViewAdapter.getItemViewType(0);
        assertEquals(hintAdapter.getItemViewType(0), itemViewType);
    }

    @Test
    public void testGetView() throws Exception {
        final View convertView = dropDownViewAdapter.getView(0, null, null);
        assertNotNull(convertView);
    }


    @After
    public void tearDown() throws Exception {
        dropDownViewAdapter = null;
    }

}