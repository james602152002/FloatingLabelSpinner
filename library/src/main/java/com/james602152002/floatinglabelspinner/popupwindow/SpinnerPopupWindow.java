package com.james602152002.floatinglabelspinner.popupwindow;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.james602152002.floatinglabelspinner.R;
import com.james602152002.floatinglabelspinner.adapter.DropDownViewAdapter;
import com.james602152002.floatinglabelspinner.adapter.HintAdapter;

/**
 * Created by shiki60215 on 18-1-10.
 */

public class SpinnerPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private DropDownViewAdapter dropDownViewAdapter;
    private AdapterView.OnItemSelectedListener listener;

    public SpinnerPopupWindow(Context context) {
        super(context);
    }

    public void setAdapter(Context context, HintAdapter hintAdapter, short margin, AdapterView.OnItemSelectedListener listener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.floating_label_spinner_popup_window, null, false);
        ListView listView = contentView.findViewById(R.id.list_view);
        CardView cardView = contentView.findViewById(R.id.card_view);
        dropDownViewAdapter = new DropDownViewAdapter(hintAdapter);
        listView.setAdapter(dropDownViewAdapter);

        ((FrameLayout.LayoutParams) cardView.getLayoutParams()).setMargins(margin, margin, margin, margin);
        setContentView(contentView);
        this.listener = listener;
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            listener.onItemSelected(parent, view, position, id);
        }
        dismiss();
    }


}
