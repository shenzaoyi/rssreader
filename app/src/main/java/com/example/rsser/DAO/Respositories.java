package com.example.rsser.DAO;

import android.app.Application;
import android.content.Context;

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
    private TypeDao typeDao;
    private ExecutorService executorService; // 整他一个线程池， 吼
    public Respositories(Application application) {
        RssDB rssDB = RssDB.getInstance(application);
        sourceDao = rssDB.sourceDao();
        itemDao = rssDB.itemDao();
        typeDao = rssDB.typeDao();
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
            List<Source> sources = sourceDao.getSourcesByIdAndDate(sid, data);
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
           Future<Type> f = getTypeById(source.getTypeId());
           Type t = f.get();
           t.setNum(t.getNum()+1);
           updateType(t);
            return sid;
        });
    }
    public void shutdown() {
        executorService.shutdown();
    }
    public Future<List<Source>> getAllSources(){
        return executorService.submit(() -> {
            return sourceDao.getAllSources();
        });
    }
    public void deleteSou(Source source) {
        executorService.submit(()->{
            sourceDao.delete(source);
            Future<Type> f = getTypeById(source.getTypeId());
            Type t = null;
            try {
                t = f.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            t.setNum(t.getNum()-1);
            updateType(t);
        });
    }

// TYPE OPERATION
    public boolean isTypeEmpty() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<Boolean> future = executorService.submit(() -> {
                int recordCount = typeDao.getTableSize();
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
    public Future<List<Type>> getAllTypes() {
        return executorService.submit(() -> {
            return typeDao.getAllTypes();
        });
    }

    public Future<Type> getTypeById(int id) {
        return executorService.submit(() -> {
            return typeDao.getTypeById(id);
        });
    }

    public Future<Type> getTypeByName(String name) {
        return executorService.submit(() -> {
            return typeDao.getTypeByName(name);
        });
    }
    public Future<Long> insertType(Type t) {
        return executorService.submit(() -> {
            return typeDao.insert(t);
        });
    }
    public void updateType(Type t) {
        executorService.submit(()->{
            typeDao.update(t);
        });
    }
    public void deleteType(Type t ) {
        executorService.submit(()->{
            typeDao.delete(t);
        });
    }
    public void deleteSubscriptionsByType(int id) {
     // 根据 tid 删除所有此类型对应的源
        executorService.submit(()->{
            sourceDao.deleteByTypeId(id);
            List<Source> sources = sourceDao.getSourcesByTypeId(id);
            for (Source s : sources) {
                itemDao.deleteItemsBySourceId(s.getId());
            }
        });
    }
}
