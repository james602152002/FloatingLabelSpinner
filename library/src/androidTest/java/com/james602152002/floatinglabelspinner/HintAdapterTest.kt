package com.james602152002.floatinglabelspinner;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.james602152002.floatinglabelspinner.adapter.HintAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-9.
 */
public class HintAdapterTest extends AndroidJUnitRunner {

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

        spinner = new FloatingLabelSpinner(InstrumentationRegistry.getInstrumentation().getTargetContext());
        spinner.setAdapter(baseAdapter);
        spinner.setHint("hint");
        adapter = (HintAdapter) spinner.getAdapter();
    }

    @Test
    public void testGetCount() {
        Assert.assertEquals(1, adapter.getCount());
    }

    @Test
    public void testAdapter() {
        adapter.getCount();
        adapter.getItem(0);
        adapter.getItemViewType(0);
        adapter.getItemId(0);
        Assert.assertNotNull(adapter.getView(0, null, null));
        Assert.assertNotNull(adapter.getDropDownView(0, null, null));
    }

    @Test
    public void testDropDownHintView() {
        spinner.setDropDownHintView(new View(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        Assert.assertNotNull(adapter.getDropDownView(0, spinner.getDropDownHintView(), null));
        adapter.setHint(null);
        View dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        Assert.assertNull(dropDownHintView);
        spinner.getDropDownHintView().setTag(-1);
        dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        Assert.assertNull(dropDownHintView);
        spinner.getDropDownHintView().setTag(0);
        dropDownHintView = adapter.getDropDownView(0, spinner.getDropDownHintView(), null);
        Assert.assertNull(dropDownHintView);
    }

    @Test
    public void testDropDownView() {
        adapter.setHint(null);
        View dropDownView = adapter.getDropDownView(1, null, null);
        Assert.assertNull(dropDownView);
        dropDownView = adapter.getDropDownView(1, new View(InstrumentationRegistry.getInstrumentation().getTargetContext()), null);
        Assert.assertNull(dropDownView);
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