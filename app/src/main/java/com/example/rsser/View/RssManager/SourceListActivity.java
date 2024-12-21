package com.example.rsser.View.RssManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;
import com.example.rsser.R;
import com.example.rsser.Utils.QRUtil;
import com.example.rsser.Utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class SourceListActivity extends AppCompatActivity {
    private TextView textViewTypeName;
    private Button buttonAddSource;
    private RecyclerView recyclerViewSource;
    private SourceAdapter sourceAdapter;
    private List<Source> sourceList;
    private int typeId;
    private Respositories resp;
    private static final long CLICK_TIME_THRESHOLD = 1500; // 1.5毫秒
    private int clickCount = 0;
    private long lastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);

        textViewTypeName = findViewById(R.id.textViewTypeName);
        buttonAddSource = findViewById(R.id.buttonAddSource);
        recyclerViewSource = findViewById(R.id.recyclerViewSource);
        sourceList = new ArrayList<>();
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
        Future<List<Source>> f = resp.getSourceByTid(typeId);
        try {
            sourceList = f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSourceList() {
        sourceAdapter = new SourceAdapter(sourceList);
        sourceAdapter.setOnItemClickListener((view, source) -> {
            // 打开订阅源详情页面
            Intent intent = new Intent(SourceListActivity.this, SourceDetailActivity.class);
            intent.putExtra("sourceId", source.getId());
            startActivity(intent);
        });
        sourceAdapter.setOnItemLongClickListener((view, source) -> {
            // 弹出菜单,供用户进行各种操作
            showSourceOptionMenu(view, source);
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
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < CLICK_TIME_THRESHOLD) {
                clickCount++; // 连续点击，增加计数
            } else {
                clickCount = 1; // 重置计数
            }
            System.out.println(clickCount);
            lastClickTime = currentTime; // 更新最后点击时间
            if (clickCount == 1) {
                Toast.makeText(this, "都说了先别点!", Toast.LENGTH_SHORT).show();
            }
            if (clickCount > 5) {
                Toast.makeText(this, "月落乌啼霜满天，千里江陵一日还", Toast.LENGTH_SHORT).show();
            }
//            showAddSourceDialog();
        });
    }

    private void showSourceOptionMenu(View itemView, Source source) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.menu_type_option, null);

        // 设置弹出菜单的点击事件
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupView.findViewById(R.id.action_edit).setOnClickListener(v -> {
            showEditSourceDialog(source);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.action_delete).setOnClickListener(v -> {
            deleteSource(source);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.action_share).setOnClickListener(v -> {
            shareSource(source);
            popupWindow.dismiss();
        });

        // 计算弹出菜单在 item 视图下方的位置
        int[] location = new int[2];
        itemView.getLocationOnScreen(location);
        int itemViewX = location[0];
        int itemViewY = location[1] + itemView.getHeight();

        // 测量弹出菜单的大小
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int menuWidth = popupView.getMeasuredWidth();
        int menuHeight = popupView.getMeasuredHeight();

        // 获取屏幕尺寸
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // 确保弹出菜单不会超出屏幕边界
        int x = Math.max(0, Math.min(screenWidth - menuWidth, itemViewX));
        int y = Math.max(0, Math.min(screenHeight - menuHeight, itemViewY));

        // 显示弹出菜单
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, x, y);
    }

    private void showAddSourceDialog() {
        // 创建并显示添加订阅源的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_source, null);

        EditText editTextUrl = dialogView.findViewById(R.id.editTextUrl);
        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextFeedType = dialogView.findViewById(R.id.editTextFeedType);

        builder.setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // 获取用户输入的订阅源信息
                    String url = editTextUrl.getText().toString();
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    String feedType = editTextFeedType.getText().toString();

                    // 创建新的 Source 对象并保存到数据源中
                    Source newSource = new Source(url, title, description, feedType, System.currentTimeMillis(), typeId);
                    resp.insertSou(newSource);

                    // 更新 adapter 和 UI
                    sourceList.add(newSource);
                    sourceAdapter.notifyItemInserted(sourceList.size() - 1);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // 取消操作
                })
                .show();
    }

    private void showEditSourceDialog(Source source) {
        // 创建并显示编辑订阅源的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_source, null);

        TextView editTextUrl = dialogView.findViewById(R.id.editTextUrl);
        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        TextView editTextFeedType = dialogView.findViewById(R.id.editTextFeedType);

        editTextUrl.setText(source.getUrl());
        editTextTitle.setText(source.getTitle());
        editTextDescription.setText(source.getDescription());
        editTextFeedType.setText(source.getFeedType());

        builder.setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // 获取用户修改的订阅源信息
                    String url = editTextUrl.getText().toString();
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    String feedType = editTextFeedType.getText().toString();

                    // 更新数据源中的订阅源信息
                    source.setUrl(url);
                    source.setTitle(title);
                    source.setDescription(description);
                    source.setFeedType(feedType);
                    resp.updateSource(source);

                    // 更新 adapter 和 UI
                    int position = sourceList.indexOf(source);
                    sourceList.set(position, source);
                    sourceAdapter.notifyItemChanged(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // 取消操作
                })
                .show();
    }

    private void deleteSource(Source source) {
        // 从数据源中删除该订阅源
        resp.deleteSou(source);

        // 从 adapter 中移除该订阅源
        int position = sourceList.indexOf(source);
        sourceList.remove(position);
        sourceAdapter.notifyItemRemoved(position);
    }
    public void shareSource(Source source) {
        // 创建分享选项对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("分享订阅源");
        builder.setItems(new CharSequence[]{"文本分享", "二维码分享"}, (dialog, which) -> {
            ShareUtil shareUtil = new ShareUtil(this);
            switch (which) {
                case 0:
                    shareUtil.shareRSSSource(source);
                    break;
                case 1:
                    shareUtil.shareRSSSourceQRCode(source);
                    break;
            }
        });
        builder.show();
    }
    private Type getTypeById(int typeId) {
        // 根据 typeId 从数据源中获取 Type 对象
        Future<Type> f = resp.getTypeById(typeId);
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}