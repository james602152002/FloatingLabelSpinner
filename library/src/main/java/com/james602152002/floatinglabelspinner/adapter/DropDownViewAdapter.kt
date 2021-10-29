package com.james602152002.floatinglabelspinner.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by shiki60215 on 18-1-11.
 */

public class DropDownViewAdapter extends BaseAdapter {

    private HintAdapter hintAdapter;

    public DropDownViewAdapter(HintAdapter hintAdapter) {
        this.hintAdapter = hintAdapter;
    }

    @Override
    public int getCount() {
        return hintAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return hintAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return hintAdapter.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return hintAdapter.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return hintAdapter.getDropDownView(position, convertView, parent);
    }
}
