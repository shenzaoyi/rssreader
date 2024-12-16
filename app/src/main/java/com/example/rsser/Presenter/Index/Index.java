package com.example.rsser.Presenter.Index;

import com.example.rsser.DAO.Item;
import com.example.rsser.DAO.Respositories;
import com.example.rsser.DAO.Source;
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
    public void loadData(List<Item> res) {
        List<IndexAdpter.Item> itemList = new ArrayList<>();
        // 数据处理
        Source s = indexModel.getSourceById(res.get(0).getSourceId());
        for (int i = 0; i < res.size(); i++) {
            Date date = new Date(s.getLast_updated());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            IndexAdpter.Item j = new IndexAdpter.Item(res.get(i).getTitle(),formattedDate, s.getTitle(), res.get(i).getLink(), res.get(i).getDescription());
            itemList.add(j);
        }
        // 通知view层
        String msg = "加载订阅源成功";
        baseView.loadData(itemList, msg);
    }
    public void handlError(List<Item> items, int errorCase) {
        switch (errorCase) {
            case 0:
                // there no Source, tell user to add
                String msg0 = "没有订阅源，快去导入吧";
                baseView.loadData(new ArrayList<>(0), msg0);
                break;
            case 1:
                // this sid is not exist
                String msg1 = "默认偏好订阅源不存在，尝试手动更新";
                baseView.loadData(new ArrayList<>(0), msg1);
            case 2:
                String msg2 = "该订阅源暂无订阅信息";
                baseView.loadData(new ArrayList<>(0), msg2);
        }
    }
    @Override
    public void tempSave(List<Item> items) {
        indexModel.tempsave(items);
    }
    public void tempSaveS(List<Source> sources) {
        indexModel.tempsaveS(sources);
    }
    public boolean loadRecommendation(int sid, long data){
        int errorCase;
        if (indexModel.isEmptySource()) {
            System.out.println("Table is NULL");
            // check if source_table is null
            errorCase = 0;
            handlError(new ArrayList<>(0),errorCase);
            return false;
        }else if (!indexModel.isSourExist(sid)) {
            // this record not exist, select another
            System.out.println("This sid is not exist" + indexModel.isSourExist(sid));
            errorCase = 1;
            Source source = indexModel.getFirstSource();
            List<Item> items = indexModel.getItemBySid(source.getId());
            handlError(items, errorCase);
            return false;
        } else{
            List<Source> sources = indexModel.GetSourcesBySidData(sid, data);
            if (sources.isEmpty()) {
                // need to updata
                System.out.println("It is empty!");
                return false;
            }else{
                List<Item> res = indexModel.getItemBySid(sid);
                if (res.size() == 0) {
                    System.out.println("Items is empty");
                    handlError(null, 3);
                }
                loadData(res);
                return true;
            }
        }
    }
}
