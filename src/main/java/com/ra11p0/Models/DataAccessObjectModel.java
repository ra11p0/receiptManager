package com.ra11p0.Models;

import com.ra11p0.Interfaces.IDataAccessObject;
import java.util.ArrayList;
import java.util.List;

public abstract class DataAccessObjectModel<T> implements IDataAccessObject<T> {
    List<T> data = new ArrayList<>();

    String path;

    @Override
    public final void setPath(String path) { this.path = path; }

    @Override
    public final String getPath() { return path; }

    @Override
    public final List<T> get() { return data; }

    @Override
    public final void add(T t) { data.add(t); }

    @Override
    public final void addAll(List<T> t) { data.addAll(t); }

    @Override
    public final void remove(T t) { data.remove(t); }

    @Override
    public final void removeAll(List<T> t) { data.removeAll(t); }

    @Override
    public final void load() { load(path); }

    @Override
    public final void save() { save(path); }

}
