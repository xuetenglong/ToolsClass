package com.tools.toolsclass.viewanimation;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.os.Bundle;
import android.widget.TextView;

import com.tools.toolsclass.R;
import com.tools.toolsclass.viewanimation.customview.DescView;

public class ViewAnimationActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.descView)
    DescView mDescView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        mUnbinder = ButterKnife.bind(this);
        mDescView.setData("7888888888888888888888888888888888","ryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy","546666666666666666666666666666");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
