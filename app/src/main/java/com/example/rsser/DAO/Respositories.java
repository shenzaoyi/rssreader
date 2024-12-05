package com.example.rsser.DAO;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Respositories {
    private SourceDao sourceDao;
    private ItemDao itemDao;
    public Respositories(Application application) {
        RssDB rssDB = RssDB.getInstance(application);
        sourceDao = rssDB.sourceDao();
        itemDao = rssDB.itemDao();
    }
    // Item Operation
    public boolean insert(Item item) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            itemDao.insert(item);
        });
        return true;
    }
    // Get all items with sid
    public void getItemsBySid(int sid, Callback callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            List<Item> items = itemDao.getItemsBySourceId(sid);
            callback.onItemsLoaded(items);
        });
    }
}
