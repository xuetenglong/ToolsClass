package com.tools.toolsclass.viewanimation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.tools.toolsclass.R;
import com.tools.toolsclass.util.Logger;
import com.tools.toolsclass.util.ScreenUtils;
import com.tools.toolsclass.viewanimation.customview.view.VisibleImageView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 小球上滑动向下消失，下滑和停留2秒出来
 */
public class UpDisappearAnimationActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    private int safeInsetTop; //电池栏的高度

    private int startY1;
    //默认的动画时间
    private static final int TIME_ANIMATION = 300;
    //工具栏是否是隐藏状态
    private boolean isToolHide = true;
    private float viewSlop;
    //按下的y坐标
    private float lastYY;
    //按下的x坐标
    private float lastXX;
    private boolean isMove;
    private Handler handlerUp = new Handler();
    private Runnable runnable = () -> {
        isToolHide = true;
        showTool();
    };


    @BindView(R.id.iv_up_disappear)
    VisibleImageView iv_up_disappear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_disappear_animation);
        mUnbinder = ButterKnife.bind(this);

        safeInsetTop=ScreenUtils.getSafeInsetTop(UpDisappearAnimationActivity.this,getWindow());
        viewSlop = ViewConfiguration.get(UpDisappearAnimationActivity.this).getScaledTouchSlop();
        iv_up_disappear.post(new Runnable() {
            @Override
            public void run() {
                int[] rect = new int[2];
                iv_up_disappear.getLocationOnScreen(rect);
                startY1 = rect[1];
                Log.d("TAG", "---------iv_right_disappear+startY-----------" + startY1);
            }
        });
    }


    //==================动画============================
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastYY = ev.getY();
                lastXX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                handlerUp.removeCallbacks(runnable);
                float disY = ev.getY() - lastYY;
                float disX = ev.getX() - lastXX;
                lastYY = ev.getY();
                lastXX = ev.getX();
                Logger.d("----------------------+++ = %f", disY + "");
                //垂直方向滑动
                if (Math.abs(disY) > viewSlop && Math.abs(disY) > Math.abs(disX)) {
                    if (disY > 15 && !isToolHide) {
                        //往下滑,显示小球
                        isToolHide = true;
                        showTool();
                        Logger.d("----------------------disY = %f", disY + "");
                    } else if (disY <= -15 && isToolHide) {
                        //往上滑，隐藏小球
                        isToolHide = false;
                        hideTool();
                        Logger.d("----------------------disY = %f", disY + "");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                Logger.d("--------------ACTION_UP %b", isToolHide + "");
                if (!isMove && !isToolHide) {
                    handlerUp.postDelayed(runnable, 2000);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 显示
     */
    private void showTool() {
        if (startY1 == 0) return;
    /*    ObjectAnimator anim = ObjectAnimator.ofFloat(iv_up_disappear, "y",
                startY1 + iv_up_disappear.getHeight() + ScreenUtils.sp2px(this, 24),
                startY1);//
        anim.setDuration(TIME_ANIMATION);
        anim.start();*/


        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_up_disappear, "y",
                startY1 + iv_up_disappear.getHeight() + ScreenUtils.sp2px(this, 24),
                startY1-safeInsetTop);//
        anim.setDuration(TIME_ANIMATION);
        anim.start();
    }


    /**
     * 隐藏
     */
    private void hideTool() {
        if (startY1 == 0) return;
       /* ObjectAnimator anim = ObjectAnimator.ofFloat(iv_up_disappear, "y",
                startY1, startY1 + iv_up_disappear.getHeight() + ScreenUtils.sp2px(this, 24));
        anim.setDuration(TIME_ANIMATION);
        anim.start();*/


        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_up_disappear, "y",
                startY1-safeInsetTop, startY1 + iv_up_disappear.getHeight() + ScreenUtils.sp2px(this, 24));
        anim.setDuration(TIME_ANIMATION);
        anim.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
