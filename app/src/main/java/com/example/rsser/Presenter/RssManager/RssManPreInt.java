package com.example.rsser.Presenter.RssManager;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;

import java.util.List;

public interface RssManPreInt {
    public void saveSource(Source source,  List<Item> items);
}
