package com.ra11p0.Classes;

import com.ra11p0.Classes.Models.ReceiptModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;

import java.util.*;

public class Receipt extends ReceiptModel {

    public ReadOnlyStringProperty storeProperty() { return store; }

    public FloatProperty totalProperty() { return total; }

    public ObjectProperty<Date> dateProperty() { return date; }

    public ReadOnlyListProperty<ReceiptItem> itemsProperty() { return items; }

    public void addItem(ReceiptItem item){
        if(items.contains(item)) items.get(items.indexOf(item)).addQuantity(item.quantityProperty().get());
        else items.get().add(item);
    }

    public void removeItem(ReceiptItem item){ items.remove(item); }

    public Receipt(String store, Date date){
        items.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends ReceiptItem> c) {
                total.bind(new FloatBinding(){
                    { items.forEach(e->super.bind(e.totalProperty())); }

                    @Override
                    protected float computeValue() {
                        return (float) items.stream().mapToDouble(e->e.totalProperty().get()).sum();
                    }
                });
            }
        });
        dateString.bind(this.date.asString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(date.getTime());
        this.store.set(store);
        this.date.set(date);
    }

    public Receipt(Receipt receipt){
        this(receipt.storeProperty().get(), receipt.dateProperty().get());
        items.get().addAll(receipt.items);
    }

    @Override
    public Receipt clone() {
        Receipt clone = new Receipt(storeProperty().get(), dateProperty().get());
        for(ReceiptItem receiptItem : items){
            ReceiptItem newReceiptItem = new ReceiptItem(receiptItem, receiptItem.quantityProperty().get());
            clone.addItem(newReceiptItem);
        }
        return clone;
    }
}
