package com.example.rsser.View.RssManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rsser.DAO.Source;
import com.example.rsser.R;

public class SourceDetailActivity extends AppCompatActivity {
    private TextView textViewTitle, textViewUrl, textViewDescription;
    private Button buttonEdit, buttonDelete, buttonShare;
    private Source source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewUrl = findViewById(R.id.textViewUrl);
        textViewDescription = findViewById(R.id.textViewDescription);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonShare = findViewById(R.id.buttonShare);

        int sourceId = getIntent().getIntExtra("sourceId", -1);
        if (sourceId == -1) {
            finish();
            return;
        }

        loadSourceDetails(sourceId);
        setupButtonListeners();
    }

    private void loadSourceDetails(int sourceId) {
        // 从数据源中加载指定 ID 的订阅源
        // 将订阅源的信息填充到 UI 控件中
        source = getSourceById(sourceId);
        if (source != null) {
            textViewTitle.setText(source.getTitle());
            textViewUrl.setText(source.getUrl());
            textViewDescription.setText(source.getDescription());
        } else {
            finish();
        }
    }

    private void setupButtonListeners() {
        buttonEdit.setOnClickListener(v -> {
            // 打开编辑订阅源的对话框
            showEditSourceDialog(source);
        });

        buttonDelete.setOnClickListener(v -> {
            // 删除订阅源
            deleteSource(source);
            finish();
        });

        buttonShare.setOnClickListener(v -> {
            // 分享订阅源
            shareSource(source);
        });
    }

    private void showEditSourceDialog(Source source) {
        // 显示编辑订阅源的对话框
        // 获取用户修改的订阅源信息
        // 更新数据源中的订阅源信息
        // 更新 UI
    }

    private void deleteSource(Source source) {
        // 从数据源中删除该订阅源
        // 更新 UI
    }

    private void shareSource(Source source) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "订阅源: " + source.getTitle() + "\n" + source.getUrl());
        startActivity(Intent.createChooser(shareIntent, "分享订阅源"));
    }

    private Source getSourceById(int sourceId) {
        // 根据 sourceId 从数据源中获取 Source 对象
        // 返回获取到的 Source 对象
        return null;
    }
}