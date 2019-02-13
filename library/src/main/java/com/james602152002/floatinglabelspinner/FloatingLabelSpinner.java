package com.james602152002.floatinglabelspinner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.SpinnerAdapter;

import com.james602152002.floatinglabelspinner.adapter.HintAdapter;
import com.james602152002.floatinglabelspinner.popupwindow.SpinnerPopupWindow;

import java.lang.reflect.Field;


/**
 * Created by shiki60215 on 17-12-22.
 */

public class FloatingLabelSpinner extends AppCompatSpinner {

    private short label_horizontal_margin;
    private short label_vertical_margin;
    private final TextPaint labelPaint;

    private short divider_stroke_width;
    private final Paint dividerPaint;

    private short error_vertical_margin;
    private short error_horizontal_margin;
    private final TextPaint errorPaint;

    private int highlight_color;
    private int hint_text_color;
    private int error_color;
    private CharSequence hint;
    private short padding_left, padding_top, padding_right, padding_bottom;
    private short hint_cell_height = -1;

    private float label_text_size;
    private float hint_text_size;
    private float error_text_size;
    private float float_label_anim_percentage = 0;
    private short ANIM_DURATION;
    private short ERROR_ANIM_DURATION_PER_WIDTH;
    private OnItemSelectedListener listener;
    private HintAdapter hintAdapter;
    //    private View hintView;
    private View dropDownHintView;
    //Default hint views
    private int dropDownHintViewID;
    private boolean is_error = false;
    private CharSequence error;
    private ObjectAnimator errorAnimator;
    private float error_percentage = 0;

    private SpinnerPopupWindow popupWindow;
    private boolean recursive_mode = false;
    private View selectedView;
    private boolean is_moving = false;
    private final short touch_slop;
    private float down_x, down_y;
//    private boolean can_select = false;

