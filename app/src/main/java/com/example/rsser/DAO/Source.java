package com.example.rsser.DAO;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rss_source",
        foreignKeys = @ForeignKey(entity = Type.class,
                parentColumns = "id",
                childColumns = "typeId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("typeId")})
public class Source implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String url;
    private String title;
    private String description;
    private String feedType;
    @NonNull
    private long last_updated;
    private int typeId;

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }


    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }


    public String getUrl() {
        return url;
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

    public int getTypeId() {
        return typeId;
    }

    public Source(String url, String title, String description, String feedType, long last_updated, int typeId) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.feedType = feedType;
        this.last_updated = last_updated;
        this.typeId = typeId;
    }
}