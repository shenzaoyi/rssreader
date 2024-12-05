package com.example.rsser.Contract;

import com.example.rsser.base.BaseView;

public interface Contract {
    interface Model{
        void executeIndex();
    }
    interface View{
    }
    interface Presenter <T extends BaseView>{
        void attachView(T view);

        void detachView();

        boolean isViewAttached();
    }
}