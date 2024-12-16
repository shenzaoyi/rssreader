package com.example.rsser.DAO;

import android.app.Application;
import android.content.Context;
import android.telecom.Call;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Respositories {
    private SourceDao sourceDao;
    private ItemDao itemDao;
    private ExecutorService executorService; // 整他一个线程池， 吼
    public Respositories(Application application) {
        RssDB rssDB = RssDB.getInstance(application);
        sourceDao = rssDB.sourceDao();
        itemDao = rssDB.itemDao();
        int nthread = loadThreadPoolSize(application);
        executorService = Executors.newFixedThreadPool(nthread);
    }
    private int loadThreadPoolSize(Context context) {
        Properties properties = new Properties();
        try (InputStream input = context.getAssets().open("config.properties")) {
            // 加载属性文件
            properties.load(input);
            // 获取线程池大小
            String threadPoolSizeStr = properties.getProperty("thread.pool.size");
            return Integer.parseInt(threadPoolSizeStr);
        } catch (IOException e) {
            e.printStackTrace();
            return 4; // 默认线程数
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 4; // 出现异常时返回默认线程数
        }
    }
// ITEM OPERATION
    public boolean insert(Item item) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            itemDao.insert(item);
        });
        return true;
    }
    public boolean insertItems(List<Item> items) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            for (Item item : items) {
                itemDao.insert(item);
            }
        });
        return true;
    }
    // Get all items with sid
    public void getItemsBySid(int sid, Callback.onItemsLoaded callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            List<Item> items = itemDao.getItemsBySourceId(sid);
            callback.onItemsLoaded(items);
        });
    }
    // Save Items In Array
    public void saveItemInArr(List<Item> items) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            for (int i = 0; i < items.size(); i++) {
                itemDao.insert(items.get(i));
            }
        });
    }

// SOURCE OPERATION
    // Save Sources In Array
    public void saveSourceArr(List<Source> sources) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            for (int i = 0; i < sources.size(); i++) {
                sourceDao.insert(sources.get(i));
            }
        });
    }

    // check if sid exist
    public void checkSidSou(int sid, long data, Callback.onSourceLoaded callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            System.out.println(sid);
            System.out.println(data);
            List<Source> sources = sourceDao.getSouBySidData(sid, data);
            callback.onSourceLoaded(sources);
        });
    }
    public boolean isTableEmpty() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<Boolean> future = executorService.submit(() -> {
                int recordCount = sourceDao.getTableSize();
                return recordCount == 0;
            });
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return true; // 出现异常时,默认返回表为空
        } finally {
            executorService.shutdown();
        }
    }
    public void isSidExists(int sid, Callback.onSourceExist callback) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean exists = sourceDao.getSourceById(sid) != null;
            callback.onSourceExist(exists);
        });
    }
    public Future<Source> GetFirstSource() {
        return executorService.submit(() -> {
            return sourceDao.getFirstSource();
        });
    }
    public Future<Source> GetSouById(int id) {
        return executorService.submit(() -> {
            return sourceDao.getSourceById(id);
        });
    }
    public Future<Long> insertSou(Source source) {
        return executorService.submit(()->{
           Long sid = sourceDao.insert(source);
            return sid;
        });
    }
    public void shutdown() {
        executorService.shutdown();
    }
}
