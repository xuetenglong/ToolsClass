package com.tools.toolsclass.viewanimation.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tools.toolsclass.R;
import com.tools.toolsclass.util.Logger;

import java.lang.ref.WeakReference;

/**
 *  * 需求如下：
 *  * 学员：显示两行，信息默认弹开，3秒钟后收起，收起后只显示1条，即分值信息。
 *  * 坊主：显示3行，信息默认弹开，3秒钟后收起，收齐后，只显示1条，及分值信息。
 */
public class DescView extends LinearLayout implements View.OnClickListener {
    private final String TAG = this.getClass().getName();
    private final int CLOSE_VIEW = 1001;//关闭布局

    private int mDelayDuration = 3 * 1000;//三秒后，自动关闭布局
    private int mDuration = 500;//展开/关闭布局执行的时间
    private int mText1Height, mText3Height;//第一行、二行、三行的高度
    private int mMinHeight;//最小高度
    private int mExpandHeight;//展开后的高度

    //旋转动画的角度
    private float fromDegrees_open = 180f;
    private float toDegrees_open = 360f;
    private float fromDegrees_close = 0f;
    private float toDegreesOpen_close = 180f;

    private Context mContext;
    private LinearLayout mTextLayout;
    private TextView mTextView1, mTextView2, mTextView3;
    private ImageView mArrowView;//箭头


    private Handler handler;
    private ValueAnimator mOpenAnimator, mCloseAnimator;
    private RotateAnimation mArrowRotateAnimation;
    private boolean isExpand = true;//题干是否已经完全展开了
    private boolean isLeader;//学员展示两行，坊主展示三行

    public DescView(Context context) {
        this(context, null);
    }

