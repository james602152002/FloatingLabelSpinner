package com.james602152002.floatinglabelspinner

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.BaseAdapter
import android.widget.Spinner
import androidx.annotation.VisibleForTesting
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created by shiki60215 on 18-1-9.
 */
class FloatingLabelSpinnerTest : AndroidJUnitRunner() {
    //    @Rule
    var customView: FloatingLabelSpinner? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        customView = FloatingLabelSpinner(context, null, 0, Spinner.MODE_DROPDOWN)
        customView = FloatingLabelSpinner(context, null)
        customView = FloatingLabelSpinner(context, Spinner.MODE_DROPDOWN)
        customView = FloatingLabelSpinner(context)
        customView!!.minimumWidth = 100
    }

    @Test
    fun testHintTextEqual() {
        val hint = "hint"
        customView!!.setHint(hint)
        Assert.assertEquals(hint, customView!!.getHint())
    }

    @Test
    fun testHintTextColor() {
        val color = Color.GRAY
        customView!!.hintTextColor = color
        Assert.assertEquals(color.toLong(), customView!!.hintTextColor)
    }

    @Test
    fun testHintTestSize() {
        val text_size = 10f
        customView!!.hintTextSize = text_size
        Assert.assertEquals(text_size, customView!!.hintTextSize, 0f)
    }

    @Test
    fun testHighLightColor() {
        val color = Color.BLUE
        customView!!.highlightColor = color
        Assert.assertEquals(color.toLong(), customView!!.highlightColor)
    }

    @Test
    fun testLabelTextSize() {
        val label_text_size = 8f
        customView!!.labelTextSize = label_text_size
        Assert.assertEquals(label_text_size, customView!!.labelTextSize, 0f)
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testDispatchDraw() {
        val canvas = Canvas()
        customView!!.error = "error"
        var field = FloatingLabelSpinner::class.java.getDeclaredField("errorAnimator")
        field.isAccessible = true
        field[customView] = ObjectAnimator()
        field = FloatingLabelSpinner::class.java.getDeclaredField("hintCellHeight")
        field.isAccessible = true
        field[customView] = 1.toShort()
        customView!!.setAdapter(object : BaseAdapter() {
            override fun getCount(): Int {
                return 0
            }

            override fun getItem(position: Int): Any? {
                return null
            }

            override fun getItemId(position: Int): Long {
                return 0
            }

            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                return View(InstrumentationRegistry.getInstrumentation().targetContext)
            }
        })
        customView!!.setSelection(1)
        field = FloatingLabelSpinner::class.java.getDeclaredField("selectedView")
        field.isAccessible = true
        field[customView] = View(InstrumentationRegistry.getInstrumentation().targetContext)
        customView!!.setHint("hint")
        customView!!.error = "error"
        customView!!.dispatchDraw(canvas)
        customView!!.setSelection(0)
        customView!!.dispatchDraw(canvas)
        field = FloatingLabelSpinner::class.java.getDeclaredField("floatLabelAnimPercentage")
        field.isAccessible = true
        field[customView] = 1
        customView!!.dispatchDraw(canvas)
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testDispatchDrawWithErrorMargin() {
        customView!!.setErrorMargin(10, 10)
        customView!!.error = "error"
        val field = FloatingLabelSpinner::class.java.getDeclaredField("errorAnimator")
        field.isAccessible = true
        field[customView] = ObjectAnimator()
        customView!!.setHint("hint")
        customView!!.error = "error"
        customView!!.dispatchDraw(Canvas())
    }

    @Test
    fun testDispatchDrawWithoutHintAndError() {
        customView!!.setHint(null)
        customView!!.error = null
        customView!!.dispatchDraw(Canvas())
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun testDrawSpannableString() {
        val span = SpannableString("*hint")
        span.setSpan(ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        customView!!.setHint(span)
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod(
            "drawSpannableString",
            Canvas::class.java,
            CharSequence::class.java,
            TextPaint::class.java,
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        method.isAccessible = true
        method.invoke(customView, Canvas(), span, TextPaint(), 0, 0)
    }

    @Test
    fun testError() {
        val error = "error"
        customView!!.requestLayout()
        customView!!.error = error
        Assert.assertEquals(error, customView!!.error)
        customView!!.error = null
    }

    @Test
    fun testErrorColor() {
        val color = Color.RED
        customView!!.errorColor = color
        Assert.assertEquals(color.toLong(), customView!!.errorColor)
    }

    @Test
    fun testErrorTextSize() {
        val error_text_size = 6f
        customView!!.errorTextSize = error_text_size
        Assert.assertEquals(error_text_size, customView!!.errorTextSize, 0f)
    }

    @Test
    fun testMargins() {
        customView!!.setLabelMargins(10, 10)
        customView!!.setErrorMargin(10, 10)
    }

    @Test
    fun testThickness() {
        val thickness = 5
        customView!!.thickness = thickness
        Assert.assertEquals(thickness.toLong(), customView!!.thickness.toLong())
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testErrorPercentage() {
        val test_percentage = .5f
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod(
            "setErrorPercentage",
            Float::class.javaPrimitiveType
        )
        method.isAccessible = true
        method.invoke(customView, test_percentage)
        val field = FloatingLabelSpinner::class.java.getDeclaredField("errorPercentage")
        field.isAccessible = true
        Assert.assertEquals(test_percentage, field[customView])
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testLabelPercentage() {
        val test_percentage = .5f
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod(
            "setFloatLabelAnimPercentage",
            Float::class.javaPrimitiveType
        )
        method.isAccessible = true
        method.invoke(customView, test_percentage)
        val field = FloatingLabelSpinner::class.java.getDeclaredField("floatLabelAnimPercentage")
        field.isAccessible = true
        Assert.assertEquals(test_percentage, field[customView])
    }

    @Test
    fun testAnimationDuration() {
        val anim_duration: Short = 80
        customView!!.setAnimDuration(-1)
        customView!!.setAnimDuration(anim_duration.toInt())
        Assert.assertEquals(anim_duration.toLong(), customView!!.animDuration)
    }

    @Test
    fun testErrorAnimDuration() {
        val error_anim_duration: Short = 5000
        customView!!.setErrorAnimDuration(-1)
        customView!!.setErrorAnimDuration(error_anim_duration.toInt())
        Assert.assertEquals(error_anim_duration.toLong(), customView!!.errorAnimDuration)
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun testAdapter() {
        initAdapter()
        customView!!.setHint("hint")
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod("measureHintCellHeight")
        method.isAccessible = true
        method.invoke(customView)
    }

    @VisibleForTesting
    fun testErrorAnimation() {
        try {
            customView!!.error = "error"
            val method = FloatingLabelSpinner::class.java.getDeclaredMethod("startErrorAnimation")
            method.isAccessible = true
            method.invoke(customView)
        } catch (e: Exception) {
        }
    }

    @Test
    fun testDropDownHintView() {
        val hintView = View(InstrumentationRegistry.getInstrumentation().targetContext)
        customView!!.dropDownHintView = hintView
        Assert.assertEquals(hintView, customView!!.dropDownHintView)
        val dropDownHintView = (Int.MAX_VALUE * Math.random()).toInt()
        customView!!.setDropDownHintView(dropDownHintView)
        Assert.assertEquals(dropDownHintView.toLong(), customView!!.dropDownHintViewID)
    }

    @VisibleForTesting
    fun testOnItemSelectedListener() {
        customView!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val listener = customView!!.onItemSelectedListener
        listener!!.onItemSelected(null, null, 0, 0)
        listener.onNothingSelected(null)
    }

    @VisibleForTesting
    fun testPerformClick() {
        initAdapter()
        customView!!.performClick()
        customView!!.performClick()
    }

    @VisibleForTesting
    fun testPerformLongClick() {
        initAdapter()
        customView!!.performLongClick()
        customView!!.performLongClick(0f, 0f)
    }

    @Test
    fun testRecursiveMode() {
        customView!!.recursiveMode = true
        Assert.assertTrue(customView!!.recursiveMode)
    }

    @Test
    fun testLayoutSpinnerView() {
        customView!!.layoutSpinnerView(1)
    }

    @Test
    fun testSetSelection() {
        val position = 1
        customView!!.setSelection(position, false)
        Assert.assertEquals(position.toLong(), customView!!.selectedItemPosition.toLong())
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testRecursiveModeDismiss() {
        initAdapter()
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod("togglePopupWindow")
        method.isAccessible = true
        method.invoke(customView)
        customView!!.recursiveMode = true
        customView!!.dismiss()
        val field = FloatingLabelSpinner::class.java.getDeclaredField("popupWindow")
        field.isAccessible = true
        Assert.assertNull(field[customView])
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun testAllDismissCondition() {
        initAdapter()
        customView!!.dismiss()
        customView!!.recursiveMode = true
        customView!!.dismiss()
        customView!!.recursiveMode = false
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod("togglePopupWindow")
        method.isAccessible = true
        method.invoke(customView)
        customView!!.dismiss()
    }

    @Test
    fun testMustFillMode() {
        customView!!.setMustFillMode(true)
    }

    @Test
    fun testRightIcon() {
        customView!!.setRightIcon(0, 0, 0)
    }

    @Test
    fun testLocale() {
        customView!!.updatePaintLocale(Locale.getDefault())
    }

    @Test
    fun testMustFill() {
        customView!!.mustFill = true
        Assert.assertEquals(customView!!.mustFill, true)
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testDataSetChanged() {
        initAdapter()
        customView!!.notifyDataSetChanged()
        customView!!.dropDownHintView =
            View(InstrumentationRegistry.getInstrumentation().targetContext)
        val method = FloatingLabelSpinner::class.java.getDeclaredMethod("togglePopupWindow")
        method.isAccessible = true
        method.invoke(customView)
        customView!!.notifyDataSetChanged()
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testTouchEvent() {
        // Obtain MotionEvent object
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 100
        val x = 0.0f
        val y = 0.0f
        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        val metaState = 0
        var motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        val field = FloatingLabelSpinner::class.java.getDeclaredField("performClick")
        field.isAccessible = true
        field[customView] = true
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 1000f, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 0f, 1000f, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent = MotionEvent.obtain(
            downTime,
            eventTime,
            MotionEvent.ACTION_MOVE,
            1000f,
            1000f,
            metaState
        )
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
        motionEvent =
            MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_CANCEL, x, y, metaState)
        customView!!.dispatchTouchEvent(motionEvent)
    }

    private fun initAdapter() {
        customView!!.setAdapter(object : BaseAdapter() {
            override fun getCount(): Int {
                return 1
            }

            override fun getItem(position: Int): Any? {
                return null
            }

            override fun getItemId(position: Int): Long {
                return 0
            }

            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                return View(InstrumentationRegistry.getInstrumentation().targetContext)
            }
        })
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        customView = null
    }
}