package com.james602152002.floatinglabelspinner.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by shiki60215 on 18-1-11.
 */
class DropDownViewAdapter(private val hintAdapter: HintAdapter) : BaseAdapter() {

    override fun getCount() = hintAdapter.count

    override fun getItem(position: Int): Any? {
        return hintAdapter.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return hintAdapter.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return hintAdapter.getItemViewType(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return hintAdapter.getDropDownView(position, convertView, parent)
    }
}