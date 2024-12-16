package com.example.rsser.View.Detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.rsser.Presenter.Detail.DetailPresenter;
import android.text.util.Linkify;
import com.example.rsser.R;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;

public class DetailActivity extends BaseView implements DetailInt {
    private DetailPresenter detailPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadData();
    }
    private void loadData(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String url = intent.getStringExtra("url");
        String src = intent.getStringExtra("src");
        String pubdata = intent.getStringExtra("pubdata");

        TextView titleTextView = findViewById(R.id.title_section);
        WebView contentTextView = findViewById(R.id.content_section);
        TextView srcTextView = findViewById(R.id.hyperlink); // 假设您在布局中有一个显示 src 的 TextView
        TextView pubdataTextView = findViewById(R.id.detail_pubdata); // 假设您在布局中有一个显示 pubdata 的 TextView

        // 更新视图内容
        titleTextView.setText(title); // 设置标题
        contentTextView.loadData(content, "text/html; charset=utf-8", "UTF-8");
//        srcTextView.setText("阅读原文"); // 设置来源
        pubdataTextView.setText(pubdata); // 设置发布时间
        String linkText = "阅读原文";
        SpannableString spannableString = new SpannableString(linkText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击后打开链接
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url)); // 替换为实际链接
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        srcTextView.setText(spannableString);
        srcTextView.setMovementMethod(LinkMovementMethod.getInstance()); // 使链接可点击
    }

    @Override
    protected void initPresenter() {
        detailPresenter = new DetailPresenter(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return this.detailPresenter;
    }
}
