package com.james602152002.floatinglabelspinner.popupwindow

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner
import com.james602152002.floatinglabelspinner.adapter.HintAdapter
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by shiki60215 on 18-1-10.
 */
class SpinnerPopupWindowTest : AndroidJUnitRunner() {
    private var popupWindow: SpinnerPopupWindow? = null
    private var spinner: FloatingLabelSpinner? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        spinner = FloatingLabelSpinner(context)
        spinner!!.dropDownHintView = View(context)
        spinner!!.setHint("hint")
        popupWindow = SpinnerPopupWindow(context)
        spinner!!.setAdapter(object : BaseAdapter() {
            override fun getCount(): Int {
                return 0
            }

            override fun getItem(position: Int): Any? {
                return null
            }

            override fun getItemId(position: Int): Long {
                return 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
                return null
            }
        })
        popupWindow!!.setAdapter(
            spinner!!,
            spinner!!.adapter as HintAdapter,
            0,
            spinner!!.onItemSelectedListener
        )
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testOnItemClick() {
        popupWindow!!.onItemClick(null, null, 0, 0)
        val field = SpinnerPopupWindow::class.java.getDeclaredField("listener")
        field.isAccessible = true
        field[popupWindow] = null
        spinner!!.recursiveMode = true
        popupWindow!!.onItemClick(null, null, 0, 0)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        popupWindow = null
        spinner = null
    }
}