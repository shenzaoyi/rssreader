package com.example.rsser.View.Index;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.rsser.R;
import com.example.rsser.View.RssManager.AddRssActivity; // 导入添加RSS的Activity
import com.example.rsser.View.RssManager.RssTypeAct;

// 抽取菜单管理
public class MenuManager extends BaseIndexManager {
    private PopupMenu popupMenu;

    public MenuManager(Context context) {
        super(context);
    }

    public void showAddOptionsMenu(View anchorView) {
        popupMenu = new PopupMenu(context, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_add_source) {
                    handleAddSource();
                    return true;
                } else if (itemId == R.id.action_manage_sources) {
                    handleManageSources();
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    private void handleAddSource() {
        // 跳转到添加RSS的Activity
        Intent intent = new Intent(context, AddRssActivity.class);
        context.startActivity(intent);
    }

    private void handleManageSources() {
        Intent intent = new Intent(context, RssTypeAct.class);
        context.startActivity(intent);
    }
}