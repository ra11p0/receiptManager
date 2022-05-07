package com.ra11p0.Models;

import com.ra11p0.Classes.Receipt;
import com.ra11p0.Interfaces.IReceipt;
import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Date;

public abstract class ReceiptModel implements Cloneable, IReceipt {
    protected StringProperty store = new SimpleStringProperty();

    protected ListProperty<ReceiptItem> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected ObjectProperty<Date> date = new SimpleObjectProperty<>();

    protected transient FloatProperty total = new SimpleFloatProperty();

    protected transient StringProperty dateString = new SimpleStringProperty();

    protected transient Boolean changed = false;

    @Override
    public abstract ReceiptModel clone();

    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != Receipt.class) return false;
        ReceiptModel receipt = (ReceiptModel) obj;
        return receipt.total().get() == this.total().get() &
                receipt.items().equals(this.items()) &
                receipt.store().get().equals(this.store().get()) &
                receipt.date().get().equals(this.date().get());
    }

    @Override
    public String toString(){ return store.get(); }
}
