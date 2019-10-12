package com.tools.toolsclass.expandable.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tools.toolsclass.R;
import com.tools.toolsclass.util.ScreenUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 可展开的TextView效果控件
 * 参考https://github.com/Chen-Sir/ExpandableTextView, 去掉了动画效果
 */
public class CustomExpandableTextView extends LinearLayout implements View.OnClickListener {

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 5;

    /* The default content text size*/
    private static final int DEFAULT_CONTENT_TEXT_SIZE = 16;
    private static final float DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER = 1.0f;

    private static final int STATE_TV_GRAVITY_LEFT = 0;
    private static final int STATE_TV_GRAVITY_CENTER = 1;
    private static final int STATE_TV_GRAVITY_RIGHT = 2;

    private static final String DEFAULT_TEXT_EXPAND = "展开全部";
    private static final String DEFAULT_TEXT_COLLAPSED = "收起";

    protected TextView mTv;

    protected ImageView mImg;

    boolean isImg;

    protected TextView mStateTv; // TextView to expand/collapse

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mMaxCollapsedLines;

    private Drawable mExpandDrawable;

    private Drawable mCollapseDrawable;

    private int mStateTvGravity;

    private String mCollapsedString;

    private String mExpandString;

    private int mContentTextSize;

    private int mContentTextColor;

    private float mContentLineSpacingMultiplier;

    private int mStateTextColor;
    private int mStateTextSize;

    private OnExpandStateChangeListener mListener;

    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public CustomExpandableTextView(Context context) {
        this(context, null);
    }

    public CustomExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {

        if (mStateTv.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        mStateTv.setText(mCollapsed ? mExpandString : mCollapsedString);
        mStateTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
        } else {
            mTv.setMaxLines(Integer.MAX_VALUE);
        }
        mTv.invalidate();

        // notify the listener
        if (mListener != null) {
            mListener.onExpandStateChanged(mTv, !mCollapsed);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mStateTv.setVisibility(View.GONE);
        mTv.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
            mTv.invalidate();
        }
        mStateTv.setVisibility(View.VISIBLE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(@Nullable CharSequence text) {
        mRelayout = true;
        if (isImg) {
            mImg.setVisibility(View.VISIBLE);
            mImg.setImageResource(R.mipmap.ic_competitive_product);
            mImg.setPadding(0,0, ScreenUtils.dpToPxInt(getContext(), 6),0);
            mTv.setText("                "+text);
            //mTv.setText(setLeftImage(getContext(), "", text.toString()));
        } else {
            mTv.setText(text);
            mImg.setVisibility(View.GONE);
            mImg.setPadding(0,0,0,0);

        }
        if(!isImg){
            setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        }

    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        mStateTv.setText(mCollapsed ? mExpandString : mCollapsedString);
        mStateTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        if (mTv == null) {
            return "";
        }
        return mTv.getText();
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.a_custom_expandable_textview, this, true);
        // enforces vertical orientation
        setOrientation(LinearLayout.VERTICAL);

        // default visibility is gone
        setVisibility(GONE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.CustomExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mContentTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomExpandableTextView_contentTextSize, DEFAULT_CONTENT_TEXT_SIZE);
        mContentLineSpacingMultiplier = typedArray.getFloat(R.styleable.CustomExpandableTextView_contentLineSpacingMultiplier, DEFAULT_CONTENT_TEXT_LINE_SPACING_MULTIPLIER);
        mContentTextColor = typedArray.getColor(R.styleable.CustomExpandableTextView_contentTextColor, Color.BLACK);

        mExpandDrawable = typedArray.getDrawable(R.styleable.CustomExpandableTextView_expandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.CustomExpandableTextView_collapseDrawable);
        mStateTvGravity = typedArray.getInt(R.styleable.CustomExpandableTextView_DrawableAndTextGravity, STATE_TV_GRAVITY_LEFT);
        mExpandString = typedArray.getString(R.styleable.CustomExpandableTextView_expandText);
        mCollapsedString = typedArray.getString(R.styleable.CustomExpandableTextView_collapseText);
        mStateTextColor = typedArray.getColor(R.styleable.CustomExpandableTextView_expandCollapseTextColor, Color.BLACK);
        mStateTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomExpandableTextView_expandCollapseTextSize, DEFAULT_CONTENT_TEXT_SIZE);

        if (mExpandString == null) {
            mExpandString = DEFAULT_TEXT_EXPAND;
        }
        if (mCollapsedString == null) {
            mCollapsedString = DEFAULT_TEXT_COLLAPSED;
        }

        typedArray.recycle();

    }

