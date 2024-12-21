package com.example.rsser.View.RssManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;
import com.example.rsser.R;

import java.util.ArrayList;
import java.util.List;

public class SourceListActivity extends AppCompatActivity {
    private TextView textViewTypeName;
    private Button buttonAddSource;
    private RecyclerView recyclerViewSource;
    private SourceAdapter sourceAdapter;
    private List<Source> sourceList;
    private int typeId;
    private Respositories resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);

        textViewTypeName = findViewById(R.id.textViewTypeName);
        buttonAddSource = findViewById(R.id.buttonAddSource);
        recyclerViewSource = findViewById(R.id.recyclerViewSource);
        resp = new Respositories(this.getApplication());

        typeId = getIntent().getIntExtra("typeId", -1);
        if (typeId == -1) {
            finish();
            return;
        }

        loadSourcesByType(typeId);
        setupSourceList();
        setupAddSourceButton();
    }

    private void loadSourcesByType(int typeId) {
        // 从数据源中加载指定类型的所有订阅源
        // 将结果赋值给 sourceList 变量
        sourceList = new ArrayList<>();
        // TODO: 实现加载订阅源的逻辑
    }

    private void setupSourceList() {
        sourceAdapter = new SourceAdapter(sourceList);
        sourceAdapter.setOnItemClickListener(source -> {
            // 打开订阅源详情页面
            Intent intent = new Intent(SourceListActivity.this, SourceDetailActivity.class);
            intent.putExtra("sourceId", source.getId());
            startActivity(intent);
        });
        sourceAdapter.setOnItemLongClickListener(source -> {
            // 弹出菜单,供用户进行各种操作
            showSourceOptionMenu(source);
        });
        recyclerViewSource.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSource.setAdapter(sourceAdapter);

        Type type = getTypeById(typeId);
        if (type != null) {
            textViewTypeName.setText(type.getName());
        }
    }

    private void setupAddSourceButton() {
        buttonAddSource.setOnClickListener(v -> {
            // 打开添加订阅源的对话框
            showAddSourceDialog();
        });
    }

    private void showSourceOptionMenu(Source source) {
        PopupMenu popupMenu = new PopupMenu(this, recyclerViewSource);
        popupMenu.inflate(R.menu.menu_source_option);
        popupMenu.setOnMenuItemClickListener(item -> {
            return false;
        });
        popupMenu.show();
    }

    private void showAddSourceDialog() {
        // 显示添加订阅源的对话框
        // 获取用户输入的订阅源信息
        // 保存新的订阅源到数据源中
        // 更新 adapter 和 UI
    }

    private void showEditSourceDialog(Source source) {
        // 显示编辑订阅源的对话框
        // 获取用户修改的订阅源信息
        // 更新数据源中的订阅源信息
        // 更新 adapter 和 UI
    }

    private void deleteSource(Source source) {
        // 从数据源中删除该订阅源
        // 从 adapter 中移除该订阅源
        // 更新 UI
    }

    private Type getTypeById(int typeId) {
        // 根据 typeId 从数据源中获取 Type 对象
        // 返回获取到的 Type 对象
        return null;
    }
}