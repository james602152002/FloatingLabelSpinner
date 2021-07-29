package com.james602152002.floatinglabelspinner

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.BitmapFactory.Options
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.james602152002.floatinglabelspinner.adapter.HintAdapter
import com.james602152002.floatinglabelspinner.popupwindow.SpinnerPopupWindow
import java.lang.ref.SoftReference
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by shiki60215 on 17-12-22.
 */
class FloatingLabelSpinner : AppCompatSpinner {

    private var labelHorizontalMargin = 0
    private var labelVerticalMargin = 0
    private val labelPaint: TextPaint
    private var dividerStrokeWidth = 0
    private val dividerPaint: Paint
    private var errorVerticalMargin = 0
    private var errorHorizontalMargin = 0
    private val errorPaint: TextPaint
    private val iconPaint: Paint
    var highlightColor = 0
        set(value) {
            field = value
            invalidate()
        }
    var hintTextColor = 0
    var errorColor = 0
    private var rightIconColor = 0
    private var hint: CharSequence? = null
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0
    val hintCellHeight: Int
        get() {
            measureHintCellHeight()
            return mHintCellHeight
        }
    private var mHintCellHeight = -1
    var labelTextSize = 0f
        set(value) {
            field = value
            updatePadding()
        }
    var hintTextSize = 0f
        set(value) {
            field = value
            updatePadding()
        }
    var errorTextSize = 0f
        set(value) {
            errorPaint.textSize = value
            field = value
            updatePadding()
        }
    var floatLabelAnimPercentage = 0f
        set(value) {
            field = value
            invalidate()
        }
    var animDuration = 0
        private set
    var errorAnimDuration = 0
        private set
    private var listener: OnItemSelectedListener? = null
    private var hintAdapter: HintAdapter? = null

    //    private View hintView;
    var dropDownHintView: View? = null