    private void findViews() {
        mImg = findViewById(R.id.expandable_img);
        mTv = findViewById(R.id.expandable_text);
        mTv.setTextColor(mContentTextColor);
        mTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentTextSize);
        mTv.setLineSpacing(0, mContentLineSpacingMultiplier);
//        mTv.setOnClickListener(this);

        mStateTv = findViewById(R.id.expand_collapse);
        mStateTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStateTextSize);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (mStateTvGravity == STATE_TV_GRAVITY_LEFT) {
            params.gravity = Gravity.START;
        } else if (mStateTvGravity == STATE_TV_GRAVITY_CENTER) {
            params.gravity = Gravity.CENTER_HORIZONTAL;
        } else if (mStateTvGravity == STATE_TV_GRAVITY_RIGHT) {
            params.gravity = Gravity.END;
        }
        mStateTv.setLayoutParams(params);
        mStateTv.setText(mCollapsed ? mExpandString : mCollapsedString);
        mStateTv.setTextColor(mStateTextColor);
        mStateTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mCollapsed ? mExpandDrawable : mCollapseDrawable, null);
        mStateTv.setOnClickListener(this);
    }

    /**
     * 设置展开全部是否可点击
     *
     * @param isExpandable
     */
    public void setIsExpandable(boolean isExpandable) {
        mStateTv.setClickable(isExpandable);
    }


    /**
     * 是否展示图片
     */
    public void setIsImg(boolean isImg) {
        this.isImg = isImg;
        if(isImg){
            mImg.setVisibility(View.VISIBLE);
            mImg.setImageResource(R.mipmap.ic_competitive_product);
            mImg.setPadding(0,0,ScreenUtils.dpToPxInt(getContext(), 6),0);
        }else {
            mImg.setVisibility(View.GONE);
            mImg.setPadding(0,0,0,0);
        }
    }


    /**
     * 设置最大显示行数,超过最大行数后显示暂开全部
     *
     * @param mMaxCollapsedLines
     */
    public void setMaxCollapsedLines(int mMaxCollapsedLines) {
        this.mMaxCollapsedLines = mMaxCollapsedLines;
        mTv.setMaxLines(mMaxCollapsedLines);
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }


    public SpannableString setLeftImage(Context context, String flag, String msg) {
        SpannableString spannableString = new SpannableString("  " + msg);
        Drawable rightDrawable = null;
        rightDrawable = context.getResources().getDrawable(R.mipmap.ic_competitive_product);
        rightDrawable.setBounds(0, 0, rightDrawable.getIntrinsicWidth(), rightDrawable.getIntrinsicHeight());

        spannableString.setSpan(new MyImageSpan(rightDrawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public class MyImageSpan extends ImageSpan {
        public MyImageSpan(Context context, Bitmap bitmap) {
            super(context, bitmap);
        }

        public MyImageSpan(Context context, Bitmap bitmap, int verticalAlignment) {
            super(context, bitmap, verticalAlignment);
        }

        public MyImageSpan(Drawable drawable) {
            super(drawable);
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            try {
                Drawable d = getDrawable();
                Rect rect = d.getBounds();
                if (fm != null) {
                    Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                    int fontHeight = fmPaint.bottom - fmPaint.top;
                    int drHeight = rect.bottom - rect.top;

                    int top = drHeight / 2 - fontHeight / 4;
                    int bottom = drHeight / 2 + fontHeight / 4;

                    fm.ascent = -bottom;
                    fm.top = -bottom;
                    fm.bottom = top;
                    fm.descent = top;
                }
                return rect.right;
            } catch (Exception e) {
                return 20;
            }
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            try {
                Drawable d = getDrawable();
                canvas.save();
                int transY = 0;
                transY = ((bottom - top) - d.getBounds().bottom) / 2 + top;
                canvas.translate(x, transY);
                d.draw(canvas);
                canvas.restore();
            } catch (Exception e) {
            }
        }
    }
}
