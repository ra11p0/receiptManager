package com.ra11p0.Models;

import com.ra11p0.Interfaces.IItem;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class ItemModel implements IItem {
    protected StringProperty name = new SimpleStringProperty();

    protected StringProperty store = new SimpleStringProperty();

    protected FloatProperty price = new SimpleFloatProperty();

    protected FloatProperty tax = new SimpleFloatProperty();

    @Override
    public int hashCode() { return (store().get() + name().get() + price().get() + tax().get()).hashCode(); }

    @Override
    public boolean equals(Object obj){
        try {
            if (obj == null) return false;
            ItemModel item = (ItemModel) obj;
            return item.hashCode() == this.hashCode();
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public String toString(){ return name().get() ; }
}
