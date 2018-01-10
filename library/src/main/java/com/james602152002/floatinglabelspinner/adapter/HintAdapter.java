package com.james602152002.floatinglabelspinner.adapter;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

/**
 * Created by shiki60215 on 18-1-10.
 */

public class HintAdapter extends BaseAdapter {

    private final short HINT_TYPE = -1;

    private SpinnerAdapter mSpinnerAdapter;
    private final Context mContext;
    private CharSequence hint;
    private final FloatingLabelSpinner spinner;

    public HintAdapter(Context context, FloatingLabelSpinner spinner, SpinnerAdapter spinnerAdapter) {
        this.mContext = context;
        this.spinner = spinner;
        this.mSpinnerAdapter = spinnerAdapter;
    }

    public void setHint(CharSequence hint) {
        this.hint = hint;
    }

    @Override
    public int getViewTypeCount() {
        //Workaround waiting for a Google correction (https://code.google.com/p/android/issues/detail?id=79011)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return 1;
        }
        int viewTypeCount = mSpinnerAdapter.getViewTypeCount();
        return viewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        position = hint != null ? position - 1 : position;
        return (position == -1) ? HINT_TYPE : mSpinnerAdapter.getItemViewType(position);
    }

    @Override
    public int getCount() {
        int count = mSpinnerAdapter.getCount();
        return hint != null ? count + 1 : count;
    }

    @Override
    public Object getItem(int position) {
        position = hint != null ? position - 1 : position;
        return (position == -1) ? hint : mSpinnerAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        position = hint != null ? position - 1 : position;
        return (position == -1) ? 0 : mSpinnerAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return buildView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return buildView(position, convertView, parent, true);
    }

    private View buildView(int position, View convertView, ViewGroup parent, boolean isDropDownView) {
        if (getItemViewType(position) == HINT_TYPE) {
            return getHintView(parent, isDropDownView);
        }
        //workaround to have multiple types in spinner
        if (convertView != null) {
            convertView = (convertView.getTag() != null && convertView.getTag() instanceof Integer && (Integer) convertView.getTag() != HINT_TYPE) ? convertView : null;
        }
        position = hint != null ? position - 1 : position;
        return isDropDownView ? mSpinnerAdapter.getDropDownView(position, convertView, parent) : mSpinnerAdapter.getView(position, convertView, parent);
    }

    private View getHintView(final ViewGroup parent, final boolean isDropDownView) {
        View convertView = null;
        final View dropDownHintView = spinner.getDropDownHintView();
        final int dropDownHintViewID = spinner.getDropDownHintViewID();
        final short hint_cell_height = spinner.getHint_cell_height();
        final float hint_text_size = spinner.getHint_text_size();
        if (isDropDownView) {
            if (dropDownHintView != null) {
                convertView = dropDownHintView;
                convertView.setTag(HINT_TYPE);
            } else {
                final LayoutInflater inflater = LayoutInflater.from(mContext);
                final int resid = dropDownHintViewID;
                final TextView textView = (TextView) inflater.inflate(resid, parent, false);
                textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, hint_cell_height));
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setText(hint);
                textView.setTextColor(spinner.getHint_text_color());
                if (hint_text_size != -1)
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hint_text_size);
                convertView = textView;
            }
        } else {
            convertView = new View(mContext);
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, hint_cell_height));
        }
        if (convertView.getLayoutParams() == null) {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);
        }
        return convertView;
    }
}
