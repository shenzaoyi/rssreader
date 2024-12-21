package com.example.rsser.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TypeDao {
    // 插入新的订阅源类型
    @Insert
    long insert(Type type);

    // 更新订阅源类型信息
    @Update
    int update(Type type);

    // 删除指定的订阅源类型
    @Delete
    int delete(Type type);

    // 根据 ID 删除订阅源类型
    @Query("DELETE FROM types WHERE id = :id")
    int deleteById(int id);

    // 查询所有的订阅源类型
    @Query("SELECT * FROM types")
    List<Type> getAllTypes();

    // 根据 ID 查询指定的订阅源类型
    @Query("SELECT * FROM types WHERE id = :id")
    Type getTypeById(int id);

    // 根据名称查询订阅源类型
    @Query("SELECT * FROM types WHERE name = :name LIMIT 1")
    Type getTypeByName(String name);

    // 获取 types 表的行数
    @Query("SELECT COUNT(*) FROM types")
    int getTableSize();
}