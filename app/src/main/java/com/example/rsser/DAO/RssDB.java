package com.example.rsser.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Item.class, Source.class, Type.class}, version = 1, exportSchema = false)
public abstract class RssDB extends RoomDatabase {
    public static synchronized RssDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RssDB.class, "rss_db")
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
    public abstract SourceDao sourceDao();
    public abstract TypeDao typeDao();

    private static RssDB instance;
}