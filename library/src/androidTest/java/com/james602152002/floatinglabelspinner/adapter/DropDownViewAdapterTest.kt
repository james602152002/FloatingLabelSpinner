package com.james602152002.floatinglabelspinner.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by shiki60215 on 18-1-11.
 */
class DropDownViewAdapterTest : AndroidJUnitRunner() {
    private var dropDownViewAdapter: DropDownViewAdapter? = null
    private var hintAdapter: HintAdapter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val spinner = FloatingLabelSpinner(context)
        spinner.dropDownHintView = View(context)
        spinner.setHint("hint")
        hintAdapter = HintAdapter(context, spinner, object : BaseAdapter() {
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
        })
        dropDownViewAdapter = DropDownViewAdapter(hintAdapter!!)
    }

    @get:Throws(Exception::class)
    @get:Test
    val count: Unit
        get() {
            Assert.assertEquals(hintAdapter!!.count.toLong(), dropDownViewAdapter!!.count)
        }

    @Test
    @Throws(Exception::class)
    fun testItem() {
        val item = dropDownViewAdapter!!.getItem(0)
        Assert.assertEquals(hintAdapter!!.getItem(0), item)
    }

    @Test
    @Throws(Exception::class)
    fun testItemId() {
        val itemID = dropDownViewAdapter!!.getItemId(0)
        Assert.assertEquals(hintAdapter!!.getItemId(0), itemID)
    }

    @Test
    @Throws(Exception::class)
    fun testItemViewType() {
        val itemViewType = dropDownViewAdapter!!.getItemViewType(0)
        Assert.assertEquals(hintAdapter!!.getItemViewType(0).toLong(), itemViewType.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testGetView() {
        val convertView = dropDownViewAdapter!!.getView(0, null, null)
        Assert.assertNotNull(convertView)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        dropDownViewAdapter = null
    }
}