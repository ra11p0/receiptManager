package com.ra11p0;

import com.ra11p0.Interfaces.ILoadable;
import com.ra11p0.Interfaces.ISavable;

public class Settings implements ISavable, ILoadable {
    public float version;
    public String language;
    public String currency;
    public String appearance;

    @Override
    public void load() {

    }

    @Override
    public void load(String path) {

    }

    @Override
    public void save() {

    }

    @Override
    public void save(String path) {

    }
}
