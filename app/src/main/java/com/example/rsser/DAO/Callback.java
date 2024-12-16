package com.example.rsser.DAO;

import java.util.List;

public interface Callback {
    interface onItemsLoaded{
        void onItemsLoaded(List<Item> items);
    }
    interface onSourceLoaded{
        void onSourceLoaded(List<Source> sources);
    }
    interface onSourceExist{
        void onSourceExist(boolean exists);
    }
}
