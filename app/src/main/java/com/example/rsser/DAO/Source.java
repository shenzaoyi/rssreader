package com.example.rsser.DAO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "rss_source")
public class Source implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }

    public Source(String url, String title, String description, String feedType, long last_updated) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.feedType = feedType;
        this.last_updated = last_updated;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFeedType() {
        return feedType;
    }

    public long getLast_updated() {
        return last_updated;
    }

    private String title;
    private String description;
    private String feedType;
    private long last_updated;
}
