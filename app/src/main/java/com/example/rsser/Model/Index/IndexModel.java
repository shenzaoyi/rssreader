package com.example.rsser.Model.Index;

import com.example.rsser.DAO.Callback;
import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.base.BaseModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class IndexModel extends BaseModel implements IndexInterface{

    // 使用一个内部类来接受数据， 学的时候鬼知道是这么用的
    public class callback implements Callback{
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
    public List<Item> getData(int sid) {
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
    public void initRepo(Respositories respositories) {
        this.respos = respositories;
    }
}
