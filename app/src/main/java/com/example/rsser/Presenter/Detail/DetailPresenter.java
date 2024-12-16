package com.example.rsser.Presenter.Detail;

import com.example.rsser.DAO.Respositories;
import com.example.rsser.Model.RssMan.RssManModel;
import com.example.rsser.View.Detail.DetailActivity;
import com.example.rsser.base.BasePresenter;

public class DetailPresenter extends BasePresenter<DetailActivity> implements DetailInt {
    private RssManModel rssManModel;
    public DetailPresenter(DetailActivity view) {
        attachView(view);
        this.rssManModel = new RssManModel();
        rssManModel.initRepo(new Respositories(baseView.getApplication()));
    }
}
