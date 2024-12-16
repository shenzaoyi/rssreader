package com.example.rsser.Presenter.RssManager;


import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
import com.example.rsser.Model.RssMan.RssManModel;
import com.example.rsser.View.RssManager.AddRssActivity;
import com.example.rsser.base.BasePresenter;

import java.util.List;


public class RssManPresenter extends BasePresenter<AddRssActivity> implements RssManPreInt {
    private final RssManModel rssManModel;
    public RssManPresenter(AddRssActivity view){
        attachView(view);
        this.rssManModel = new RssManModel();
        rssManModel.initRepo(new Respositories(baseView.getApplication()));
    }
    @Override
    public void saveSource(Source source, List<Item> items) {
        // check if source is null
        if (source == null) return;
        // save source in sqlite
        Long sid = rssManModel.SaveSource(source);
        // save item;
        for (Item item : items) {
            item.setSourceId(sid.intValue());
        }
        rssManModel.saveItems(items);
        baseView.onSuccess();
    }
}
