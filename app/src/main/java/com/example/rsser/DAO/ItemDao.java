package com.example.rsser.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    // 插入新的 RSS 项目
    @Insert
    long insert(Item rssItem);

    // 删除指定的 RSS 项目
    @Delete
    void delete(Item rssItem);

    // 查询所有的 RSS 项目
    @Query("SELECT * FROM rss_item")
    List<Item> getAllItems();

    // 根据源 ID 查询指定的 RSS 项目
    @Query("SELECT * FROM rss_item WHERE sourceId = :sourceId")
    List<Item> getItemsBySourceId(int sourceId);

    // 根据 ID 查询指定的 RSS 项目
    @Query("SELECT * FROM rss_item WHERE id = :id")
    Item getItemById(int id);
    @Query("DELETE FROM rss_item WHERE sourceId = :sourceId")
    void deleteItemsBySourceId(int sourceId);
}
