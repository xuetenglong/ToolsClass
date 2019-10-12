package com.tools.toolsclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tools.toolsclass.clearedittext.ClearEditTextActivity;
import com.tools.toolsclass.expandable.CustomExpandableTextViewActivity;
import com.tools.toolsclass.progressbar.activity.ProgressBarActivity;
import com.tools.toolsclass.viewanimation.ViewAnimationActivity;
import com.tools.toolsclass.waveview.WaveViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ToolsEntryActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.tv_horizontal_progress_bar)
    TextView tv_horizontal_progress_bar;

    @BindView(R.id.tv_waveView)
    TextView tv_waveView;

    @BindView(R.id.tv_descView)
    TextView tv_descView;

    @BindView(R.id.comment_expandable_view)
    TextView comment_expandable_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_entry);
        mUnbinder = ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_horizontal_progress_bar, R.id.tv_waveView, R.id.tv_descView,
            R.id.tv_clearEditText, R.id.comment_expandable_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_horizontal_progress_bar: //进度条相关
                startActivity(new Intent(this, ProgressBarActivity.class));
                break;
            case R.id.tv_waveView: //水波纹相关
                startActivity(new Intent(this, WaveViewActivity.class));
                break;
            case R.id.tv_descView: //水波纹相关
                startActivity(new Intent(this, ViewAnimationActivity.class));
                break;
            case R.id.tv_clearEditText: //清除文本框类容
                startActivity(new Intent(this, ClearEditTextActivity.class));
                break;
            case R.id.comment_expandable_view: //文本展开收起
                startActivity(new Intent(this, CustomExpandableTextViewActivity.class));
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
