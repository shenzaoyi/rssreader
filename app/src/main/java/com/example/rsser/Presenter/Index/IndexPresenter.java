package com.example.rsser.Presenter.Index;

import com.example.rsser.base.BasePresenter;
// 此处没有继续继承
public interface IndexPresenter  {
    // 根据sid， 从Model获取用户首页加载的数据
    // sid 是用户定义的默认显示的订阅源
    public void getdata(int sid);
}