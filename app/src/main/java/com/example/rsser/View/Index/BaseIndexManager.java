package com.example.rsser.View.Index;

import android.content.Context;
import android.util.DisplayMetrics;

// 创建一个基础管理类  
public abstract class BaseIndexManager {
    protected Context context;

    public BaseIndexManager(Context context) {
        this.context = context;
    }

    // 通用的工具方法  
    protected float calculateThreshold(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        return screenWidth * 0.3f;
    }
}
