package com.example.rsser.Presenter.Index;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.Model.Index.IndexModel;
import com.example.rsser.View.Index.IndexActivity;
import com.example.rsser.View.Index.IndexAdpter;
import com.example.rsser.base.BasePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Index extends BasePresenter<IndexActivity> implements IndexPresenter {
    private final IndexModel indexModel;
    public Index(IndexActivity view){
        attachView(view);
        this.indexModel = new IndexModel();
        indexModel.initRepo(new Respositories(baseView.getApplication()));
    }
    @Override
    public void getdata(int sid) {
        // 调用model层
        List<Item> res = indexModel.getData(sid);
        List<IndexAdpter.Item> itemList = new ArrayList<>();
        // 数据处理
        for (int i = 0; i < res.size(); i++) {
            Long t = res.get(i).getPubdate();
            Date date = new Date(t);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            IndexAdpter.Item j = new IndexAdpter.Item(res.get(i).getTitle(),formattedDate,"知乎", "img_url", res.get(i).getDescription());
        }
        // 通知view层
        baseView.loadData(itemList);
    }
}
