package com.james602152002.floatinglabelspinner.adapter

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner

/**
 * Created by shiki60215 on 18-1-10.
 */
class HintAdapter(
    private val mContext: Context,
    private val spinner: FloatingLabelSpinner,
    private val mSpinnerAdapter: SpinnerAdapter
) : BaseAdapter() {
    private val hintType = -1
    private var hint: CharSequence? = spinner.getHint()

    fun setHint(hint: CharSequence?) {
        this.hint = hint
    }

    override fun getViewTypeCount(): Int {
        //Workaround waiting for a Google correction (https://code.google.com/p/android/issues/detail?id=79011)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            1
        } else mSpinnerAdapter.viewTypeCount
    }

    override fun getItemViewType(position: Int): Int {
        return when (val adjustPos = posShift(position)) {
            -1 -> hintType
            else -> mSpinnerAdapter.getItemViewType(adjustPos)
        }
    }

    override fun getCount(): Int {
        val count = mSpinnerAdapter.count
        return if (hint != null) count + 1 else count
    }

    override fun getItem(position: Int): Any? {
        return when (val adjustPos = posShift(position)) {
            -1 -> hint
            else -> mSpinnerAdapter.getItem(adjustPos)
        }
    }

    override fun getItemId(position: Int): Long {
        return when (val adjustPos = posShift(position)) {
            -1 -> 0
            else -> mSpinnerAdapter.getItemId(adjustPos)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return buildView(position, convertView, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return buildView(position, convertView, parent, true)
    }

    private fun buildView(
        position: Int,
        view: View?,
        parent: ViewGroup?,
        isDropDownView: Boolean
    ): View {
        var adjustPos = position
        var convertView: View? = view
        if (getItemViewType(adjustPos) == hintType) {
            return getHintView(parent, isDropDownView)
        }
        //workaround to have multiple types in spinner
        if (convertView != null) {
            convertView = when (val tag = convertView.tag) {
                tag is Int && tag != hintType -> convertView
                else -> null
            }
        }
        adjustPos = posShift(position)

        return if (isDropDownView) mSpinnerAdapter.getDropDownView(
            adjustPos,
            convertView,
            parent
        ) else mSpinnerAdapter.getView(adjustPos, convertView, parent)
    }

    private fun getHintView(parent: ViewGroup?, isDropDownView: Boolean): View {
        val convertView: View?
        val dropDownHintView = spinner.dropDownHintView
        val dropDownHintViewID = spinner.dropDownHintViewID
        val hintCellHeight = spinner.hintCellHeight
        val hintTextSize = spinner.hintTextSize
        if (isDropDownView) {
            if (dropDownHintView != null) {
                convertView = dropDownHintView
                convertView.tag = hintType
            } else {
                val inflater = LayoutInflater.from(mContext)
                val textView = inflater.inflate(dropDownHintViewID, parent, false) as TextView
                textView.layoutParams =
                    AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        hintCellHeight
                    )
                textView.gravity = Gravity.CENTER_VERTICAL
                textView.text = hint
                textView.setTextColor(spinner.hintTextColor)
                if (hintTextSize != -1f) textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    hintTextSize
                )
                convertView = textView
            }
        } else {
            convertView = View(mContext)
            convertView.layoutParams =
                AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, hintCellHeight)
        }
        if (convertView.layoutParams == null) {
            val lp = AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            convertView.layoutParams = lp
        }
        return convertView
    }

    private fun posShift(position: Int) = when (hint) {
        null -> position
        else -> position - 1
    }
}