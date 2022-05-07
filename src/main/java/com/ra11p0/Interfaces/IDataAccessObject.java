package com.ra11p0.Interfaces;

import java.util.List;

public interface IDataAccessObject<T> extends ISavable, ILoadable {
    List<T> get();

    void add(T t);

    void addAll(List<T> t);

    void remove(T t);

    void removeAll(List<T> t);

    String getPath();

    void setPath(String path);
}
