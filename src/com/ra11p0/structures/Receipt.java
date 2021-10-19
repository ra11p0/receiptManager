package com.ra11p0.structures;

import com.google.gson.Gson;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Receipt {
    private String _ID; public String get_ID() {
        return _ID;
    }
    private final String _store; public String get_store() {
        return _store;
    }
    private float _paid; public float get_paid() {
        return _paid;
    }
    private float _totalTax; public float get_totalTax() {
        return _totalTax;
    }
    private int _qty; public int get_qty() {
        return _qty;
    }
    private final ArrayList<ReceiptItem> _items = new ArrayList<>(); public ArrayList<ReceiptItem> get_items() {
        return _items;
    }
    private Date _date; public Date get_date() {
        return _date;
    }
    private transient String _newId = "";
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
        _items.add(item);
        _paid += item.get_Item().get_price() * item.get_qty();
        _totalTax += 1+ item.get_Item().get_taxRate()/(item.get_Item().get_price() * item.get_qty());
        _qty ++;
        _changesMade=true;
    }
    public void saveReceipt() throws IOException {
        File oldReceipt = new File("res/receipts/" + _ID + ".json");
        if (_newId != null) _ID = _newId;
        try {
            if (oldReceipt.exists())  oldReceipt.renameTo(new File("res/receipts/" + _ID + ".json"));
        }catch(Exception e){
            JOptionPane.showMessageDialog(new JFrame(), e, "Error!", JOptionPane.ERROR_MESSAGE);
        }
        Gson gson = new Gson();
        FileWriter fw = new FileWriter("res/receipts/" + _ID + ".json");
        gson.toJson(this, fw);
        fw.close();
        _changesMade=false;
    }
    public void removeItem(ReceiptItem item){
        _items.remove(item);
        _paid -= item.get_Item().get_price() * item.get_qty();
        _totalTax -= 1+ item.get_Item().get_taxRate()/(item.get_Item().get_price() * item.get_qty());
        _qty --;
        _changesMade=true;
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
    public boolean deleteReceipt(){
        File receiptFile = new File("res/receipts/" + this.get_ID() + ".json");
        return receiptFile.delete();
    }
    public String toString(){
        return _ID;
    }
    public String get_dateString(){
        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(_date.getTime());
        dateString += (calendar.get(Calendar.YEAR)-2000) + "-";
        if (calendar.get(Calendar.MONTH)+1 < 10) dateString += "0" + calendar.get(Calendar.MONTH)+1 + "-";
        else dateString += calendar.get(Calendar.MONTH)+1 + "-";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) dateString += "0" + calendar.get(Calendar.DAY_OF_MONTH);
        else dateString += calendar.get(Calendar.DAY_OF_MONTH);
        return dateString;
    }
}
