package com.tools.toolsclass.progressbar.activity;

import android.os.Bundle;

import com.tools.toolsclass.R;
import com.tools.toolsclass.progressbar.fragment.ProgressBarHorizontalFragment;

import androidx.appcompat.app.AppCompatActivity;

public class ProgressBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        ProgressBarHorizontalFragment mProgressBarHorizontal = ProgressBarHorizontalFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_progress_bar, mProgressBarHorizontal).commitAllowingStateLoss();
    }
}