    public FloatingLabelSpinner(Context context) {
        super(context);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        touch_slop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, null);
    }

    public FloatingLabelSpinner(Context context, int mode) {
        super(context, mode);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        touch_slop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, null);
    }

    public FloatingLabelSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        touch_slop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, attrs);
    }

    public FloatingLabelSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        touch_slop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray defaultArray = context.obtainStyledAttributes(new int[]{R.attr.colorControlNormal, R.attr.colorAccent});
        final int primary_color = defaultArray.getColor(0, 0);
        defaultArray.recycle();
        defaultArray = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatingLabelSpinner);
        label_horizontal_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_label_horizontal_margin, 0);
        label_vertical_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_label_vertical_margin, 0);
        error_horizontal_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_error_horizontal_margin, 0);
        error_vertical_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_error_vertical_margin, dp2px(3));
        hint_text_color = typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_textColorHint, Color.GRAY);
        highlight_color = typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_colorHighlight, primary_color);
        error_color = typedArray.getColor(R.styleable.FloatingLabelSpinner_j_fls_colorError, Color.RED);
        hint = typedArray.getString(R.styleable.FloatingLabelSpinner_j_fls_hint);
        divider_stroke_width = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_thickness, dp2px(2));
        label_text_size = typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_label_textSize, sp2Px(16));
        hint_text_size = typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_hint_textSize, sp2Px(20));
        error_text_size = typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelSpinner_j_fls_error_textSize, sp2Px(16));
        dividerPaint.setStrokeWidth(divider_stroke_width);
        labelPaint.setTextSize(hint_text_size);
        errorPaint.setTextSize(error_text_size);
        dropDownHintViewID = typedArray.getResourceId(R.styleable.FloatingLabelSpinner_j_fls_dropDownHintView, R.layout.drop_down_hint_view);
        ANIM_DURATION = (short) typedArray.getInteger(R.styleable.FloatingLabelSpinner_j_fls_float_anim_duration, 800);
        ERROR_ANIM_DURATION_PER_WIDTH = (short) typedArray.getInteger(R.styleable.FloatingLabelSpinner_j_fls_error_anim_duration, 8000);
        recursive_mode = typedArray.getBoolean(R.styleable.FloatingLabelSpinner_j_fls_recursive, false);

        if (ANIM_DURATION < 0)
            ANIM_DURATION = 800;
        if (ERROR_ANIM_DURATION_PER_WIDTH < 0)
            ERROR_ANIM_DURATION_PER_WIDTH = 8000;

        TypedArray backgroundTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.background});
        Drawable background = backgroundTypedArray.getDrawable(0);
        if (background != null) {
            setBackgroundDrawable(background);
        } else
            setBackgroundColor(0);
        backgroundTypedArray.recycle();
        backgroundTypedArray = null;

        setOnItemSelectedListener(null);
        typedArray.recycle();
        typedArray = null;

        TypedArray paddingArray = context.obtainStyledAttributes(attrs, new int[]
                {android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom});
        if (paddingArray.hasValue(0)) {
            padding_left = padding_top = padding_right = padding_bottom = (short) paddingArray.getDimensionPixelOffset(0, 0);
        } else {
            padding_left = (short) (paddingArray.hasValue(1) ? paddingArray.getDimensionPixelOffset(1, getPaddingLeft()) : 0);
            padding_top = (short) (paddingArray.hasValue(2) ? paddingArray.getDimensionPixelOffset(2, getPaddingTop()) : 0);
            padding_right = (short) (paddingArray.hasValue(3) ? paddingArray.getDimensionPixelOffset(3, getPaddingRight()) : 0);
            padding_bottom = (short) (paddingArray.hasValue(4) ? paddingArray.getDimensionPixelOffset(4, getPaddingBottom()) : 0);
        }
        paddingArray.recycle();
        paddingArray = null;
        updatePadding();

    }

    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * getResources().getDisplayMetrics().density);
    }

    private int sp2Px(float spValue) {
        return (int) (spValue * getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        this.padding_left = (short) left;
        this.padding_top = (short) top;
        this.padding_right = (short) right;
        this.padding_bottom = (short) bottom;
        super.setPadding(left, top + label_vertical_margin + (int) label_text_size, right,
                bottom + divider_stroke_width + (int) error_text_size + (error_vertical_margin << 1));
    }

    public void setHighlight_color(int color) {
        this.highlight_color = color;
        invalidate();
    }

    private void updatePadding() {
        setPadding(padding_left, padding_top, padding_right, padding_bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (float_label_anim_percentage != 1) {
            if (selectedView != null)
                selectedView.setAlpha(getSelectedItemPosition() != 0 ? float_label_anim_percentage : 0);
        }
        super.dispatchDraw(canvas);

        labelPaint.setColor(hint_text_color);
        final float current_text_size = hint_text_size + (label_text_size - hint_text_size) * float_label_anim_percentage;
        labelPaint.setTextSize(current_text_size);

        final int label_paint_dy = (int) (padding_top + current_text_size + (1 - float_label_anim_percentage) * (hint_cell_height * .5f));

        if (hint != null)
            drawSpannableString(canvas, hint, labelPaint, label_horizontal_margin, label_paint_dy);

        final int divider_y = (int) (padding_top + label_text_size + label_vertical_margin + (divider_stroke_width >> 1) + (hint_cell_height > 0 ? hint_cell_height : hint_text_size));
        if (!is_error) {
            dividerPaint.setColor(highlight_color);
        } else {
            dividerPaint.setColor(error_color);
            final int error_paint_dy = (int) (divider_y + error_text_size + error_vertical_margin);
            final float error_text_width = errorPaint.measureText(error.toString());
            final int hint_repeat_space_width = getWidth() / 3;
            final float max_dx = hint_repeat_space_width + error_text_width;
            final int start_x = error_horizontal_margin - (int) (max_dx * error_percentage);
            errorPaint.setColor(error_color);
            if (errorAnimator != null) {
                if (error_horizontal_margin > 0 && errorPaint.getShader() == null) {
                    final float margin_ratio = (float) error_horizontal_margin / getWidth();
                    final float gradient_ratio = .025f;
                    LinearGradient shader = new LinearGradient(0, 0, getWidth(), 0, new int[]{0, error_color, error_color, 0},
                            new float[]{margin_ratio, margin_ratio + gradient_ratio, 1 - margin_ratio - gradient_ratio, 1 - margin_ratio}, Shader.TileMode.CLAMP);
                    errorPaint.setShader(shader);
                } else if (error_horizontal_margin == 0) {
                    errorPaint.setShader(null);
                }
            }
            drawSpannableString(canvas, error, errorPaint, start_x, error_paint_dy);
            if (start_x < 0 && start_x + max_dx < getWidth()) {
                drawSpannableString(canvas, error, errorPaint, (int) (start_x + max_dx), error_paint_dy);
            }
        }
        canvas.drawLine(0, divider_y, getWidth(), divider_y, dividerPaint);
    }

    private void drawSpannableString(final Canvas canvas, CharSequence hint, final TextPaint paint, final int start_x, final int start_y) {
        // draw each span one at a time
        int next;
        float xStart = start_x;
        float xEnd;

        if (paint != errorPaint)
            hint = TextUtils.ellipsize(hint, paint, getWidth() - padding_left - padding_right - label_horizontal_margin, TextUtils.TruncateAt.END);

        if (hint instanceof SpannableString) {
            SpannableString spannableString = (SpannableString) hint;
            for (int i = 0; i < spannableString.length(); i = next) {

                // find the next span transition
                next = spannableString.nextSpanTransition(i, spannableString.length(), CharacterStyle.class);

                // measure the length of the span
                xEnd = xStart + paint.measureText(spannableString, i, next);

                // draw the highlight (background color) first
                BackgroundColorSpan[] bgSpans = spannableString.getSpans(i, next, BackgroundColorSpan.class);
                if (bgSpans.length > 0) {
                    Paint mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    mHighlightPaint.setColor(bgSpans[0].getBackgroundColor());
                    canvas.drawRect(xStart, start_y + paint.getFontMetrics().top, xEnd, start_y + paint.getFontMetrics().bottom, mHighlightPaint);
                }

                // draw the text with an optional foreground color
                ForegroundColorSpan[] fgSpans = spannableString.getSpans(i, next, ForegroundColorSpan.class);
                if (fgSpans.length > 0) {
                    int saveColor = paint.getColor();
                    paint.setColor(fgSpans[0].getForegroundColor());
                    canvas.drawText(spannableString, i, next, xStart, start_y, paint);
                    paint.setColor(saveColor);
                } else {
                    canvas.drawText(spannableString, i, next, xStart, start_y, paint);
                }

                xStart = xEnd;
            }
        } else {
            canvas.drawText(hint, 0, hint.length(), xStart, start_y, paint);
        }

    }

    @Override
    public void setOnItemSelectedListener(@Nullable final OnItemSelectedListener listener) {
        this.listener = listener;
//        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
//                measureHintCellHeight();
//                if (float_label_anim_percentage == 0 && position != 0) {
//                    startAnimator(0, 1);
//                } else if (position == 0 && float_label_anim_percentage != 0) {
//                    startAnimator(1, 0);
//                }
////                if (FloatingLabelSpinner.this.listener != null && can_select) {
////                    FloatingLabelSpinner.this.listener.onItemSelected(parent, view, position, id);
////                    can_select = false;
////                } else {
////                    can_select = true;
////                }
//
//                if (!isRecursive_mode())
//                    popupWindow = null;
//                requestLayout();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                if (FloatingLabelSpinner.this.listener != null) {
//                    FloatingLabelSpinner.this.listener.onNothingSelected(parent);
//                }
//            }
//
//            private void startAnimator(float startValue, float endValue) {
//                final ObjectAnimator animator = ObjectAnimator.ofFloat(FloatingLabelSpinner.this, "float_label_anim_percentage", startValue, endValue);
//                animator.setInterpolator(new AccelerateInterpolator(3));
//                animator.setDuration(ANIM_DURATION);
//                animator.start();
//            }
//        };
//        super.setOnItemSelectedListener(itemSelectedListener);
    }

    private void measureHintCellHeight() {
        if (hint_cell_height == -1) {
            if (hintAdapter != null && hintAdapter.getCount() > 0) {
                View firstChild = hintAdapter.getView(1, null, null);
                if (firstChild != null) {
                    final int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    final int h = w;
                    firstChild.measure(w, h);
                    hint_cell_height = (short) firstChild.getMeasuredHeight();
                    invalidate();
                }
            }
        }
    }


    final private void setFloat_label_anim_percentage(float float_label_anim_percentage) {
        this.float_label_anim_percentage = float_label_anim_percentage;
        invalidate();
    }

    public float getLabel_text_size() {
        return label_text_size;
    }

    public void setLabel_text_size(float label_text_size) {
        this.label_text_size = label_text_size;
        updatePadding();
    }

    public float getHint_text_size() {
        return hint_text_size;
    }

    public void setHint_text_size(float hint_text_size) {
        this.hint_text_size = hint_text_size;
        updatePadding();
    }

    public void setError_text_size(float error_text_size) {
        this.error_text_size = error_text_size;
        errorPaint.setTextSize(error_text_size);
        updatePadding();
    }

    public float getError_text_size() {
        return error_text_size;
    }

    public void setLabelMargins(int horizontal_margin, int vertical_margin) {
        this.label_horizontal_margin = (short) horizontal_margin;
        this.label_vertical_margin = (short) vertical_margin;
        updatePadding();
    }

    public void setThickness(int thickness) {
        this.divider_stroke_width = (short) thickness;
        dividerPaint.setStrokeWidth(divider_stroke_width);
        updatePadding();
    }

    public int getThickness() {
        return divider_stroke_width;
    }

    public void setErrorMargin(int horizontal_margin, int vertical_margin) {
        this.error_horizontal_margin = (short) horizontal_margin;
        this.error_vertical_margin = (short) vertical_margin;
        updatePadding();
    }

    public View getDropDownHintView() {
        return dropDownHintView;
    }

    public void setDropDownHintView(View dropDownHintView) {
        this.dropDownHintView = dropDownHintView;
    }

    public int getDropDownHintViewID() {
        return dropDownHintViewID;
    }

    public void setDropDownHintView(int mDropDownHintView) {
        this.dropDownHintViewID = mDropDownHintView;
    }

    public CharSequence getHint() {
        return hint;
    }

    public void setHint(CharSequence hint) {
        this.hint = hint;
        if (hintAdapter != null)
            hintAdapter.setHint(hint);
        invalidate();
    }

    public void setAnimDuration(int ANIM_DURATION) {
        if (ANIM_DURATION < 0)
            ANIM_DURATION = 800;
        this.ANIM_DURATION = (short) ANIM_DURATION;
    }

    public short getAnimDuration() {
        return ANIM_DURATION;
    }

    public void setErrorAnimDuration(int ERROR_ANIM_DURATION) {
        if (ERROR_ANIM_DURATION < 0)
            ERROR_ANIM_DURATION = 8000;
        this.ERROR_ANIM_DURATION_PER_WIDTH = (short) ERROR_ANIM_DURATION;
    }

    public short getErrorAnimDuration() {
        return ERROR_ANIM_DURATION_PER_WIDTH;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (adapter instanceof HintAdapter) {
            super.setAdapter(adapter);
        } else {
            hintAdapter = new HintAdapter(getContext(), this, adapter);
            super.setAdapter(hintAdapter);
        }
        if (hintAdapter != null)
            hintAdapter.setHint(hint);
    }

    public int getHighlight_color() {
        return highlight_color;
    }

    public int getHint_text_color() {
        return hint_text_color;
    }

    public void setHint_text_color(int hint_text_color) {
        this.hint_text_color = hint_text_color;
    }

    public int getError_color() {
        return error_color;
    }

    public void setError_color(int error_color) {
        this.error_color = error_color;
    }

    public CharSequence getError() {
        return error;
    }

    public void setError(CharSequence error) {
        this.is_error = !TextUtils.isEmpty(error);
        this.error = error;
        if (is_error) {
            if (getWidth() > 0) {
                startErrorAnimation();
            } else {
                addOnLayoutChangeListener(new OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (v.getWidth() > 0) {
                            startErrorAnimation();
                        }
                        v.removeOnLayoutChangeListener(this);
                    }
                });
            }
        } else {
            if (errorAnimator != null) {
                errorAnimator.cancel();
                errorAnimator = null;
            }
        }
        invalidate();
    }

    private void startErrorAnimation() {
        final float error_length = errorPaint.measureText(error.toString());
        final int width = getWidth();
        if (error_length > width - (error_horizontal_margin << 1)) {
            error_percentage = 0;
            if (errorAnimator == null)
                errorAnimator = ObjectAnimator.ofFloat(this, "error_percentage", 0, 1);
            errorAnimator.setRepeatCount(ValueAnimator.INFINITE);
            errorAnimator.setRepeatMode(ValueAnimator.RESTART);
            errorAnimator.setStartDelay(ANIM_DURATION);
            short duration = (short) (ERROR_ANIM_DURATION_PER_WIDTH * error_length / width);
            errorAnimator.setDuration(duration);
            errorAnimator.start();
        }
    }

    private final void setError_percentage(float error_percentage) {
        this.error_percentage = error_percentage;
        invalidate();
    }

    public short getHint_cell_height() {
        measureHintCellHeight();
        return hint_cell_height;
    }

    public boolean isRecursive_mode() {
        return recursive_mode;
    }

    public void setRecursive_mode(boolean recursive_mode) {
        this.recursive_mode = recursive_mode;
    }

    @Override
    public boolean performClick() {
        togglePopupWindow();
        return true;
    }

    @Override
    public boolean performLongClick() {
        togglePopupWindow();
        return true;
    }

    @Override
    public boolean performLongClick(float x, float y) {
        togglePopupWindow();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                is_moving = false;
                down_x = event.getRawX();
                down_y = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!is_moving && (Math.abs(event.getRawX() - down_x) > touch_slop || Math.abs(event.getRawY() - down_y) > touch_slop)) {
                    is_moving = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!is_moving) {
                    performClick();
                }
                break;
        }
        return true;
    }

    private void togglePopupWindow() {
        if (hintAdapter == null)
            return;
        if (popupWindow == null) {
            final short margin = (short) dp2px(8);
            final short card_margin_below_lollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? (short) 0 : (short) dp2px(5);
            popupWindow = new SpinnerPopupWindow(getContext());
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(getWidth() - padding_left - padding_right + ((card_margin_below_lollipop + margin) << 1));
            popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
            popupWindow.setAdapter(this, hintAdapter, margin, listener);
            int dy = -(Math.round(error_text_size) + (error_vertical_margin << 1) + padding_bottom + margin
                    + (card_margin_below_lollipop != 0 ? card_margin_below_lollipop + dp2px(3) : card_margin_below_lollipop));
            popupWindow.showAsDropDown(this, -(card_margin_below_lollipop + margin), dy);
        } else {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public void layoutSpinnerView(int position) {
        removeSelectedView();
        if (position != getSelectedItemPosition()) {
            selectedView = null;
        }

        try {
            Field field = FloatingLabelSpinner.class.getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("mNextSelectedPosition");
            field.setAccessible(true);
            field.set(this, position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSelectedView();
        if (selectedView != null) {
            ViewGroup.LayoutParams lp = selectedView.getLayoutParams();
            if (lp == null)
                lp = generateDefaultLayoutParams();
            addViewInLayout(selectedView, 0, lp);
            selectedView.setSelected(true);
            selectedView.setEnabled(true);
            int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h = w;
            selectedView.measure(w, h);
            final int left = padding_left;
            final int top = (int) (padding_top + label_text_size + label_vertical_margin);
            final int right = padding_left + selectedView.getMeasuredWidth();
            final int bottom = top + selectedView.getMeasuredHeight();
            selectedView.layout(left, top, right, bottom);
            selectedView.requestLayout();
        }

        measureHintCellHeight();
        if (float_label_anim_percentage == 0 && position != 0) {
            startAnimator(0, 1);
        } else if (position == 0 && float_label_anim_percentage != 0) {
            startAnimator(1, 0);
        }

        if (!isRecursive_mode())
            popupWindow = null;
        requestLayout();
    }

    private void startAnimator(float startValue, float endValue) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(FloatingLabelSpinner.this, "float_label_anim_percentage", startValue, endValue);
        animator.setInterpolator(new AccelerateInterpolator(3));
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

    @Override
    public void setSelection(int position, boolean animate) {
        setSelection(position);
    }

    @Override
    public void setSelection(int position) {
        layoutSpinnerView(position);
    }

    private void removeSelectedView() {
        if (selectedView != null)
            removeAllViewsInLayout();
    }

    @Override
    public View getSelectedView() {
        if (hintAdapter != null) {
            final int position = getSelectedItemPosition();
            if (position < hintAdapter.getCount()) {
                selectedView = hintAdapter.getView(position, selectedView, this);
                return selectedView;
            }
        }
        return super.getSelectedView();
    }

    public void dismiss() {
//        can_select = false;
        if (recursive_mode && popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                requestLayout();
            }
        }
    }

    public void notifyDataSetChanged() {
        if (dropDownHintView != null)
            dropDownHintView.invalidate();
        if (popupWindow != null) {
            popupWindow.notifyDataSetChanged();
        }
    }
}