    public DescView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DescView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initListener();
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        mContext = context;
        inflate(context, R.layout.desc_layout, this);
        //使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) {
            return;
        }
        mTextLayout = findViewById(R.id.textLayout);
        mTextView1 = findViewById(R.id.text1);
        mTextView2 = findViewById(R.id.text2);
        mTextView3 = findViewById(R.id.text3);
        mArrowView = findViewById(R.id.descview_arrow);
        mText1Height = getResources().getDimensionPixelSize(R.dimen.homepageDescView_text3Height);
        mText3Height = getResources().getDimensionPixelSize(R.dimen.homepageDescView_text3Height);
        mMinHeight = mText1Height;
        isLeader = true;//ProjectManager.isLeader();
        handler = new TaskHandler(this, Looper.getMainLooper());

    }

    private void initListener() {
        mArrowView.setOnClickListener(this);
    }

    public void setData(String text1, String text2, String text3) {
        finish();
        mExpandHeight = 0;
//        mArrowView.setSelected(true);
        boolean text1IsEmpty = TextUtils.isEmpty(text1);
        boolean text2IsEmpty = TextUtils.isEmpty(text2);
        boolean text3IsEmpty = TextUtils.isEmpty(text3);
        int textViewHiddenCount = 0;//当只有一个textview时，隐藏image

        if (text1IsEmpty && text2IsEmpty && text3IsEmpty) {
            setVisibility(GONE);
            return;
        }

        if (text1IsEmpty && text2IsEmpty && !text3IsEmpty) {
            mMinHeight = mText3Height;
        }

        if (!text1IsEmpty) {
            mTextView1.setText(text1);
            mExpandHeight += mText1Height;
        } else {
            mTextView1.setVisibility(GONE);
            textViewHiddenCount++;
        }

        if (!text2IsEmpty && isLeader) {
            mTextView2.setText(text2);
            mExpandHeight += mText1Height;
        } else {
            mTextView2.setVisibility(GONE);
            textViewHiddenCount++;
        }

        if (!text3IsEmpty) {
            mTextView3.setText(text3);
            mExpandHeight += mText3Height;
        } else {
            mTextView3.setVisibility(GONE);
            textViewHiddenCount++;
        }

        if (textViewHiddenCount == 2) {
            //只显示一个textview，隐藏image
            mArrowView.setVisibility(GONE);
        } else {
            delayToClose();
        }

//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();//默认展开
//        lp.height = expandHeight;//默认展开
//        setLayoutParams(lp);
//        invalidate();
//        requestLayout();
    }

    /**
     * 隐藏异常界面和lodingView，展示正常界面
     */
    public void finish() {
        if (handler != null) {
            handler.removeMessages(CLOSE_VIEW);
        }
        if (mOpenAnimator != null) {
            mOpenAnimator.cancel();
        }
        if (mCloseAnimator != null) {
            mCloseAnimator.cancel();
        }
//        mExpandHeight = 0;
    }

    @Override
    protected void onAttachedToWindow() {
        Logger.e(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Logger.e(TAG, "onDetachedFromWindow");
        if (android.os.Build.VERSION.SDK_INT < 24) { //小于7.0，执行finish
            finish();
        } else {
            //7.0以后，只要listview的header滑出屏幕，也会执行onDetachedFromWindow，
            finish();
            mTextLayout.getLayoutParams().height = mMinHeight;//重置为最小高度，即设置成收拢的状态
            mTextLayout.requestLayout();
            isExpand = false;
            mArrowView.setImageResource(R.mipmap.down_arrow);
            //图片换成下箭头，所以，旋转角度需要变化
            fromDegrees_open = 0f;
            toDegrees_open = 180f;
            fromDegrees_close = 180f;
            toDegreesOpen_close = 360f;
        }

        super.onDetachedFromWindow();
    }

    /**
     * 执行展开布局操作
     */
    public void expand() {
        if (!isExpand) {
            if (handler != null) {
                handler.removeMessages(CLOSE_VIEW);
            }
//            mArrowView.setSelected(true);
            getOpenValueAnimator(mTextLayout).setDuration(mDuration).start();
        }
    }

    /**
     * 执行关闭布局操作
     */
    public void collapse() {
        if (isExpand) {
            if (handler != null) {
                handler.removeMessages(CLOSE_VIEW);
            }
//            mArrowView.setSelected(false);
            getCloseValueAnimator(mTextLayout).setDuration(mDuration).start();
        } else {
            expand();
        }
    }

    /**
     * 获取展开布局执行的动画
     *
     * @return
     */
    private ValueAnimator getOpenValueAnimator(final View v) {
        mOpenAnimator = ValueAnimator.ofFloat(0, 1);
        final int currentHeight = v.getHeight();
        mOpenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.getLayoutParams().height = currentHeight + (int) ((mExpandHeight - currentHeight) * interpolatedTime);
                v.requestLayout();
                if (interpolatedTime == 1) {
                    isExpand = true;
                    delayToClose();
                }
            }
        });
        openArrowOpenAnimator();
        return mOpenAnimator;
    }

    private void openArrowOpenAnimator() {
        if (mArrowRotateAnimation != null)
            mArrowRotateAnimation.cancel();
        mArrowRotateAnimation = new RotateAnimation(fromDegrees_open, toDegrees_open, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();//yun
        mArrowRotateAnimation.setInterpolator(lin);
        mArrowRotateAnimation.setDuration(mDuration);//设置动画持续周期
        mArrowRotateAnimation.setRepeatCount(0);//设置重复次数
        mArrowRotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        mArrowRotateAnimation.setStartOffset(0);//执行前的等待时间
        mArrowView.setAnimation(mArrowRotateAnimation);
    }

    private void closeArrowOpenAnimator() {
        if (mArrowRotateAnimation != null)
            mArrowRotateAnimation.cancel();
        mArrowRotateAnimation = new RotateAnimation(fromDegrees_close, toDegreesOpen_close, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        mArrowRotateAnimation.setInterpolator(lin);
        mArrowRotateAnimation.setDuration(mDuration);//设置动画持续周期
        mArrowRotateAnimation.setRepeatCount(0);//设置重复次数
        mArrowRotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        mArrowRotateAnimation.setStartOffset(0);//执行前的等待时间
        mArrowView.setAnimation(mArrowRotateAnimation);
    }

    /**
     * 获取关闭布局执行的动画
     *
     * @return
     */
    private ValueAnimator getCloseValueAnimator(final View v) {
        mCloseAnimator = ValueAnimator.ofFloat(0, 1);
        final int currentHeight = v.getHeight();
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.getLayoutParams().height = currentHeight - (int) ((currentHeight - mMinHeight) * interpolatedTime);
                v.requestLayout();
                if (interpolatedTime == 1) {
                    isExpand = false;
                }
            }
        });
        closeArrowOpenAnimator();
        return mCloseAnimator;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.descview_arrow:
                collapse();
                break;
        }
    }

    private static class TaskHandler extends Handler {
        private final WeakReference<DescView> clientRef;

        TaskHandler(DescView clientRef, Looper looper) {
            super(looper);
            this.clientRef = new WeakReference<DescView>(clientRef);
        }

        @Override
        public void handleMessage(Message msg) {
            DescView client = clientRef.get();
            if (client != null)
                client.handleMessage(msg);
        }
    }

    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case CLOSE_VIEW:
                collapse();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 延迟执行隐藏操作
     */
    private void delayToClose() {
        handler.sendMessageDelayed(Message.obtain(handler, CLOSE_VIEW), mDelayDuration);
    }

}
