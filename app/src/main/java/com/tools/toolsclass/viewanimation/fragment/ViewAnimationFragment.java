package com.tools.toolsclass.viewanimation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tools.toolsclass.R;
import com.tools.toolsclass.viewanimation.customview.DescView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 动画
 */
public class ViewAnimationFragment extends Fragment {

    private Unbinder mUnbinder;

    @BindView(R.id.descView)
    DescView mDescView;


    public static ViewAnimationFragment newInstance() {
        return new ViewAnimationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_animation, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDescView.setData("7888888888888888888888888888888888","ryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy","546666666666666666666666666666");
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


}
