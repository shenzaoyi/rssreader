package com.example.rsser.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SourceDao {
    // 插入新的 RSS 源
    @Insert
    long insert(Source rssSource);

    // 删除指定的 RSS 源
    @Delete
    void delete(Source rssSource);

    // 查询所有的 RSS 源
    @Query("SELECT * FROM rss_source")
    List<Source> getAllSources();

    // 根据 ID 查询指定的 RSS 源
    @Query("SELECT * FROM rss_source WHERE id = :id")
    Source getSourceById(int id);

    // 检查是否存在指定sid且last_updated>指定日期
    @Query("SELECT * FROM rss_source WHERE id = :sid AND last_updated > :date")
    List<Source> getSouBySidData(int sid, long date);
    @Query("SELECT COUNT(*) FROM rss_source")
    int getTableSize();
    @Query("SELECT * FROM rss_source LIMIT 1")
    Source getFirstSource();
}
