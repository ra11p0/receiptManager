package com.ra11p0.Classes;


import com.google.gson.Gson;
import com.ra11p0.Classes.Models.DataAccessObjectModel;
import com.ra11p0.Utils.ReceiptsFile;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.util.List;

public class ReceiptsDAO extends DataAccessObjectModel<Receipt> {
    public List<String> getNamesOfItems(){
        return get().stream()
                .flatMap(e -> e.itemsProperty()
                        .get().stream())
                .map(e->e.nameProperty().get())
                .distinct()
                .toList();
    }

    public List<String> getStores(){
        return get().stream()
                .map(e->e.storeProperty().get())
                .distinct()
                .toList();
    }

    public List<ReceiptItem> getReceiptItems(){
        return get().stream()
                .flatMap(e -> e.itemsProperty().stream())
                .toList();
    }

    @Override
    public void load(String path){
        setPath(path);
        Gson gson = FxGson.create();
        FileReader gsonFileReader;
        try {
            gsonFileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            save();
            return;
        }
        ReceiptsFile receiptsPacket = gson.fromJson(gsonFileReader, ReceiptsFile.class);
        try {
            gsonFileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(receiptsPacket == null) return;
        for(Receipt receipt : receiptsPacket.receipts){
            Receipt newReceipt = new Receipt(receipt.storeProperty().get(), receipt.dateProperty().get());
            for(ReceiptItem receiptItem : receipt.itemsProperty()){
                ReceiptItem newReceiptItem = new ReceiptItem(receiptItem, receiptItem.quantityProperty().get());
                newReceipt.addItem(newReceiptItem);
            }
            add(newReceipt);
        }
    }
    @Override
    public void save(String path){
        ReceiptsFile receiptsPacket = new ReceiptsFile(get());
        boolean isDeleted = new File(path).delete();
        if(!isDeleted) System.err.println("file not deleted!");
        Gson gson = FxGson.create();
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        gson.toJson(receiptsPacket, fw);
        try {
            assert fw != null;
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
