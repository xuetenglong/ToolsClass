package com.tools.toolsclass.progressbar.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tools.toolsclass.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 水平的 进度条
 */
public class ProgressBarHorizontalFragment extends Fragment {
    private Unbinder mUnbinder;
    @BindView(R.id.progressBarHorizontal)
    ProgressBar progressBarHorizontal;


    public static ProgressBarHorizontalFragment newInstance() {
        return new ProgressBarHorizontalFragment();
    }


    public static ProgressBarHorizontalFragment getInstance(String currentTeacherId) {
        ProgressBarHorizontalFragment fragment = new ProgressBarHorizontalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentTeacherId", currentTeacherId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_bar, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        try {
            progressBarHorizontal.setMax(100);
            ValueAnimator va = ValueAnimator.ofInt(0, 80);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progressBarHorizontal.setProgress((int) animation.getAnimatedValue());
                }
            });
            va.start();
        } catch (Exception e) {

        }


    /*    new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 100) {
                    i++;
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBarHorizontal.setProgress(i);
                }
            }
        }.start();*/

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


}
