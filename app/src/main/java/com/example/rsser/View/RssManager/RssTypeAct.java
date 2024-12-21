package com.example.rsser.View.RssManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Type;
import com.example.rsser.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// 这种不用MVP， 整个项目是否该用MVP，待重构，，，
public class RssTypeAct extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubscriptionAdapter adapter;
    private Respositories resp;
    private List<Type> typeList;
    private Button addTypeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subscription);
        resp = new Respositories(getApplication());

        recyclerView = findViewById(R.id.recyclerView_type);
        addTypeButton = findViewById(R.id.addTypeButton);
        typeList = new ArrayList<>();
        loadRssType();
        typeManager();
    }

    private void typeManager() {
        adapter = new SubscriptionAdapter(typeList);
        adapter.setOnItemClickListener(new SubscriptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Type type) {
                Intent intent = new Intent(RssTypeAct.this, SourceListActivity.class);
                intent.putExtra("typeId", type.getId());
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener((view, type) -> {
            // 弹出菜单,供用户进行各种操作
            showTypeOptionMenu(view, type);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        addTypeButton.setOnClickListener(v -> showAddTypeDialog());
    }

    private void showAddTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_type, null);
        EditText editTextTypeName = dialogView.findViewById(R.id.editTextTypeName);
        EditText editTextTypeDesc = dialogView.findViewById(R.id.editTextTypeDesc);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        buttonSave.setOnClickListener(v -> {
            String typeName = editTextTypeName.getText().toString().trim();
            String desc = editTextTypeDesc.getText().toString().trim();
            if (!typeName.isEmpty()) {
                Type t = new Type(typeName);
                t.setDescription(desc);
                long id =  addNewType(t);
                t.setId((int)id);
                t.setNum(0);
                adapter.addType(t);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "请输入类型名称", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void showTypeOptionMenu(View itemView, Type type) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.menu_type_option, null);

        // 设置弹出菜单的点击事件
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupView.findViewById(R.id.action_edit).setOnClickListener(v -> {
            showEditTypeDialog(type);
            popupWindow.dismiss();
        });
        popupView.findViewById(R.id.action_delete).setOnClickListener(v -> {
            deleteType(type);
            popupWindow.dismiss();
        });
        popupView.findViewById(R.id.action_share).setOnClickListener(v -> {
            shareType(type);
            popupWindow.dismiss();
        });

        // 计算弹出菜单在 item 视图下方的位置
        int[] location = new int[2];
        itemView.getLocationOnScreen(location);
        int itemViewX = location[0];
        int itemViewY = location[1] + itemView.getHeight();
        int menuWidth = popupView.getMeasuredWidth();
        int menuHeight = popupView.getMeasuredHeight();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // 确保弹出菜单不会超出屏幕边界
        int x = Math.max(0, Math.min(screenWidth - menuWidth, itemViewX));
        int y = Math.max(0, Math.min(screenHeight - menuHeight, itemViewY));

        // 显示弹出菜单
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, x, y);
    }

    private void shareType(Type type) {
    }

    private void deleteType(Type type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除订阅源类型");
        builder.setMessage("确定要删除该订阅源类型及其下所有订阅源吗?");
        builder.setPositiveButton("确定", (dialog, which) -> {
            resp.deleteType(type); // 从数据库中删除该类型
            resp.deleteSubscriptionsByType(type.getId()); // 删除该类型下的所有订阅源
            adapter.notifyItemRemoved(typeList.indexOf(type));
            typeList.remove(type);
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showEditTypeDialog(Type type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_type, null);
        EditText editTextTypeName = dialogView.findViewById(R.id.editTextTypeName);
        EditText editTextTypeDesc = dialogView.findViewById(R.id.editTextTypeDesc);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        String oldName = type.getName();
        int oldId = type.getId();
        if (oldName.equals("默认")){
            editTextTypeName.setFocusable(false);
            editTextTypeName.setClickable(false);
            Toast.makeText(this,"默认类型无法更改名称",Toast.LENGTH_SHORT).show();
        }
        editTextTypeName.setText(oldName);
        editTextTypeName.setTag(type.getId());
        editTextTypeDesc.setText(type.getDescription());

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        buttonSave.setOnClickListener(v -> {
            String newTypeName = editTextTypeName.getText().toString().trim();
            String newTypeDesc = editTextTypeDesc.getText().toString().trim();
            if (!newTypeName.isEmpty()) {
                Future<Type> f = resp.getTypeByName(newTypeName);
                try {
                    Type t = f.get();
                    if (t != null && t.getId() != oldId){
                        // 如果名称相等，即只是修改备注，允许的
                        Toast.makeText(this, "此文件夹已经存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                type.setName(newTypeName);
                type.setDescription(newTypeDesc);
                int k = new Integer(editTextTypeName.getTag().toString());
                type.setId(k);
                resp.updateType(type); // 更新数据库中的类型信息
                adapter.notifyItemChanged(typeList.indexOf(type)); // 更新列表项
                dialog.dismiss();
            } else {
                Toast.makeText(this, "请输入类型名称", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private long addNewType(Type t) {
        Future<Long> f = resp.insertType(t);
        long id = -1;
        try {
            id = f.get();
            if (id == -1) {
                System.out.println("Save Type Error");
            }
            return id;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadRssType() {
        Future<List<Type>> f = resp.getAllTypes();
        try {
            typeList = f.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}