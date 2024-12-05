package com.example.rsser.View.Index;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rsser.DAO.Item;
import com.example.rsser.Presenter.Index.Index;
import com.example.rsser.R;
import com.example.rsser.base.BasePresenter;
import com.example.rsser.base.BaseView;
import java.util.List;

public class IndexActivity extends BaseView implements MainViewInt {
    private Index indexPresenter;   // View 维护的一个Presenter, youyong
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        TextView textView = findViewById(R.id.headOfIndex);
        textView.setTextSize(24);
        initPresenter();
        // mock 数据
        Item item = new Item();
        item.setSourceId(1);
        item.setTitle("Sample Item Title");
        item.setLink("http://samplelink.com");
        item.setDescription("Sample item description");
        item.setContent("Sample item content");
        item.setPubdate(System.currentTimeMillis()); // 使用当前时间作为发布时间
        // mock 数据

        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "这是一个简单的 Toast 消息", Toast.LENGTH_SHORT).show();
                indexPresenter.getdata(1);
                Log.d("LOG", "onClick: " + "Clicked!");
            }
        });
    }

    @Override
    protected void initPresenter() {
        this.indexPresenter = new Index(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return indexPresenter;
    }

    @Override
    public void loadData(List<IndexAdpter.Item> itemList) {
        RecyclerView recyclerView = findViewById(R.id.indexCycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        IndexAdpter adapter = new IndexAdpter(this, itemList);
        recyclerView.setAdapter(adapter);
    }
}
