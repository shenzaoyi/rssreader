package com.example.rsser.base;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rsser.Contract.Contract;

// BaseView 的实现：
public abstract class BaseView extends AppCompatActivity implements Contract.View {

    // 这两方法交给子类自己实现
    protected abstract void initPresenter();
    protected abstract BasePresenter getPresenter();
}
