package com.example.rsser.View.RssManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;
import com.example.rsser.Presenter.RssManager.RssManPresenter;
import com.example.rsser.R;
import com.example.rsser.Utils.UtilAPI;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRssActivity extends BaseView {

    private EditText etRssLink;
    private TextView tvTitle;
    private TextView tvDescription;
    private Button btnParse;
    private Button btnAdd;
    private ProgressBar progressBar;
    private RssManPresenter rssManPresenter;
    private Source source;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rss);
        items = new ArrayList<>();

        etRssLink = findViewById(R.id.et_rss_link);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        btnParse = findViewById(R.id.btn_parse);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_bar);

        initPresenter();
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etRssLink.getText().toString();
                Toast.makeText(AddRssActivity.this, "解析中", Toast.LENGTH_SHORT).show();
                fetchRssInBackground(url);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSource();
            }
        });
    }
    private void fetchRssInBackground(String url) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        progressBar.setVisibility(View.VISIBLE);
        executor.execute(() -> {
            try {
                Long start = System.currentTimeMillis();
                UtilAPI utilAPI = new UtilAPI();
                utilAPI.ParseXml(url);
                Long end = System.currentTimeMillis();
                System.out.println("解析耗时： " + (end - start));
                handler.post(() -> {
                    try {
                        // UI 线程更新
                        Map<String, String> sources = utilAPI.getSourceInfo();

                        if (sources != null && !sources.isEmpty()) {
                            String description = sources.getOrDefault("description", "无描述");
                            tvDescription.setText(description);
                            String title = sources.getOrDefault("title", "无标题");
                            tvTitle.setText(title);
                            Long data = System.currentTimeMillis();
                            source = new Source(url, title, description, "rss2.0", data);
                        } else {
                            Toast.makeText(this, "解析RSS源失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "更新UI时出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddRssActivity", "UI Update Error", e);
                    }
                });
                handler.post(()->{
                    try {
                        List<Map<String, String>> itemInfos = utilAPI.getItemInfo();
                        for (Map<String, String> itemInfo : itemInfos) {
                            String link = itemInfo.getOrDefault("link", "null");
                            String title = itemInfo.getOrDefault("title", "title");
                            String description = itemInfo.getOrDefault("description", "null");
                            Item i = new Item();
                            i.setTitle(title);
                            i.setLink(link);
                            i.setDescription(description);
                            items.add(i);
                        }
                    }catch (Exception e) {
                        Toast.makeText(this, "获取Items时出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddRssActivity", "Items Fetch Error", e);
                    }
                });
            } catch (Exception e) {
                handler.post(() -> {
                    Toast.makeText(this, "获取RSS源失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AddRssActivity", "Fetch Error", e);
                });
            } finally {
                handler.post(() -> progressBar.setVisibility(View.GONE));
            }
        });
    }
    public void addSource() {
        rssManPresenter.saveSource(source, items);
    }

    @Override
    protected void initPresenter() {
        this.rssManPresenter = new RssManPresenter(AddRssActivity.this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return rssManPresenter;
    }
    public void onSuccess(){
        System.out.println("添加成功");
        Toast.makeText(this, "添加成功!", Toast.LENGTH_LONG).show();
    }
}