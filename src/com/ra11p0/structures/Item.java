package com.ra11p0.structures;

import java.util.Locale;

public class Item {
    private final String _name; public String get_name() {
        return _name;
    }
    private String _store; public String get_store() {
        return _store;
    }
    private final float _tax; public float get_taxRate() {
        return _tax;
    }
    private final float _price; public float get_price() {
        return _price;
    }

    public Item(String name, float tax, float price, String store){
        _name = name;
        _tax = tax;
        _price = price;
        _store = store;
    }
    public void setStore(String store){
        _store = store;
    }
    public String toString(){
        return _name + " - " + String.format("%.2f", _price);
    }
    @Override
    public boolean equals(Object obj){
        try {
            if (obj == null) return false;
            Item item = (Item) obj;
            if(item.get_store().length() == 0 || _store.length()==0) return item.get_name().toLowerCase(Locale.ROOT).equals(this.get_name().toLowerCase(Locale.ROOT)) && item.get_price() == this.get_price();
            return item.get_name().toLowerCase(Locale.ROOT).equals(this.get_name().toLowerCase(Locale.ROOT)) && item.get_price() == this.get_price() && item.get_store().equals(_store);
        }catch (Exception ex){
            return false;
        }
    }
}
