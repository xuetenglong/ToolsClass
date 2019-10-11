package com.tools.toolsclass.viewanimation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.tools.toolsclass.R;
import com.tools.toolsclass.util.ScreenUtils;
import com.tools.toolsclass.viewanimation.customview.view.VisibleImageView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 小球上滑动向右消失，下滑和停留2秒出来
 */
public class RightDisappearAnimationActivity extends AppCompatActivity implements  GestureDetector.OnGestureListener{

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
    
    
    @BindView(R.id.iv_right_disappear)
    VisibleImageView iv_right_disappear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_disappear_animation);
        mUnbinder = ButterKnife.bind(this);

        safeInsetTop=ScreenUtils.getSafeInsetTop(RightDisappearAnimationActivity.this,getWindow());
        viewSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        GestureDetector mGestureDetector = new GestureDetector(
                this, this);
         MyOnTouchListener myOnTouchListener = new MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };
       registerMyOnTouchListener(myOnTouchListener);
        iv_right_disappear.post(new Runnable() {
            @Override
            public void run() {
                int[] rect = new int[2];
                iv_right_disappear.getLocationOnScreen(rect);
                startY1 = rect[0];
                Log.d("TAG", "---------iv_right_disappear+startY-----------" + startY1);
            }
        });
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }


    // 一旦触摸屏按下，就马上产生onDown事件
    @Override
    public boolean onDown(MotionEvent ev) {
        lastYY = ev.getY();
        lastXX = ev.getX();
        return false;
    }


    // 点击了触摸屏，但是没有移动和弹起的动作onShowPress和onDown的区别在于 onDown是，
    // 一旦触摸屏按下，就马上产生onDown事件，但是onShowPress是onDown事件产生后，
    // 一段时间内，如果没有移动鼠标和弹起事件，就认为是onShowPress事件。
    @Override
    public void onShowPress(MotionEvent ev) {

    }

    // 轻击触摸屏后，弹起。如果这个过程中产onLongPress、onScroll和onFling事件，
    // 就不会 产生onSingleTapUp事件。
    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        return false;
    }
    // 当手在屏幕上滑动离开屏幕时触发，参数跟onFling一样（注意两者的区别）
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
        isMove = true;
        handlerUp.removeCallbacks(runnable);
        float disY = e2.getY() - lastYY;
        float disX = e2.getX() - lastXX;
        lastYY = e2.getY();
        lastXX = e2.getX();
        Log.d("TAG", "----------------------+++ = " + disY);
        //垂直方向滑动
        if (Math.abs(disY) > viewSlop && Math.abs(disY) > Math.abs(disX)) {
            if (disY > 15 && !isToolHide) {
                //往下滑,显示小球
                isToolHide = true;
                showTool();
                Log.d("TAG", "----------------------disY = " + disY);
            } else if (disY <= -15 && isToolHide) {
                //往上滑，隐藏小球
                isToolHide = false;
                hideTool();
                Log.d("TAG", "----------------------disY = " + disY);
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        isMove = false;
        Log.d("TAG", "--------------ACTION_UP" + isToolHide);
        if (!isMove && !isToolHide) {
            handlerUp.postDelayed(runnable, 2000);
        }
        return false;
    }


    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }


    /**
     * 显示
     */
    private void showTool() {
        if (startY1 == 0) return;
        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_right_disappear, "x",
                startY1 + iv_right_disappear.getWidth() + ScreenUtils.sp2px(this, 24),
                startY1);//
        anim.setDuration(300);
        anim.start();
    }


    /**
     * 隐藏
     */
    private void hideTool() {
        if (startY1 == 0) return;
        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_right_disappear, "x",
                startY1, startY1 + iv_right_disappear.getWidth() + ScreenUtils.sp2px(this, 24));
        anim.setDuration(300);
        anim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
