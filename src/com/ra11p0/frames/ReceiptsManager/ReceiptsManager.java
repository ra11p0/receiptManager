package com.ra11p0.frames.ReceiptsManager;

import com.google.gson.Gson;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import com.ra11p0.structures.ReceiptsPacket;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ReceiptsManager {
    private static JFrame _frame;
    private final static ArrayList<Receipt> _receipts = new ArrayList<>();
    public final static ArrayList<Item> _items = new ArrayList<>();
    private static ArrayList<String> _namesOfProducts = new ArrayList<>();
    public static Boolean changesMade = false;
    public static void refreshItemsAndReceipts(){
        _items.clear();
        _receipts.clear();
        Gson gson = new Gson();
        FileReader gsonFileReader = null;
        try {
            gsonFileReader = new FileReader("res/receipts.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert gsonFileReader != null;
        ReceiptsPacket receiptsPacket = gson.fromJson(gsonFileReader, ReceiptsPacket.class);
        try {
            gsonFileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(receiptsPacket == null) return;
        for(Receipt receipt : receiptsPacket.receipts){
            _receipts.add(receipt);
            for (ReceiptItem item : receipt.get_items())
                if (!_items.contains(item.get_Item()))
                    _items.add(item.get_Item());
        }
        _items.sort(Comparator.comparing(Item::get_name));
    }
    public static ArrayList<Receipt> getReceipts() {
        if(_receipts.size() == 0) refreshItemsAndReceipts();
        return _receipts;
    }
    public static ArrayList<Item> getItems() {
        for(Receipt receipt : _receipts)
            for(ReceiptItem receiptItem : receipt.get_items()) {
                if (!_items.contains(receiptItem.get_Item())) {
                    _items.add(receiptItem.get_Item());
                }
            }
        _items.sort(Comparator.comparing(Item::get_name));
        if(_items.size() == 0) refreshItemsAndReceipts();
        return _items;
    }
    public static ArrayList<Receipt> getReceiptsContaining(ArrayList<Item> items){
        ArrayList<Receipt> receipts = new ArrayList<>();
        for(Receipt receipt : _receipts)
            for(Item item : items)
                for(ReceiptItem receiptItem : receipt.get_items())
                    if(receiptItem.get_Item().get_name().equals(item.get_name()) && !receipts.contains(receipt)) receipts.add(receipt);
        return receipts;
    }
    public static void addReceipt(Receipt receipt){
        _receipts.add(receipt);
        changesMade = true;
    }
    public static void removeReceipt(Receipt receipt){
        _receipts.remove(receipt);
        changesMade = true;
    }
    public static void saveReceipt(Receipt receipt) throws IOException {
        removeReceipt(receipt);
        if (receipt._newId != null) receipt.set_ID(receipt._newId);
        receipt._changesMade=false;
        changesMade = true;
        addReceipt(receipt);
    }
    public static boolean checkIfChangesMade(){
        return changesMade;
    }
    public static ArrayList<String> getNamesOfProducts(){
        _namesOfProducts.clear();
        for(Item item : _items) if(!_namesOfProducts.contains(item.get_name())) _namesOfProducts.add(item.get_name());
        return _namesOfProducts;
    }
}