    //Default hint views
    var dropDownHintViewID = 0
    private var isError = false
    var error: CharSequence? = null
        set(value) {
            isError = !TextUtils.isEmpty(error)
            field = value
            if (isError) {
                if (width > 0) {
                    startErrorAnimation()
                } else {
                    addOnLayoutChangeListener(object : OnLayoutChangeListener {
                        override fun onLayoutChange(
                            v: View,
                            left: Int,
                            top: Int,
                            right: Int,
                            bottom: Int,
                            oldLeft: Int,
                            oldTop: Int,
                            oldRight: Int,
                            oldBottom: Int
                        ) {
                            if (v.width > 0) {
                                startErrorAnimation()
                            }
                            v.removeOnLayoutChangeListener(this)
                        }
                    })
                }
            } else {
                errorAnimator?.cancel()
                errorAnimator = null
            }
            invalidate()
        }
    private var errorAnimator: ObjectAnimator? = null
    var errorPercentage = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val popupWindow: SpinnerPopupWindow
    var recursiveMode = false
    private var selectedView: View? = null
    private var isMoving = false
    private val touchSlop: Int
    private var downX = 0f
    private var downY = 0f
    private var bitmapHeight = 0
    private var rightIconBitmap: Bitmap? = null
    private var rightIconSize = 0
    private var savedLabel: CharSequence? = null
    var mustFill = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, mode: Int) : super(context, mode) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(
        context,
        attrs,
        defStyleAttr,
        mode
    ) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    init {
        val antiAliasFlag = Paint.ANTI_ALIAS_FLAG
        labelPaint = TextPaint(antiAliasFlag)
        dividerPaint = Paint(antiAliasFlag)
        iconPaint = Paint(antiAliasFlag)
        errorPaint = TextPaint(antiAliasFlag)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop

        popupWindow = SpinnerPopupWindow(context)
        popupWindow.setBackgroundDrawable(ColorDrawable(0))
        popupWindow.isOutsideTouchable = true
        popupWindow.height = LayoutParams.WRAP_CONTENT
    }

    @SuppressLint("ResourceType")
    private fun init(context: Context, attrs: AttributeSet?) {
        val defaultArray = context.obtainStyledAttributes(
            intArrayOf(
                R.attr.colorControlNormal,
                R.attr.colorAccent
            )
        )
        val primaryColor = defaultArray.getColor(0, 0)
        defaultArray.recycle()

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatingLabelSpinner)
        labelHorizontalMargin = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_label_horizontal_margin,
            0
        )
        labelVerticalMargin = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_label_vertical_margin,
            0
        )
        errorHorizontalMargin = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_error_horizontal_margin,
            0
        )
        errorVerticalMargin = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_error_vertical_margin,
            dp2px(3f)
        )
        hintTextColor =
            typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_textColorHint, Color.GRAY)
        highlightColor = typedArray.getColor(
            R.styleable.FloatingLabelSpinner_j_fls_colorHighlight,
            primaryColor
        )
        errorColor =
            typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_colorError, Color.RED)
        rightIconColor =
            typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_rightIconTint, 0)
        hint = typedArray.getString(R.styleable.FloatingLabelSpinner_j_fls_hint)
        dividerStrokeWidth = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_thickness,
            dp2px(2f)
        )
        labelTextSize = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_label_textSize,
            sp2Px(16f)
        ).toFloat()
        hintTextSize = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_hint_textSize,
            sp2Px(20f)
        ).toFloat()
        errorTextSize = typedArray.getDimensionPixelOffset(
            R.styleable.FloatingLabelSpinner_j_fls_error_textSize,
            sp2Px(16f)
        ).toFloat()
        dividerPaint.strokeWidth = dividerStrokeWidth.toFloat()
        labelPaint.textSize = hintTextSize
        errorPaint.textSize = errorTextSize
        dropDownHintViewID = typedArray.getResourceId(
            R.styleable.FloatingLabelSpinner_j_fls_dropDownHintView,
            R.layout.drop_down_hint_view
        )
        animDuration =
            typedArray.getInteger(R.styleable.FloatingLabelSpinner_j_fls_float_anim_duration, 800)
        errorAnimDuration =
            typedArray.getInteger(R.styleable.FloatingLabelSpinner_j_fls_error_anim_duration, 8000)
        recursiveMode =
            typedArray.getBoolean(R.styleable.FloatingLabelSpinner_j_fls_recursive, false)
        savedLabel = hint
        if (typedArray.getBoolean(R.styleable.FloatingLabelSpinner_j_fls_must_fill_type, false)) {
            mustFill = true
            hint = SpannableString(savedLabel.toString() + " *").apply {
                setSpan(
                    ForegroundColorSpan(Color.RED),
                    length - 1, length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        if (animDuration < 0) animDuration = 800
        if (errorAnimDuration < 0) errorAnimDuration = 8000
        val backgroundTypedArray =
            context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.background))
        backgroundTypedArray.getDrawable(0)?.let { background ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(background)
            } else {
                setBackgroundDrawable(background)
            }
        } ?: setBackgroundColor(0)
        backgroundTypedArray.recycle()

        onItemSelectedListener = null
        typedArray.recycle()

        val paddingArray = context.obtainStyledAttributes(
            attrs,
            intArrayOf(
                android.R.attr.padding,
                android.R.attr.paddingLeft,
                android.R.attr.paddingTop,
                android.R.attr.paddingRight,
                android.R.attr.paddingBottom
            )
        )
        if (paddingArray.hasValue(0)) {
            mPaddingBottom = paddingArray.getDimensionPixelOffset(0, 0)
            mPaddingRight = mPaddingBottom
            mPaddingTop = mPaddingRight
            mPaddingLeft = mPaddingTop
        } else {
            mPaddingLeft = (if (paddingArray.hasValue(1)) paddingArray.getDimensionPixelOffset(
                1,
                mPaddingLeft
            ) else 0)
            mPaddingTop = (if (paddingArray.hasValue(2)) paddingArray.getDimensionPixelOffset(
                2,
                mPaddingTop
            ) else 0)
            mPaddingRight = (if (paddingArray.hasValue(3)) paddingArray.getDimensionPixelOffset(
                3,
                mPaddingRight
            ) else 0)
            mPaddingBottom = (if (paddingArray.hasValue(4)) paddingArray.getDimensionPixelOffset(
                4,
                mPaddingBottom
            ) else 0)
        }
        paddingArray.recycle()
        updatePadding()
    }

    fun updatePaintLocale(locale: Locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            labelPaint.textLocale = locale
            errorPaint.textLocale = locale
        }
    }

    private fun dp2px(dpValue: Float): Int {
        return (0.5f + dpValue * resources.displayMetrics.density).toInt()
    }

    private fun sp2Px(spValue: Float): Int {
        return (spValue * resources.displayMetrics.scaledDensity).toInt()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mPaddingLeft = left
        mPaddingTop = top
        mPaddingRight = right
        mPaddingBottom = bottom
        super.setPadding(
            left, top + labelVerticalMargin + labelTextSize.toInt(), right,
            bottom + dividerStrokeWidth + errorTextSize.toInt() + (errorVerticalMargin shl 1)
        )
    }

    private fun updatePadding() {
        setPadding(
            mPaddingLeft,
            mPaddingTop,
            mPaddingRight,
            mPaddingBottom
        )
    }

    public override fun dispatchDraw(canvas: Canvas) {
        if (floatLabelAnimPercentage != 1f) {
            selectedView?.alpha = if (selectedItemPosition != 0) floatLabelAnimPercentage else 0f
        }
        super.dispatchDraw(canvas)
        labelPaint.color = hintTextColor
        val currentTextSize =
            hintTextSize + (labelTextSize - hintTextSize) * floatLabelAnimPercentage
        labelPaint.textSize = currentTextSize
        val labelPaintDy =
            (mPaddingTop + currentTextSize + (1 - floatLabelAnimPercentage) * (mHintCellHeight * .5f)).toInt()
        if (hint != null) drawSpannableString(
            canvas,
            hint,
            labelPaint,
            labelHorizontalMargin,
            labelPaintDy
        )
        val dividerY =
            (mPaddingTop + labelTextSize + labelVerticalMargin + (dividerStrokeWidth shr 1)
                    + if (mHintCellHeight > 0) mHintCellHeight.toFloat() else hintTextSize)
        if (!isError) {
            dividerPaint.color = highlightColor
        } else {
            dividerPaint.color = errorColor
            val errorPaintDy = (dividerY + errorTextSize + errorVerticalMargin).toInt()
            val errorTextWidth = errorPaint.measureText(error.toString())
            val hintRepeatSpaceWidth = width / 3
            val maxDx = hintRepeatSpaceWidth + errorTextWidth
            val startX = errorHorizontalMargin - (maxDx * errorPercentage).toInt()
            errorPaint.color = errorColor
            if (errorAnimator != null) {
                if (errorHorizontalMargin > 0 && errorPaint.shader == null) {
                    val marginRatio = errorHorizontalMargin.toFloat() / width
                    val gradientRatio = .025f
                    val shader = LinearGradient(
                        0f,
                        0f,
                        width.toFloat(),
                        0f,
                        intArrayOf(0, errorColor, errorColor, 0),
                        floatArrayOf(
                            marginRatio,
                            marginRatio + gradientRatio,
                            1 - marginRatio - gradientRatio,
                            1 - marginRatio
                        ),
                        Shader.TileMode.CLAMP
                    )
                    errorPaint.shader = shader
                } else if (errorHorizontalMargin == 0) {
                    errorPaint.shader = null
                }
            }
            drawSpannableString(canvas, error, errorPaint, startX, errorPaintDy)
            if (startX < 0 && startX + maxDx < width) {
                drawSpannableString(
                    canvas,
                    error,
                    errorPaint,
                    (startX + maxDx).toInt(),
                    errorPaintDy
                )
            }
        }
        canvas.drawLine(0f, dividerY, width.toFloat(), dividerY, dividerPaint)
        if (rightIconBitmap != null) {
            drawRightIcon(canvas, dividerY, mHintCellHeight.toFloat())
        }
    }

    private fun drawSpannableString(
        canvas: Canvas,
        hint: CharSequence?,
        paint: TextPaint,
        start_x: Int,
        start_y: Int
    ) {
        // draw each span one at a time
        var ellpHint = hint
        var next: Int
        var xStart = start_x.toFloat()
        var xEnd: Float
        if (paint !== errorPaint) ellpHint = TextUtils.ellipsize(
            ellpHint,
            paint,
            (width - mPaddingLeft - mPaddingRight - labelHorizontalMargin).toFloat(),
            TextUtils.TruncateAt.END
        )
        if (ellpHint is SpannableString) {
            val spannableString = ellpHint
            var i = 0
            while (i < spannableString.length) {


                // find the next span transition
                next = spannableString.nextSpanTransition(
                    i,
                    spannableString.length,
                    CharacterStyle::class.java
                )

                // measure the length of the span
                xEnd = xStart + paint.measureText(spannableString, i, next)

                // draw the highlight (background color) first
                val bgSpans = spannableString.getSpans(i, next, BackgroundColorSpan::class.java)
                if (bgSpans.isNotEmpty()) {
                    val mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                    mHighlightPaint.color = bgSpans[0].backgroundColor
                    canvas.drawRect(
                        xStart,
                        start_y + paint.fontMetrics.top,
                        xEnd,
                        start_y + paint.fontMetrics.bottom,
                        mHighlightPaint
                    )
                }

                // draw the text with an optional foreground color
                val fgSpans = spannableString.getSpans(i, next, ForegroundColorSpan::class.java)
                if (fgSpans.isNotEmpty()) {
                    val saveColor = paint.color
                    paint.color = fgSpans[0].foregroundColor
                    canvas.drawText(spannableString, i, next, xStart, start_y.toFloat(), paint)
                    paint.color = saveColor
                } else {
                    canvas.drawText(spannableString, i, next, xStart, start_y.toFloat(), paint)
                }
                xStart = xEnd
                i = next
            }
        } else {
            canvas.drawText(ellpHint!!, 0, ellpHint.length, xStart, start_y.toFloat(), paint)
        }
    }

    private fun drawRightIcon(canvas: Canvas, dividerY: Float, hint_cell_height: Float) {
        rightIconBitmap?.let {
            canvas.drawBitmap(
                it, (width - mPaddingRight - rightIconSize).toFloat(),
                dividerY - (hint_cell_height + bitmapHeight) * 0.65f, iconPaint
            )
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    private fun measureHintCellHeight() {
        if (mHintCellHeight == -1) {
            if (hintAdapter != null && hintAdapter!!.count > 0) {
                val firstChild = hintAdapter!!.getView(1, null, null)
                if (firstChild != null) {
                    val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    firstChild.measure(w, w)
                    mHintCellHeight = firstChild.measuredHeight
                    invalidate()
                }
            }
        }
    }

    fun setLabelMargins(horizontal_margin: Int, vertical_margin: Int) {
        labelHorizontalMargin = horizontal_margin
        labelVerticalMargin = vertical_margin
        updatePadding()
    }

    var thickness: Int
        get() = dividerStrokeWidth
        set(thickness) {
            dividerStrokeWidth = thickness
            dividerPaint.strokeWidth = dividerStrokeWidth.toFloat()
            updatePadding()
        }

    fun setErrorMargin(horizontal_margin: Int, vertical_margin: Int) {
        errorHorizontalMargin = horizontal_margin
        errorVerticalMargin = vertical_margin
        updatePadding()
    }

    fun setDropDownHintView(mDropDownHintView: Int) {
        dropDownHintViewID = mDropDownHintView
    }

    fun getHint(): CharSequence? {
        return hint
    }

    fun setHint(hint: CharSequence?) {
        this.hint = hint
        if (hintAdapter != null) hintAdapter!!.setHint(hint)
        invalidate()
    }

    fun setAnimDuration(ANIM_DURATION: Int) {
        animDuration = when {
            ANIM_DURATION < 0 -> 800
            else -> ANIM_DURATION
        }
    }

    fun setErrorAnimDuration(ERROR_ANIM_DURATION: Int) {
        errorAnimDuration = when {
            ERROR_ANIM_DURATION < 0 -> 800
            else -> ERROR_ANIM_DURATION
        }
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        if (adapter is HintAdapter) {
            super.setAdapter(adapter)
        } else {
            hintAdapter = HintAdapter(context, this, adapter)
            super.setAdapter(hintAdapter)
        }
        hintAdapter?.setHint(hint)
    }

    private fun startErrorAnimation() {
        val errorLength = errorPaint.measureText(error.toString())
        val width = width
        if (errorLength > width - (errorHorizontalMargin shl 1)) {
            errorPercentage = 0f
            errorAnimator ?: kotlin.run {
                errorAnimator = ObjectAnimator.ofFloat(this, "errorPercentage", 0f, 1f).apply {
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    startDelay = animDuration.toLong()
                    duration = (errorAnimDuration * errorLength / width).toLong()
                    start()
                }
            }
        }
    }

    override fun performClick(): Boolean {
        togglePopupWindow()
        return true
    }

    override fun performLongClick(): Boolean {
        togglePopupWindow()
        return true
    }

    override fun performLongClick(x: Float, y: Float): Boolean {
        togglePopupWindow()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoving = false
                downX = event.rawX
                downY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> if (!isMoving && (Math.abs(event.rawX - downX) > touchSlop || Math.abs(
                    event.rawY - downY
                ) > touchSlop)
            ) {
                isMoving = true
            }
            MotionEvent.ACTION_UP -> if (!isMoving) {
                performClick()
            }
        }
        return true
    }

    private fun togglePopupWindow() {
        hintAdapter ?: return
        when (popupWindow.isShowing) {
            true -> {
                popupWindow.dismiss()
            }
            else -> {
                val margin = dp2px(8f)
                val cardMarginBelowLollipop =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 0 else dp2px(5f)
                val dy: Int =
                    -(errorTextSize.roundToInt() + (errorVerticalMargin shl 1) + mPaddingBottom + margin
                            + if (cardMarginBelowLollipop != 0) cardMarginBelowLollipop + dp2px(
                        3f
                    ) else cardMarginBelowLollipop)
                popupWindow.width =
                    width - mPaddingLeft - mPaddingRight + (cardMarginBelowLollipop + margin shl 1)
                if (hintAdapter != popupWindow.hintAdapter) {
                    popupWindow.setAdapter(this, hintAdapter, margin, listener)
                }
                popupWindow.showAsDropDown(this, -(cardMarginBelowLollipop + margin), dy)
            }
        }
    }

    fun layoutSpinnerView(position: Int) {
        removeSelectedView()
        if (position != selectedItemPosition) {
            selectedView = null
        }
        try {
            FloatingLabelSpinner::class.java.superclass?.superclass?.superclass?.superclass?.getDeclaredField(
                "mNextSelectedPosition"
            )?.let { field ->
                field.isAccessible = true
                field[this] = position
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        getSelectedView()
        if (selectedView != null) {
            var lp = selectedView!!.layoutParams
            if (lp == null) lp = generateDefaultLayoutParams()
            addViewInLayout(selectedView, 0, lp)
            removeAllViewsInLayout()
            addViewInLayout(selectedView, 0, lp)
            selectedView!!.isSelected = true
            selectedView!!.isEnabled = true
            val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            selectedView!!.measure(w, w)
            var iconSize = 0
            if (rightIconBitmap != null) {
                iconSize = rightIconSize - (mPaddingRight shl 1)
            }
            val left = mPaddingLeft
            val top = (mPaddingTop + labelTextSize + labelVerticalMargin).toInt()
            val right = mPaddingLeft + selectedView!!.measuredWidth - iconSize
            val bottom = top + selectedView!!.measuredHeight
            selectedView!!.layout(left, top, right, bottom)
            selectedView!!.requestLayout()
        }
        measureHintCellHeight()
        if (floatLabelAnimPercentage == 0f && position != 0) {
            startAnimator(0f, 1f)
        } else if (position == 0 && floatLabelAnimPercentage != 0f) {
            startAnimator(1f, 0f)
        }
//        if (!recursiveMode) popupWindow = null
        requestLayout()
    }

    private fun startAnimator(startValue: Float, endValue: Float) {
        val animator = ObjectAnimator.ofFloat(
            this@FloatingLabelSpinner,
            "floatLabelAnimPercentage",
            startValue,
            endValue
        )
        animator.interpolator = AccelerateInterpolator(3f)
        animator.duration = animDuration.toLong()
        animator.start()
    }

    override fun setSelection(position: Int, animate: Boolean) {
        setSelection(position)
    }

    override fun setSelection(position: Int) {
        if (recursiveMode) {
            popupWindow.notifyDataSetChanged()
        }
        listener?.onItemSelected(this, this, position, 0)
        layoutSpinnerView(position)
    }

    private fun removeSelectedView() {
//        if (selectedView != null)
        removeAllViewsInLayout()
    }

    override fun getSelectedView(): View? {
        return hintAdapter?.let {
            when (selectedItemPosition) {
                in 0..it.count -> it.getView(selectedItemPosition, selectedView, this)
                in Int.MIN_VALUE..0 -> it.getView(0, selectedView, this)
                else -> super.getSelectedView()
            }
        } ?: super.getSelectedView()
    }

    fun dismiss() {
        if (recursiveMode) {
            popupWindow.dismiss()
            //            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            requestLayout()
            //            }
        }
    }

    fun notifyDataSetChanged() {
        popupWindow.notifyDataSetChanged()
        if (dropDownHintView != null) dropDownHintView!!.invalidate()
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        requestLayout()
        //        }
    }

    fun setRightIcon(drawableID: Int, clear_btn_width: Int, alpha: Int) {
        rightIconSize = clear_btn_width
        iconPaint.alpha = alpha
        val drawable = ContextCompat.getDrawable(context, drawableID)
        //        Drawable drawable = VectorDrawableCompat.create(getResources(),drawableID,null) ;
        val options = Options()
        options.inJustDecodeBounds = true
        val sampleBitmap = createBitmap(drawable, resources, drawableID, options)
        var sampleSize = 1
        var width = options.outWidth
        var height = options.outHeight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) {
            width = drawable.getIntrinsicWidth()
            height = drawable.getIntrinsicHeight()
        }
        bitmapHeight = (height * clear_btn_width / width)
        val destinationHeight = bitmapHeight
        if (height > destinationHeight || width > clear_btn_width) {
            val halfHeight = height shr 1
            val halfWidth = width shr 1
            while (halfHeight / sampleSize > destinationHeight && halfWidth / sampleSize > clear_btn_width) {
                sampleSize *= 2
            }
        }
        sampleBitmap?.recycle()
        options.inSampleSize = sampleSize
        options.inJustDecodeBounds = false
        val oldBitmap = createBitmap(drawable, resources, drawableID, options)
        width = oldBitmap!!.width
        height = oldBitmap.height
        val matrix = Matrix()
        val scaleX = clear_btn_width.toFloat() / width
        val scaleY = destinationHeight.toFloat() / height
        matrix.postScale(scaleX, scaleY)
        if (rightIconColor != 0) {
            iconPaint.colorFilter = PorterDuffColorFilter(rightIconColor, PorterDuff.Mode.SRC_IN)
        }
        rightIconBitmap = SoftReference(
            Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true)
        ).get()
    }

    private fun createBitmap(
        drawable: Drawable?,
        resources: Resources,
        drawableId: Int,
        options: Options
    ): Bitmap? {
        return if (drawable is BitmapDrawable) {
            getBitmap(resources, drawableId, options)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) {
            getVectorBitmap(drawable)
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }

    private fun getBitmap(resources: Resources, drawableId: Int, options: Options): Bitmap? {
        return SoftReference(
            BitmapFactory.decodeResource(resources, drawableId, options)
        ).get()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getVectorBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun setMustFillMode(isMustFill: Boolean) {
        this.mustFill = isMustFill
        updateLabel()
    }

    private fun updateLabel() {
        hint = when (mustFill) {
            true -> {
                SpannableString(savedLabel.toString() + " *").apply {
                    setSpan(
                        ForegroundColorSpan(Color.RED),
                        length - 1,
                        length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            else -> {
                savedLabel
            }
        }
        invalidate()
    }
}