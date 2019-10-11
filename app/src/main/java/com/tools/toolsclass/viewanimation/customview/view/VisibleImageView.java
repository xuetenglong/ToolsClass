package com.tools.toolsclass.viewanimation.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class VisibleImageView extends AppCompatImageView {
    public VisibleImageView(Context context) {
        super(context);
    }

    public VisibleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VisibleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("-----","onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("-----","onDetachedFromWindow");
    }
}
