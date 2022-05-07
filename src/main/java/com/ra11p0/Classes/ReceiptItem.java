package com.ra11p0.Classes;

import com.ra11p0.Interfaces.IItem;
import com.ra11p0.Models.ReceiptItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public class ReceiptItem extends ReceiptItemModel {

    public StringProperty name() { return name; }

    public StringProperty store() { return store; }

    public FloatProperty tax() { return tax; }

    public FloatProperty price() { return price; }

    public FloatProperty quantity() { return quantity; }

    public FloatProperty total() { return total; }

    public void addQuantity(float quantity){ this.quantity.set(this.quantity.getValue()+quantity);}

    public ReceiptItem(IItem item, float qty){ this(item.name().get(), item.tax().get(), item.price().get(), item.store().get(), qty); }

    public ReceiptItem(String name, float tax, float price, String store, float qty){
        total.bind(new FloatBinding(){
            {
                super.bind(price());
                super.bind(quantity);
            }
            @Override
            protected float computeValue() {
                return price().get() * quantity.get();
            }
        });
        this.quantity.set(qty);
        this.name.set(name);
        this.tax.set(tax);
        this.price.set(price);
        this.store.set(store);
    }
}
