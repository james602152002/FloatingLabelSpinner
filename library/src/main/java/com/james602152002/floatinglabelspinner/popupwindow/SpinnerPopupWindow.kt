package com.james602152002.floatinglabelspinner.popupwindow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner
import com.james602152002.floatinglabelspinner.R
import com.james602152002.floatinglabelspinner.adapter.DropDownViewAdapter
import com.james602152002.floatinglabelspinner.adapter.HintAdapter

/**
 * Created by shiki60215 on 18-1-10.
 */
class SpinnerPopupWindow(context: Context?) : PopupWindow(context), OnItemClickListener {

    var hintAdapter: HintAdapter? = null

    private var dropDownViewAdapter: DropDownViewAdapter? = null
    private var listener: OnItemSelectedListener? = null
    private var spinner: FloatingLabelSpinner? = null

    @SuppressLint("InflateParams")
    fun setAdapter(
        spinner: FloatingLabelSpinner,
        hintAdapter: HintAdapter?,
        margin: Int,
        listener: OnItemSelectedListener?
    ) {
        this.spinner = spinner
        val contentView = LayoutInflater.from(spinner.context)
            .inflate(R.layout.floating_label_spinner_popup_window, null, false)
        val listView = contentView.findViewById<ListView>(R.id.list_view)
        val cardView: CardView = contentView.findViewById(R.id.card_view)

        this.hintAdapter = hintAdapter
        dropDownViewAdapter = DropDownViewAdapter(hintAdapter)
        listView.adapter = dropDownViewAdapter
        (cardView.layoutParams as FrameLayout.LayoutParams).setMargins(
            margin,
            margin,
            margin,
            margin
        )
        setContentView(contentView)
        this.listener = listener
        listView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        listener?.onItemSelected(parent, view, position, id)


//        listener?.onItemSelected(parent, view, position, id)
//        listener?.let {
//            it.onItemSelected(parent, view, position, id)
////            if (spinner?.recursiveMode == true) {
////                notifyDataSetChanged()
////            }
//        }
        spinner?.apply {
            layoutSpinnerView(position)
            if (!recursiveMode) {
                this@SpinnerPopupWindow.dismiss()
            }
        }
    }

    fun notifyDataSetChanged() {
        hintAdapter?.notifyDataSetChanged()
        dropDownViewAdapter?.notifyDataSetChanged()
    }
}