package com.example.rsser.Model.RssMan;

import androidx.annotation.Nullable;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
import com.example.rsser.View.RssManager.RssManInt;
import com.example.rsser.base.BaseModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RssManModel extends BaseModel implements RssManModelInt {
    @Override
    public void initRepo(Respositories respositories) {
        this.respos = respositories;
    }

    @Override
    public Long SaveSource(Source source) {
        List<Source> sourceList = new ArrayList<>();
        sourceList.add(source);
        Future<Long> future = respos.insertSou(source);
        try {
            Long sid = future.get(); // 可能抛出 InterruptedException 和 ExecutionException
            // 处理成功插入后的逻辑，例如记录插入的 ID
            return sid;
        } catch (InterruptedException e) {
            // 处理线程被中断的情况
            Thread.currentThread().interrupt(); // 重新设置中断状态
            System.err.println("Thread was interrupted while waiting for the result.");
        } catch (ExecutionException e) {
            // 处理执行异常的情况
            System.err.println("An error occurred during the asynchronous operation: " + e.getCause());
        }
        return -1L;
    }

    @Override
    public void saveItems(List<Item> items) {
        respos.insertItems(items);
    }
}
