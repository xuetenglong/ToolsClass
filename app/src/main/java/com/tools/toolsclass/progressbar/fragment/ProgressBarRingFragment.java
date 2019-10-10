package com.tools.toolsclass.progressbar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tools.toolsclass.R;
import com.tools.toolsclass.progressbar.customview.CircleProgressView;
import com.tools.toolsclass.progressbar.customview.RingProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 环形  进度条
 */
public class ProgressBarRingFragment extends Fragment {

    private Unbinder mUnbinder;

    @BindView(R.id.circleprogressBar)
    CircleProgressView circleprogressBar;


    @BindView(R.id.ringProgressBar)
    RingProgressBar ringProgressBar;


    public static ProgressBarRingFragment newInstance() {
        return new ProgressBarRingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_bar_ring, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        circleprogressBar.setMax(100);
        circleprogressBar.setCurrent(75);
        circleprogressBar.startAnimProgress(75,1000);


        ringProgressBar.setMaxValues(100);
        ringProgressBar.setCurrentValues(80);
        return view;
    }


 /*   private void calculationMarginLeft(View view) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        int marginLeft;
        if (mList.size() < 4) {
            marginLeft = mContext.getResources().getDimensionPixelSize(R.dimen.ringProgressBar_recyclerView_marginRight);
        } else {
            int screenWidth = ScreenUtils.getScreenWidth(LstApplication.getInstance().getApplicationContext());
            int ringWidth = mContext.getResources().getDimensionPixelSize(R.dimen.ringProgressBar_width);
            marginLeft = (screenWidth - ringWidth * 3 - ringWidth / 2) / 4;//只显示三个半环形
        }
        lp.leftMargin = marginLeft;
        view.setLayoutParams(lp);

    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


}
