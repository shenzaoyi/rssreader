package com.example.rsser.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class, Source.class}, version = 1)
public abstract class RssDB extends RoomDatabase {
    public abstract ItemDao itemDao();
    public abstract SourceDao sourceDao();

    private static RssDB instance;
    public static synchronized RssDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),RssDB.class,"rss_db").build();
        }
        return instance;
    }
}