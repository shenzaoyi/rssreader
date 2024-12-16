package com.example.rsser.View.Index;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.Presenter.Index.Index;
import com.example.rsser.R;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

// 最终的Activity
public class IndexActivity extends BaseView implements MainViewInt {
    private Index indexPresenter;
    private MenuManager menuManager;
    private DrawerLayoutManager drawerLayoutManager;

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 添加这些行
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // 设置头部文字大小
        TextView textView = findViewById(R.id.headerIndex);
        textView.setTextSize(24);
//        // mock
//        List<Item> mockItems = Arrays.asList(
//                new Item(1, "Sample Item Title 1", "http://samplelink1.com", "Sample item description 1", "Sample item content 1", System.currentTimeMillis()),
//                new Item(1, "Sample Item Title 2", "http://samplelink2.com", "Sample item description 2", "Sample item content 2", System.currentTimeMillis()),
//                new Item(1, "Sample Item Title 3", "http://samplelink3.com", "Sample item description 3", "Sample item content 3", System.currentTimeMillis())
//        );
//        indexPresenter.tempSave(mockItems);
//        List<Source> mockSources = Arrays.asList(
//                new Source("https://techcrunch.com/feed", "TechCrunch", "Latest technology news and updates", "RSS", System.currentTimeMillis()),
//                new Source("http://feeds.bbci.co.uk/news/rss.xml", "BBC News", "Latest news from BBC", "RSS", System.currentTimeMillis()),
//                new Source("https://www.npr.org/rss/rss.php?id=1001", "NPR", "National Public Radio updates", "RSS", System.currentTimeMillis()),
//                new Source("https://news.ycombinator.com/rss", "Hacker News", "Tech and startup news", "RSS", System.currentTimeMillis()),
//                new Source("https://www.theverge.com/rss/index.xml", "The Verge", "Covers the intersection of technology, science, art, and culture", "RSS", System.currentTimeMillis())
//        );
//        indexPresenter.tempSaveS(mockSources);
        // 初始化管理器
        menuManager = new MenuManager(this);
        drawerLayoutManager = new DrawerLayoutManager(this);
        initPresenter();
        initView();
        loadData();
    }

    private void initView() {
        // 初始化视图组件
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 配置不显示这个鬼标题，显示会摆在中不中午不午的地方
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView recyclerView = findViewById(R.id.indexCycle);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // 设置RecyclerView布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        drawerLayoutManager.setupDrawerLayout(drawerLayout, recyclerView);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase("rss_db");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            View menuItemView = findViewById(R.id.action_add);
            menuManager.showAddOptionsMenu(menuItemView);
            return true;
        }

        if (id == android.R.id.home) {
            drawerLayoutManager.toggleDrawer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 加载数据的方法
    private void loadData() {
        int sid = getSid();
        if (sid < 0) return;

        long data = System.currentTimeMillis();
        // 获取100天前的数据
        data = data - 100L * 24 * 60 * 60 * 1000;
        indexPresenter.loadRecommendation(sid, data);
    }

    // SharedPreferences 相关方法
    private int getSid() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_info", MODE_PRIVATE);
        return sharedPreferences.getInt("sid", -1);
    }

    private void setSid(int sid) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("sid", sid);
        editor.apply();
    }

    // Presenter 相关方法
    @Override
    protected void initPresenter() {
        this.indexPresenter = new Index(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return indexPresenter;
    }

    // 加载数据到RecyclerView的方法
    @Override
    public void loadData(List<IndexAdpter.Item> itemList, String msg) {
        if (itemList != null && !itemList.isEmpty()) {
            RecyclerView recyclerView = findViewById(R.id.indexCycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            IndexAdpter adapter = new IndexAdpter(this, itemList);
            recyclerView.setAdapter(adapter);
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}