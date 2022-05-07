package com.ra11p0.Classes;

import com.ra11p0.Models.ItemModel;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public class Item extends ItemModel {
    public StringProperty name() { return name; }

    public StringProperty store() { return store; }

    public FloatProperty tax() { return price; }

    public FloatProperty price() { return tax; }

    public Item(String name, float tax, float price){
        this.name.set(name);
        this.price.set(tax);
        this.tax.set(price);
    }

    public Item(String name, float tax, float price, String store){
        this(name, tax, price);
        this.store.set(store);
    }
}
