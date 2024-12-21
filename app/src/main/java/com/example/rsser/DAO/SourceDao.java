package com.example.rsser.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SourceDao {
    // 插入新的 RSS 源
    @Insert
    long insert(Source source);

    // 更新 RSS 源信息
    @Update
    int update(Source source);

    // 删除指定的 RSS 源
    @Delete
    int delete(Source source);

    // 根据 ID 删除 RSS 源
    @Query("DELETE FROM rss_source WHERE id = :id")
    int deleteById(int id);

    // 根据类型 ID 删除 RSS 源
    @Query("DELETE FROM rss_source WHERE typeId = :typeId")
    int deleteByTypeId(int typeId);

    // 查询所有的 RSS 源
    @Query("SELECT * FROM rss_source")
    List<Source> getAllSources();

    // 根据类型 ID 查询 RSS 源
    @Query("SELECT * FROM rss_source WHERE typeId = :typeId")
    List<Source> getSourcesByTypeId(int typeId);

    // 根据 ID 查询指定的 RSS 源
    @Query("SELECT * FROM rss_source WHERE id = :id")
    Source getSourceById(int id);

    // 检查是否存在指定 id 且 last_updated > 指定日期的 RSS 源
    @Query("SELECT * FROM rss_source WHERE id = :id AND last_updated > :date")
    List<Source> getSourcesByIdAndDate(int id, long date);

    // 获取 rss_source 表的行数
    @Query("SELECT COUNT(*) FROM rss_source")
    int getTableSize();

    // 获取 rss_source 表的第一条记录
    @Query("SELECT * FROM rss_source LIMIT 1")
    Source getFirstSource();
}