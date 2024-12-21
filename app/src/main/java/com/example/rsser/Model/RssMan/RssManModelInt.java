package com.example.rsser.Model.RssMan;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;

import java.util.List;

public interface RssManModelInt {
    Long SaveSource(Source source);
    void saveItems(List<Item> items);
    List<Type> loadType();
    long addType(Type newType);
}
