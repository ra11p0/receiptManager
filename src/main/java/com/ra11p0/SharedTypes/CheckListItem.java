package com.ra11p0.SharedTypes;

public class CheckListItem {
    private final Object _object;
    private boolean _isSelected = false;
    //*****
    public CheckListItem(Object object) {
        _object = object;
    }
    public boolean isSelected() {
        return _isSelected;
    }
    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
    }
    public Object getObject(){
        return _object;
    }
    @Override
    public String toString() {
        return _object.toString();
    }
}