package com.example.rsser.Model.Index;


import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Source;

import java.sql.SQLOutput;
import java.util.List;

public interface IndexInterface {
    // 根据sid 获取主页推荐items
    List<Item> getItemBySid(int sid);
    void tempsave(List<Item> items);
    void tempsaveS(List<Source> sources);

    List<Source> GetSourcesBySidData(int sid, long data);

    boolean isSourExist(int sid);
    Source getFirstSource();
    Source getSourceById(int sid);
    List<Source> getAllSources();
}
