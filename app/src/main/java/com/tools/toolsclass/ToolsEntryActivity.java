package com.tools.toolsclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tools.toolsclass.progressbar.activity.ProgressBarActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ToolsEntryActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.tv_horizontal_progress_bar)
    TextView tv_horizontal_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_entry);
        mUnbinder = ButterKnife.bind(this);
    }


    @OnClick(R.id.tv_horizontal_progress_bar)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_horizontal_progress_bar: //进度条相关
                startActivity(new Intent(this, ProgressBarActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
