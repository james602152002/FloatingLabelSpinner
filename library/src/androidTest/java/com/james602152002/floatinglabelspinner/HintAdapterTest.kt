package com.james602152002.floatinglabelspinner

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.james602152002.floatinglabelspinner.adapter.HintAdapter
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by shiki60215 on 18-1-9.
 */
class HintAdapterTest : AndroidJUnitRunner() {
    var adapter: HintAdapter? = null
    var spinner: FloatingLabelSpinner? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val baseAdapter: BaseAdapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return 0
            }

            override fun getItem(position: Int): Any? {
                return null
            }

            override fun getItemId(position: Int): Long {
                return 0
            }

            override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {
                return null
            }
        }
        spinner = FloatingLabelSpinner(InstrumentationRegistry.getInstrumentation().targetContext)
        spinner!!.adapter = baseAdapter
        spinner!!.setHint("hint")
        adapter = spinner!!.adapter as HintAdapter
    }

    @Test
    fun testGetCount() {
        Assert.assertEquals(1, adapter!!.count.toLong())
    }

    @Test
    fun testAdapter() {
        adapter!!.count
        adapter!!.getItem(0)
        adapter!!.getItemViewType(0)
        adapter!!.getItemId(0)
        Assert.assertNotNull(adapter!!.getView(0, null, null))
        Assert.assertNotNull(adapter!!.getDropDownView(0, null, null))
    }

    @Test
    fun testDropDownHintView() {
        spinner!!.dropDownHintView =
            View(InstrumentationRegistry.getInstrumentation().targetContext)
        Assert.assertNotNull(adapter!!.getDropDownView(0, spinner!!.dropDownHintView, null))
        adapter!!.setHint(null)
        var dropDownHintView = adapter!!.getDropDownView(0, spinner!!.dropDownHintView, null)
        Assert.assertNull(dropDownHintView)
        spinner!!.dropDownHintView!!.tag = -1
        dropDownHintView = adapter!!.getDropDownView(0, spinner!!.dropDownHintView, null)
        Assert.assertNull(dropDownHintView)
        spinner!!.dropDownHintView!!.tag = 0
        dropDownHintView = adapter!!.getDropDownView(0, spinner!!.dropDownHintView, null)
        Assert.assertNull(dropDownHintView)
    }

    @Test
    fun testDropDownView() {
        adapter!!.setHint(null)
        var dropDownView = adapter!!.getDropDownView(1, null, null)
        Assert.assertNull(dropDownView)
        dropDownView = adapter!!.getDropDownView(
            1,
            View(InstrumentationRegistry.getInstrumentation().targetContext),
            null
        )
        Assert.assertNull(dropDownView)
    }

    @Test
    fun testItemWithoutHint() {
        adapter!!.setHint(null)
        adapter!!.getItem(0)
        adapter!!.getItem(1)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        adapter = null
        spinner = null
    }
}