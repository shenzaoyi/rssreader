package com.example.rsser.base;

import com.example.rsser.DAO.Respositories;

public abstract class BaseModel {
    protected Respositories respos;
    public abstract void initRepo(Respositories respositories);
}
