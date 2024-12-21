package com.example.rsser.View.Index;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;
import com.example.rsser.Presenter.Index.Index;
import com.example.rsser.R;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

// 最终的Activity
public class IndexActivity extends BaseView implements MainViewInt {
    private Index indexPresenter;
    private MenuManager menuManager;
    private DrawerLayoutManager drawerLayoutManager;
    private IndexAdpter adapter;
    private TextView title;
    private List<IndexAdpter.Item> currentItems;
    private Source currentSource;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentItems != null && currentSource != null) {
            loadData(currentItems, "从缓存中读取", currentSource);
        } else {
            loadData();
        }
        initView();
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
        adapter = new IndexAdpter(this);
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
        // 初始化管理器`
        menuManager = new MenuManager(this);
        drawerLayoutManager = new DrawerLayoutManager(this);
        currentSource = new Source("","无内容","无内容","rss2.0",0,1);
        currentItems = new ArrayList<>();
        initPresenter();
        onFirstInitOverAll();
        initView();
        loadData();
    }

    private void initView() {
        title = findViewById(R.id.headerIndex);
        headerEvent();
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
        List<Source> sourceList = loadSources();
        List<Source> total = new ArrayList<>();
        total = sourceList;
        if (sourceList == null) {
            List<Source> sourceList_new = new ArrayList<>();
            sourceList_new.add(new Source("","无数据","无数据","1",12312312,1));
            total = sourceList_new;
        }
        drawerLayoutManager.setupDrawerLayout(getSid(),drawerLayout, recyclerView, total, new DrawerLayoutManager.onItemSelected() {
            @Override
            public void onItemSelected(int sid) {
                loadData(sid);
            }
        });
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
    public void loadData(int sid) {
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
    public void loadData(List<IndexAdpter.Item> itemList, String msg, Source source) {
        if (source != null) {
            title.setText(source.getTitle());
            title.setTag(source.getId());
        }
        RecyclerView recyclerView = findViewById(R.id.indexCycle);
        if (itemList != null && !itemList.isEmpty()) {
            adapter.clear();
            adapter.addAll(itemList);
            currentItems = itemList;
            currentSource = source;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "没有可显示的数据", Toast.LENGTH_LONG).show();
        }
        System.out.println(msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    // 加载所有订阅源
    public List<Source> loadSources() {
        // 通知presenter， 给我订阅源
        return indexPresenter.loadSources();
    }
    public void headerEvent() {
        // 主页 标题点击事件
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("确认")
                        .setMessage("设置为默认加载源") // 确认框的内容
                        .setPositiveButton("确定", (dialog, which) -> {
                            Toast.makeText(v.getContext(), "已确认", Toast.LENGTH_SHORT).show();
                            Object tagObject = title.getTag();
                            int sid = getSid();
                            if (tagObject instanceof Integer) {
                                sid = (Integer) tagObject;
                            } else {
                                Log.e("TAG", "Tag is not an Integer");
                            }
                            setSid(sid);
                            drawerLayoutManager.focusChange(getSid());
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            // 用户点击取消后的操作
                            dialog.dismiss(); // 关闭对话框
                        })
                        .show(); // 显示对话框

                return true; // 表示事件已处理
            }
        });
    }

    // 整个 APP 的启动工作，如： 创建默认类型文件夹,
    public void onFirstInitOverAll() {
        if (indexPresenter.isTypeEmpty()) {
            // 如果空，就创建默认的
            Type t = new Type("默认");
            indexPresenter.insertType(t);
        }
    }
}