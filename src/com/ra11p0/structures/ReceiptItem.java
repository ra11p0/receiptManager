package com.ra11p0.structures;

public class ReceiptItem {
    private Item _item; public Item get_Item() {
        return _item;
    }
    private float _qty; public float get_qty() {
        return _qty;
    }
    public ReceiptItem(Item item, float qty){
        _item = item;
        _qty = qty;
    }
    public String toString(){
        return _item.get_name() + " - " + String.format("%.2f", _item.get_price() * _qty);
    }
}
