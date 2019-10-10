package com.tools.toolsclass.progressbar.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tools.toolsclass.R;
import com.tools.toolsclass.util.YXDigitUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 环形进度条控件
 */
public class CircleProgressView extends View {
    private Paint mProgressPaint = new Paint();//背景弧线paint
    private Paint mTextPaint = new Paint();//文字paint
    private int[] mProgressColors;

    private int mProgressWidth;//进度条宽度
    private float startAngle = 270;//开始角度, 从顶部开始

    private ValueAnimator mAnimator;

    private int mProgressBgColor;//进度条底色

    private int mCurrent;//当前进度
    private int mMax = 100;//最大进度，默认100

    private int mTextColor;
    private float mTextSize;
    private boolean mTextIsBold;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mProgressBgColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_bg_color, ContextCompat.getColor(getContext(), R.color.cf8f8f8));
        mProgressWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressView_circle_progress_width, dp2px(context, 4));
        int colorStart = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_color_start, ContextCompat.getColor(getContext(), R.color.c5293f5));
        int colorEnd = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_color_end, -1);
        if (colorEnd == -1) {
            mProgressColors = new int[]{colorStart};
        } else {
            mProgressColors = new int[]{colorStart, colorEnd};
        }
        mTextColor = typedArray.getColor(R.styleable.CircleProgressView_circle_progress_text_color, ContextCompat.getColor(getContext(), R.color.c5293f5));
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressView_circle_progress_text_size, dp2px(getContext(), 16));
        mTextIsBold = typedArray.getBoolean(R.styleable.CircleProgressView_circle_progress_text_bold, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int size = width < height ? width : height;
        setMeasuredDimension(size, size);
    }


    private void resetProgressPaint() {
        mProgressPaint.reset();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));

        //绘制背景
        resetProgressPaint();
        RectF rectF = new RectF(mProgressWidth / 2, mProgressWidth / 2, getWidth() - mProgressWidth / 2, getHeight() - mProgressWidth / 2);
        mProgressPaint.setColor(mProgressBgColor);
        canvas.drawArc(rectF, 0, 360, false, mProgressPaint);

        //绘制进度
        resetProgressPaint();
        float percent = mMax == 0 ? 0 : mCurrent * 1.0f / mMax;
        if (percent < 1) {
            mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            mProgressPaint.setStrokeCap(Paint.Cap.BUTT);
        }
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        if (mProgressColors.length > 1) {
            SweepGradient sweepGradient = new SweepGradient(centerX, centerY, mProgressColors, null);
            Matrix matrix = new Matrix();
            matrix.setRotate(startAngle, centerX, centerY);
            sweepGradient.setLocalMatrix(matrix);
            mProgressPaint.setShader(sweepGradient);
        } else {
            mProgressPaint.setColor(mProgressColors[0]);
        }
        canvas.drawArc(rectF, startAngle, percent * 360, false, mProgressPaint);

        //绘制进度开始半圆
        if (mProgressColors.length > 1 && percent > 0 && percent < 1) {
            mProgressPaint.reset();
            mProgressPaint.setColor(mProgressColors[0]);
            mProgressPaint.setAntiAlias(true);
            mProgressPaint.setStyle(Paint.Style.FILL);
            RectF rectStart = new RectF(centerX - mProgressWidth / 2, 0, centerX + mProgressWidth / 2, mProgressWidth);
            canvas.drawArc(rectStart, 90, 180f, true, mProgressPaint);
        }

        // 绘制进度文字
        mTextPaint.reset();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        if (mTextIsBold) {
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
        String mContent = YXDigitUtil.floatToPercentKeep2Decimal(String.valueOf(percent));
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        // 计算文字高度 
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字baseline 
        float textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
        canvas.drawText(mContent, getWidth() / 2, textBaseY, mTextPaint);

    }

    public int getCurrent() {
        return mCurrent;
    }

    /**
     * 设置进度
     *
     * @param current
     */
    public void setCurrent(int current) {
        mCurrent = current;
        invalidate();
    }


    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMax(int max) {
        mMax = max;
    }

    private int tCurrent = -1;

    /**
     * 动画效果
     *
     * @param current  精度条进度：0-100
     * @param duration 动画时间
     */
    public void startAnimProgress(int current, int duration) {
        mAnimator = ValueAnimator.ofInt(0, current);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(animation -> {
            int current1 = (int) animation.getAnimatedValue();
            if (tCurrent != current1) {
                tCurrent = current1;
                setCurrent(current1);
                if (mOnAnimProgressListener != null)
                    mOnAnimProgressListener.valueUpdate(current1);
            }
        });
        mAnimator.start();
    }

    public interface OnAnimProgressListener {
        void valueUpdate(int progress);
    }

    private OnAnimProgressListener mOnAnimProgressListener;

    /**
     * 监听进度条进度
     *
     * @param onAnimProgressListener
     */
    public void setOnAnimProgressListener(OnAnimProgressListener onAnimProgressListener) {
        mOnAnimProgressListener = onAnimProgressListener;
    }

    public void destroy() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
