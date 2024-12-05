package com.example.rsser.base;

import com.example.rsser.Contract.Contract;

// 疯转不同业务层通用的操作
// 所有P类都需要实现这几个接口，所以抽象出一个公共类来实现这些，其他只需要继承即可
// 同时啊， 需要持有一个 BaseView
public abstract class BasePresenter <T extends BaseView> implements Contract.Presenter<T> {
    protected T baseView;

    @Override
    public void attachView(T view) {
        this.baseView = view;
    }

    @Override
    public void detachView() {
        baseView = null;
    }

    @Override
    public boolean isViewAttached() {
        return baseView != null;
    }
}
