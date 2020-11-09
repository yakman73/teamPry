package com.bilal.teampry;

import java.util.List;

public interface IRepository <T extends IEntity >{

    List<T> getAll();
    void get(T entity);
    void add(T entity);
    void delete(T entity);
    void update(T entity);
}
