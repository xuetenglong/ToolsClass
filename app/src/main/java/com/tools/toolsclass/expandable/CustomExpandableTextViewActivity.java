package com.tools.toolsclass.expandable;

import android.os.Bundle;

import com.tools.toolsclass.R;
import com.tools.toolsclass.expandable.customview.CustomExpandableTextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 文本展开收起
 */
public class CustomExpandableTextViewActivity extends AppCompatActivity {
    private Unbinder mUnbinder;


    @BindView(R.id.comment_expandable_view)
    CustomExpandableTextView comment_expandable_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_expandable_text_view);
        mUnbinder = ButterKnife.bind(this);


        comment_expandable_view.setText("简介：“你最喜欢的产品是什么？”这些问题也类似于“改进产品”" +
                "，只不过方法是相反的；不你最喜欢的产品是什么？”这些问题也类似于“改进产品”，只不过" +
                "方法是相反的；不你最喜欢的产品是什么？”这些问题也类似于“改进产品”，只不过方法是相反" +
                "的；不你最喜欢的产品是什么？”这些问题也类似于“改进产品”，只不过方法是相反的；不你最" +
                "喜欢的产品是什么？”这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的" +
                "产品是什么？”这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的产品是什么？" +
                "”这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的产品是什么？”" +
                "这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的产品是什么？”" +
                "这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的产品是什么？”" +
                "这些问题也类似于“改进产品”，只不过方法是相反的；不你最喜欢的产品是什么？”" +
                "这些问题也类似于“改进产品”，只不过方法是相反的；不是让你谈论这个产品有什么" +
                "问题，而是讨论你为什么喜欢这个产品。其实是…谈…");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
