package com.james602152002.floatinglabelspinner;

import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.james602152002.floatinglabelspinner.adapter.HintAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-9.
 */
public class HintAdapterTest extends AndroidTestCase {

    HintAdapter adapter;
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
        spinner.setAdapter(baseAdapter);
        spinner.setHint("hint");
        adapter = (HintAdapter) spinner.getAdapter();
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
        assertNotNull(adapter.getView(0, null, null));
        assertNotNull(adapter.getDropDownView(0, null, null));
    }

    @Test
    public void testDropDownHintView() {
        spinner.setDropDownHintView(new View(getContext()));
        assertNotNull(adapter.getDropDownView(0, spinner.getDropDownHintView(), null));
        adapter.setHint(null);
        View dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        assertNull(dropDownHintView);
        spinner.getDropDownHintView().setTag(-1);
        dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        assertNull(dropDownHintView);
        spinner.getDropDownHintView().setTag(0);
        dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        assertNull(dropDownHintView);
    }

    @Test
    public void testDropDownView() {
        adapter.setHint(null);
        View dropDownView = adapter.getDropDownView(1, null, null);
        assertNull(dropDownView);
        dropDownView = adapter.getDropDownView(1, new View(getContext()), null);
        assertNull(dropDownView);
    }

    @Test
    public void testItemWithoutHint() {
        adapter.setHint(null);
        adapter.getItem(0);
        adapter.getItem(1);
    }

    @After
    public void tearDown() throws Exception {
        adapter = null;
        spinner = null;
    }

}