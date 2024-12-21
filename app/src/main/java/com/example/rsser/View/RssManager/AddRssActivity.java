package com.example.rsser.View.RssManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;
import com.example.rsser.Presenter.RssManager.RssManPresenter;
import com.example.rsser.R;
import com.example.rsser.Utils.UtilAPI;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRssActivity extends BaseView {

    private EditText etRssLink;
    private EditText tvTitle;
    private EditText tvDescription;
    private Button btnParse, btnAdd, btnSelectType;
    private ProgressBar progressBar;
    private RssManPresenter rssManPresenter;
    private Source source;
    private List<Item> items;
    private List<Type> typeList; // 保存所有类型
    private String selectedType;
    private TypeAdapter adapter;
    private int tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rss);
        items = new CopyOnWriteArrayList<>();

        etRssLink = findViewById(R.id.et_rss_link);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        btnParse = findViewById(R.id.btn_parse);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_bar);
        btnSelectType = findViewById(R.id.btn_select_type);

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
                // 如果存在
                if (rssManPresenter.isExist(source.getUrl())) {
                    Toast.makeText(AddRssActivity.this, "此订阅源已经存在",Toast.LENGTH_LONG).show();
                    onFail();
                }else{
                    source.setTitle(tvTitle.getText().toString());
                    source.setDescription(tvDescription.getText().toString());
                    addSource();
                }
            }
        });
        manageType();
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
                            source = new Source(url, title, description, "rss2.0", data,-1);
                            source.setUrl(url);
                            source.setTitle(title);
                            source.setDescription(description);
                            source.setFeedType("rss 2.0");
                            source.setLast_updated(data);
                            System.out.println("source tid is : " + source.getId());
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
        try {
            List<Item> itemlist = items;
            rssManPresenter.saveSource(source, itemlist); // 添加成功的处理
            onSuccess();
        } catch (Exception e) {
            // 添加失败的处理
            onFail();
        }
    }
    private void onFail() {
        Toast.makeText(this, "添加失败, 请检查输入", Toast.LENGTH_LONG).show();
        // 清空输入框和相关数据
        clearInputs();
    }
    private void clearInputs() {
        etRssLink.setText("");
        tvTitle.setText("");
        tvDescription.setText("");
        items.clear();
        source = null;
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
        clearInputs();
    }
    public void manageType() {
        typeList = new ArrayList<>();
        typeList = rssManPresenter.loadType();
        selectedType = typeList.get(0).getName();

        // 设置 "选择类型" 按钮点击事件
        btnSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeSelectionDialog(); // 显示类型选择卡片
            }
        });
    }
    private void showTypeSelectionDialog() {
        // 创建BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_type, null);
        bottomSheetDialog.setContentView(dialogView);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        Button btnNewType = dialogView.findViewById(R.id.btn_new_type);

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TypeAdapter(typeList, type -> {
            // 点击某个类型时的回调
            Type t = type; // 更新选中的类型
            tid = t.getId();
            if (source == null) {
                Toast.makeText(this, "请首先解析订阅源", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss(); // 关闭对话框
                return;
            }
            source.setTypeId(tid);
            Toast.makeText(this, "选择了类型：" + type.getId() + " " + type.getName(), Toast.LENGTH_SHORT).show();
            btnSelectType.setText(t.getName());
            bottomSheetDialog.dismiss(); // 关闭对话框
        });
        recyclerView.setAdapter(adapter);

        // 新建类型的按钮点击事件
        btnNewType.setOnClickListener(v -> {
            handleAddNewType();
        });

        // 显示对话框
        bottomSheetDialog.show();
    }
    private void handleAddNewType() {
        // 创建 AlertDialog 让用户输入新类型名称
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建类型");

        // 加载自定义布局
        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_add_type, null);
        EditText editTextTypeName = customView.findViewById(R.id.editTextTypeName);
        EditText editTextTypeDesc = customView.findViewById(R.id.editTextTypeDesc);
        Button buttonCancel = customView.findViewById(R.id.buttonCancel);
        Button buttonSave = customView.findViewById(R.id.buttonSave);

        builder.setView(customView);
        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        buttonSave.setOnClickListener(v -> {
            String newTypeName = editTextTypeName.getText().toString().trim();
            String newTypeDesc = editTextTypeDesc.getText().toString().trim();
            Type newType = new Type(newTypeName);
            newType.setDescription(newTypeDesc);

            // 保存新类型
            long id = rssManPresenter.addType(newType);
            newType.setId((int) id);

            if (!newTypeName.isEmpty()) {
                // 将新类型添加到列表中
                typeList.add(newType);
                adapter.notifyItemInserted(typeList.size() - 1);
                selectedType = newType.getName(); // 选择新建的类型
                Toast.makeText(this, "已添加新类型：" + newType, Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 关闭对话框
            } else {
                Toast.makeText(this, "请输入类型名称", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}