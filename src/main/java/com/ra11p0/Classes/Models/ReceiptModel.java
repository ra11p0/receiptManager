package com.ra11p0.Classes.Models;

import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.Interfaces.IReceipt;
import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Date;

public abstract class ReceiptModel implements Cloneable, IReceipt {
    protected final StringProperty store = new SimpleStringProperty();

    protected final ListProperty<ReceiptItem> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected final ObjectProperty<Date> date = new SimpleObjectProperty<>();

    protected final transient FloatProperty total = new SimpleFloatProperty();

    protected final transient StringProperty dateString = new SimpleStringProperty();

    @Override
    public abstract ReceiptModel clone();

    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != Receipt.class) return false;
        ReceiptModel receipt = (ReceiptModel) obj;
        return receipt.totalProperty().get() == this.totalProperty().get() &
                receipt.itemsProperty().equals(this.itemsProperty()) &
                receipt.storeProperty().get().equals(this.storeProperty().get()) &
                receipt.dateProperty().get().equals(this.dateProperty().get());
    }

    @Override
    public String toString(){ return store.get(); }
}
