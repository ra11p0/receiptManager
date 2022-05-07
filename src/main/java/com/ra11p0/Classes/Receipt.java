package com.ra11p0.Classes;

import com.ra11p0.Models.ReceiptModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;

import java.util.*;

public class Receipt extends ReceiptModel {

    public ReadOnlyStringProperty store() { return store; }

    public FloatProperty total() { return total; }

    public ObjectProperty<Date> date() { return date; }

    public ReadOnlyListProperty<ReceiptItem> items() { return items; }

    public StringProperty dateString(){ return dateString; }

    public Boolean isChanged() { return changed; }

    public Receipt(String store, Date date){
        items.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends ReceiptItem> c) {
                total.bind(new FloatBinding(){
                    { items.forEach(e->super.bind(e.total())); }

                    @Override
                    protected float computeValue() {
                        return (float) items.stream().mapToDouble(e->e.total().get()).sum();
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
        this(receipt.store().get(), receipt.date().get());
        items.get().addAll(receipt.items);
    }

    public void addItem(ReceiptItem item){
        changed = true;
        if(items.contains(item)) items.get(items.indexOf(item)).addQuantity(item.quantity().get());

        else items.get().add(item);
    }

    public void removeItem(ReceiptItem item){
        changed = true;
        items.remove(item);
    }

    @Override
    public Receipt clone() {
        Receipt clone = new Receipt(store().get(), date().get());
        for(ReceiptItem receiptItem : items){
            ReceiptItem newReceiptItem = new ReceiptItem(receiptItem, receiptItem.quantity().get());
            clone.addItem(newReceiptItem);
        }
        return clone;
    }
}
