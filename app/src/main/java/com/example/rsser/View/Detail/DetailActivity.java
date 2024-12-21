package com.example.rsser.View.Detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        menuMan();
    }
    private void menuMan() {
        // Return button
        ImageView backbtn = findViewById(R.id.back_button);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView menubtn = findViewById(R.id.more_button);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
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
    private void showPopupMenu(View view) {
        // 创建 PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.detail_pop, popupMenu.getMenu());

        // 设置菜单项的点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        // 显示弹出菜单
        popupMenu.show();
    }
}
