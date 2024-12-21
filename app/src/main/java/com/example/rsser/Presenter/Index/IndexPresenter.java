package com.example.rsser.Presenter.Index;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;
import com.example.rsser.base.BasePresenter;

import java.util.List;

// 此处没有继续继承
public interface IndexPresenter  {
    // 根据sid， 从Model获取用户首页加载的数据
    // sid 是用户定义的默认显示的订阅源
    public void loadData(List<Item> res, Source source);
    public void tempSave(List<Item> items);
    public List<Source> loadSources();
}
