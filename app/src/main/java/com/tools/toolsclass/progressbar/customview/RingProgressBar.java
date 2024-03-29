package com.tools.toolsclass.progressbar.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tools.toolsclass.R;

/**
 * 环形
 */
public class RingProgressBar extends View {

    private int mWidth;
    private int mHeight;
    private int diameter = getResources().getDimensionPixelSize(R.dimen.ringProgressBar_diameter);  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 274;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private int[] colors2 = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};//当大于等于100%时，使用
    private float maxValues = 60;
    private float curValues = 0;
    private float mCurrentValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = getResources().getDimensionPixelSize(R.dimen.ringProgressBar_textSize);
    private int aniSpeed = 1000;
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(3);
    private String mStringUnit;

    private String bgArcColor = "#EBEFF2";//#F8F8F8    #EBEFF2


    private boolean isNeedContent;

    // sweepAngle / maxValues 的值
    private float k;

    public RingProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public RingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        int color1 = a.getColor(R.styleable.RingProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.RingProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.RingProgressBar_front_color3, -1);
        if (color3 == -1) {
            colors = new int[]{color1, color2};
            colors2 = new int[]{color1, color2, color1};
        } else {
            colors = new int[]{color1, color2, color3, color3};
        }

        sweepAngle = a.getInteger(R.styleable.RingProgressBar_total_engle, 360);
        bgArcWidth = a.getDimension(R.styleable.RingProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.RingProgressBar_front_width, dipToPx(10));
        isNeedContent = a.getBoolean(R.styleable.RingProgressBar_is_need_content, false);
        curValues = a.getFloat(R.styleable.RingProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.RingProgressBar_max_value, 60);
        mStringUnit = a.getString(R.styleable.RingProgressBar_string_unit);
        if (TextUtils.isEmpty(mStringUnit))
            mStringUnit = "%";
//        setMaxValues(maxValues);
//        setCurrentValues(curValues);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (progressWidth * 2 + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (progressWidth * 2 + diameter + 2 * DEGREE_PROGRESS_DISTANCE);

        setMeasuredDimension(width, height);
    }

    private void initView() {
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
//        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
//        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.right = diameter + (progressWidth * 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (progressWidth * 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (progressWidth * 2 + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (progressWidth * 2 + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.parseColor("#334466"));
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        rotateMatrix.setRotate(270, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.2f", curValues) + mStringUnit, centerX, centerY + textSize / 3, vTextPaint);
        }

    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        Log.e("dyf", "setCurrentValues: ");
        if (maxValues <= 0) {
            currentAngle = 0;
            curValues = 0;
            invalidate();
            return;
        }
        mCurrentValues = currentValues;//显示的数字可以大于100%，如280%
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues >= maxValues) { //当大于等于100%时，显示特定渐变颜色
            colors = colors2;
            sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
//        mCurrentValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }


    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 设置数字后面显示的文字
     *
     * @param stringUnit
     */
    public void setUnit(String stringUnit) {
        mStringUnit = stringUnit;
        if (TextUtils.isEmpty(mStringUnit))
            mStringUnit = "%";
    }


    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, final float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(0, 1);
        progressAnimator.setDuration(length);
//        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                currentAngle = percent * current;
                curValues = percent * (mCurrentValues / maxValues) * 100;
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
