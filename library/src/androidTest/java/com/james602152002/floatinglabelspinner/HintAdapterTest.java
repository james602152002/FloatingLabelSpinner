package com.james602152002.floatinglabelspinner;

import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-9.
 */
public class HintAdapterTest extends AndroidTestCase {

    FloatingLabelSpinner.HintAdapter adapter;
    FloatingLabelSpinner spinner;

    @Before
    public void setUp() throws Exception {
        BaseAdapter baseAdapter = new BaseAdapter() {
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
        };

        spinner = new FloatingLabelSpinner(getContext());
        spinner.setHint("hint");
        adapter = spinner.new HintAdapter(getContext(), baseAdapter);
    }

    @Test
    public void testGetCount() {
        assertEquals(1, adapter.getCount());
    }

    @Test
    public void testAdapter() {
        adapter.getCount();
        adapter.getItem(0);
        adapter.getItemViewType(0);
        adapter.getItemId(0);
        assertNotNull("not null");
        assertNotNull(adapter.getView(0, null, null));
        assertNotNull(adapter.getDropDownView(0, null, null));
    }

    @Test
    public void testDropDownHintView() {
        spinner.setDropDownHintView(new View(getContext()));
        assertNotNull(adapter.getDropDownView(0, spinner.getDropDownHintView(), null));
    }

    @After
    public void tearDown() throws Exception {
        adapter = null;
        spinner = null;
    }

}