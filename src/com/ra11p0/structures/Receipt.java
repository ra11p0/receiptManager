package com.ra11p0.structures;

import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;

import java.io.IOException;
import java.util.*;

public class Receipt {
    private String _ID; public String get_ID() {
        return _ID;
    } public void set_ID(String newId){
        _ID = newId;
    }
    private final String _store; public String get_store() {
        return _store;
    }
    private float _paid; public float get_paid() {
        return _paid;
    }
    private float _totalTax;// public float get_totalTax() {return _totalTax;}
    private int _qty;// public int get_qty() {return _qty;}
    private final ArrayList<ReceiptItem> _items = new ArrayList<>(); public ArrayList<ReceiptItem> get_items() {
        return _items;
    }
    private Date _date; public Date get_date() {
        return _date;
    }
    public transient String _newId = "";
    public transient boolean _changesMade = false;
    public Receipt(String store, Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(date.getTime());
        _store = store;
        _date = date;
        setNewId(date);
    }
    public void addItem(ReceiptItem item){
        _paid += item.get_Item().get_price() * item.get_qty();
        _totalTax += 1+ item.get_Item().get_taxRate()/(item.get_Item().get_price() + item.get_qty());
        _changesMade = true;
        for(ReceiptItem receiptItem : _items){
            if(receiptItem.get_Item().equals(item.get_Item())){
                receiptItem.addQty(item.get_qty());
                return;
            }
        }
        _items.add(item);
        _qty ++;
        ReceiptsManager.changesMade = true;
    }
    public void saveReceipt() throws IOException {
        ReceiptsManager.saveReceipt(this);
    }
    public void removeItem(ReceiptItem item){
        _items.remove(item);
        _paid -= item.get_Item().get_price() * item.get_qty();
        _totalTax -= 1+ item.get_Item().get_taxRate()/(item.get_Item().get_price() * item.get_qty());
        _qty --;
        _changesMade=true;
        ReceiptsManager.changesMade = true;
    }
    public void setNewId(Date date){
        _changesMade=true;
        _date = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(date.getTime());
        _newId = _store.toUpperCase(Locale.ROOT).substring(0, 3) +
                "-" + (calendar.get(Calendar.YEAR)-2000) +
                "-" + (calendar.get(Calendar.MONTH)+1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH);
        int counter = 1;
        ArrayList<String> idsOfExisting = new ArrayList<>();
        for(Receipt receipt : ReceiptsManager.getReceipts()) idsOfExisting.add(receipt.get_ID());
        while(idsOfExisting.contains(_newId + "-" + counter)) counter++;
        _newId += "-" + counter;

    }
    public void deleteReceipt(){
        ReceiptsManager.removeReceipt(this);
    }
    public String toString(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(_date.getTime());
        return _store; //+ "-" + calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1);
    }
    public String get_dateString(){
        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(_date.getTime());
        dateString += (calendar.get(Calendar.YEAR)-2000) + "-";
        if (calendar.get(Calendar.MONTH)+1 < 10) dateString += "0" + (calendar.get(Calendar.MONTH)+1) + "-";
        else dateString += (calendar.get(Calendar.MONTH)+1) + "-";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) dateString += "0" + calendar.get(Calendar.DAY_OF_MONTH);
        else dateString += calendar.get(Calendar.DAY_OF_MONTH);
        return dateString;
    }
    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != Receipt.class) return false;
        Receipt receipt = (Receipt) obj;
        return Objects.equals(receipt.get_ID(), this._ID) &&
                receipt.get_paid() == this._paid &&
                receipt.get_items().equals(this._items) &&
                receipt.get_store().equals(this.get_store());
    }
}
