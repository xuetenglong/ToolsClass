package com.tools.toolsclass.viewanimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tools.toolsclass.R;
import com.tools.toolsclass.progressbar.activity.ProgressBarActivity;
import com.tools.toolsclass.viewanimation.fragment.ViewAnimationFragment;
import com.tools.toolsclass.waveview.WaveViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewAnimationActivity extends AppCompatActivity {

    private Unbinder mUnbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        mUnbinder = ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_descView, ViewAnimationFragment.newInstance()).commitAllowingStateLoss();

    }


    @OnClick({R.id.tv_up_disappear, R.id.tv_right_disappear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_up_disappear: //小球上滑动向下消失，下滑和停留2秒出来
                startActivity(new Intent(this, UpDisappearAnimationActivity.class));
                break;
            case R.id.tv_right_disappear: //小球上滑动向右消失，下滑和停留2秒出来
                startActivity(new Intent(this, RightDisappearAnimationActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
