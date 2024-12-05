package com.example.rsser.Model.Index;


import com.example.rsser.DAO.Item;

import java.util.List;

public interface IndexInterface {
    // 根据sid 获取主页推荐items
    public List<Item> getData(int sid);
}
