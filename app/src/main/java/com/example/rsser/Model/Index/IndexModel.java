package com.example.rsser.Model.Index;

import com.example.rsser.DAO.Callback;
import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
import com.example.rsser.DAO.Type;
import com.example.rsser.base.BaseModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class IndexModel extends BaseModel implements IndexInterface{
    private boolean isSourceExistRes;

    // 使用一个内部类来接受数据， 学的时候鬼知道是这么用的
    public class callback implements Callback.onItemsLoaded{
        private List<Item> items;
        private CountDownLatch countDownLatch;
        @Override
        public void onItemsLoaded(List<Item> items) {
            this.items = items;
            if (countDownLatch != null){
                countDownLatch.countDown();
            }
        }
        public void setLatch(CountDownLatch latch) {this.countDownLatch = latch;}
    }
    @Override
    public List<Item> getItemBySid(int sid) {
        callback callback = new callback();
        CountDownLatch latch = new  CountDownLatch(1);
        callback.setLatch(latch);
        // respos 里面都是异步执行的， 需要线程同步
        respos.getItemsBySid(sid,callback);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (callback.items == null) {
            return null;
        }
        return callback.items;
    }

    @Override
    public void tempsave(List<Item> items) {
        respos.saveItemInArr(items);
    }

    @Override
    public void tempsaveS(List<Source> sources) {
        respos.saveSourceArr(sources);
    }

    @Override
    public List<Source> GetSourcesBySidData(int sid, long data) {
        callbackLoadRecommendation callback = new callbackLoadRecommendation();
        CountDownLatch latch = new  CountDownLatch(1);
        callback.setLatch(latch);
        respos.checkSidSou(sid,data,callback);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (callback.items == null) {
            return null;
        }
        System.out.println(callback.items.size());
        return callback.items;
    }
    public class callbackIsSourceExist implements Callback.onSourceExist{
        private  boolean exist;
        private CountDownLatch countDownLatch;
        public void setLatch(CountDownLatch latch) {this.countDownLatch = latch;}

        @Override
        public void onSourceExist(boolean exists) {
            this.exist = exists;
            if (countDownLatch != null){
                countDownLatch.countDown();
            }
        }
    }
    @Override
    public boolean isSourExist(int sid) {
        // check if this sid record exist
        callbackIsSourceExist callback = new callbackIsSourceExist();
        CountDownLatch latch = new  CountDownLatch(1);
        callback.setLatch(latch);
        // respos 里面都是异步执行的， 需要线程同步
        respos.isSidExists(sid,callback);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return callback.exist;
    }

    @Override
    public Source getFirstSource() {
        Future<Source> future = respos.GetFirstSource();
        try {
            Source source = future.get();
            return source;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Source getSourceById(int sid) {
        Future<Source> future = respos.GetSouById(sid);
        try {
            Source source = future.get();
            return source;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Source> getAllSources() {
        Future<List<Source>> future = respos.getAllSources();
        try {
            List<Source> sources = future.get();
            return sources;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class callbackLoadRecommendation implements Callback.onSourceLoaded{
        private List<Source> items;
        private CountDownLatch countDownLatch;

        public void setLatch(CountDownLatch latch) {this.countDownLatch = latch;}

        @Override
        public void onSourceLoaded(List<Source> sources) {
            this.items = sources;
            if (countDownLatch != null){
                countDownLatch.countDown();
            }
        }
    }
    public boolean isEmptySource() {
        // check is table is empty
        return respos.isTableEmpty();
    }
    @Override
    public void initRepo(Respositories respositories) {
        this.respos = respositories;
    }
    public boolean isEmptyType() {
        return respos.isTypeEmpty();
    }
    public void insertType(Type t) {
        respos.insertType(t);
    }
}
