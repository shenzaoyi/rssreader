package com.example.rsser.DAO;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rss_item",
    foreignKeys = @ForeignKey(
            entity = Source.class,
            parentColumns = "id",
            childColumns = "sourceId",
            onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "sourceId")}
)
public class Item implements Serializable {
    public int getId() {
        return id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPubdate(long pubdate) {
        this.pubdate = pubdate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public long getPubdate() {
        return pubdate;
    }

    public String getLink() {
        return link;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    // 外键怎么写
    private int sourceId;
    private String title;
    private String link;
    private String description;
    private String content;
    private long pubdate;
}