package com.example.rsser.Model.RssMan;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;

import java.util.List;

public interface RssManModelInt {
    public Long SaveSource(Source source);
    public void saveItems(List<Item> items);
}
