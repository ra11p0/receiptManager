package com.ra11p0.structures;

public class ReceiptItem {
    private final Item _item; public Item get_Item() {
        return _item;
    }
    private float _qty; public float get_qty() {
        return _qty;
    }
    public ReceiptItem(Item item, float qty){
        _item = item;
        _qty = qty;
    }
    public void addQty(float qty){
        _qty+=qty;
    }
    public String toString(){
        return _item.get_name() + " - " + String.format("%.2f", _item.get_price() * _qty);
    }
    @Override
    public boolean equals(Object obj){
        try {
            if (obj == null) return false;
            ReceiptItem item = (ReceiptItem) obj;
            return item.get_Item().equals(this._item) && item.get_qty() == this._qty;
        }catch (Exception ex){
            return false;
        }
    }
}
