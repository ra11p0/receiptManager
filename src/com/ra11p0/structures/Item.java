package com.ra11p0.structures;

import java.util.Locale;

public class Item {
    private final String _name; public String get_name() {
        return _name;
    }
    private final String _store; public String get_store() {
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
    public String toString(){
        return _name + " - " + String.format("%.2f", _price);
    }

    @Override
    public boolean equals(Object obj){
        try {
            if (obj == null) return false;
            Item item = (Item) obj;
            return item.get_name().toLowerCase(Locale.ROOT).equals(this.get_name().toLowerCase(Locale.ROOT)) && item.get_price() == this.get_price();
        }catch (Exception ex){
            return false;
        }
    }
}
