package com.ra11p0.structures;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Receipt {
    private String _ID; public String get_ID() {
        return _ID;
    }
    private String _store; public String get_store() {
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
    private ArrayList<ReceiptItem> _items = new ArrayList<ReceiptItem>(); public ArrayList<ReceiptItem> get_items() {
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
        _ID = _newId;
        try {
            if (oldReceipt.exists())  oldReceipt.renameTo(new File("res/receipts/" + _ID + ".json"));
        }catch(Exception e){
            new JOptionPane().showMessageDialog(new JFrame(), e, "Error!", JOptionPane.ERROR_MESSAGE);
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
    public String toString(){
        return _ID;
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
        int counter = 0;
        String[] receiptFiles = new File("res/receipts/").list();
        assert receiptFiles != null;
        for(String file : receiptFiles) {
            String substring = file.substring(0, file.length() - 5);
            if (substring.equals(_newId) || substring.equals(_newId + "-" + counter)) counter++;
        }
        if(counter != 0) _newId += "-" + counter;

    }
}
