package com.example.rsser.View.Index;

import com.example.rsser.DAO.Source;

import java.util.List;

public interface MainViewInt {
    public void loadData(List<IndexAdpter.Item> itemList, String msg, Source source);
}
