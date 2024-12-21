package com.example.rsser.Presenter.RssManager;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;

import java.util.List;

public interface RssManPreInt {
    void saveSource(Source source,  List<Item> items);
    List<Type> loadType();
    long addType(Type t);
}
