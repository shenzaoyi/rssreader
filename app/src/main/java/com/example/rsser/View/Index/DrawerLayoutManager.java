package com.example.rsser.View.Index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsser.DAO.Source;
import com.example.rsser.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class DrawerLayoutManager extends BaseIndexManager {
    private DrawerLayout drawerLayout;
    private float threshold;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private AppCompatActivity activity;
    public interface onItemSelected {
        void onItemSelected(int sid);
    }
    private onItemSelected onItemSelectedListener;

    public DrawerLayoutManager(Context context) {
        super(context);
        this.activity = (AppCompatActivity) context;
        this.threshold = calculateThreshold(context);
    }

    public void setupDrawerLayout(DrawerLayout drawerLayout, RecyclerView recyclerView, List<Source> sourceList, onItemSelected onItemSelectedListener) {
        this.drawerLayout = drawerLayout;
        this.recyclerView = recyclerView;
        this.navigationView = drawerLayout.findViewById(R.id.nav_view);
        this.onItemSelectedListener = onItemSelectedListener;

        // 设置菜单项点击事件
        setupNavigationItemListener(sourceList);

        // 设置支持 ActionBar
        setupActionBar();

        // 设置 RecyclerView 布局管理器
        setupRecyclerViewLayout();

        // 设置侧滑监听器的逻辑
        setupDrawerTouchListener();

        // 设置抽屉布局触摸监听
        setupDrawerLayoutTouchListener();

        // 设置 ActionBarDrawerToggle
        setupActionBarDrawerToggle();
    }
    // 菜单项点击事件
    private void setupNavigationItemListener(List<Source> sourceList) {
        navigationView.getMenu().clear();
        for (Source s : sourceList) {
            MenuItem item = navigationView.getMenu().add(0, s.getId(), 0, s.getTitle());
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    onItemSelectedListener.onItemSelected(item.getItemId());
                    return true;
            }
        });
    }

    private void setupActionBar() {
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupRecyclerViewLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDrawerTouchListener() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                    startY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float diffX = event.getX() - startX;
                    float diffY = event.getY() - startY;
                    if (diffX > diffY && diffX > threshold) {
                        drawerLayout.openDrawer(GravityCompat.START);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDrawerLayoutTouchListener() {
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                    startY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float diffX = event.getX() - startX;
                    float diffY = event.getY() - startY;
                    if (diffX > diffY && diffX > threshold) {
                        drawerLayout.openDrawer(GravityCompat.START);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void setupActionBarDrawerToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // 辅助方法：根据图标名称获取 Drawable 资源
    private int getDrawableResource(String iconName) {
        return activity.getResources().getIdentifier(iconName, "drawable", activity.getPackageName());
    }

}