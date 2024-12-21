package com.example.rsser.DAO;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "types")
public class Type implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;

    public int getNum() {
        return num;
    }

    @ColumnInfo(name = "num")
    private int num;

    public String getDescription() {
        return description;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type(String name) {
        this.name = name;
    }

    // 构造函数、getter和setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}