package com.ra11p0.Classes;

import com.ra11p0.Classes.Interfaces.IItem;
import com.ra11p0.Classes.Models.ReceiptItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public class ReceiptItem extends ReceiptItemModel {

    public StringProperty nameProperty() { return name; }

    public StringProperty storeProperty() { return store; }

    public FloatProperty taxProperty() { return tax; }

    public FloatProperty priceProperty() { return price; }

    public FloatProperty quantityProperty() { return quantity; }

    public FloatProperty totalProperty() { return total; }

    public void addQuantity(float quantity){ this.quantity.set(this.quantity.getValue()+quantity);}

    public ReceiptItem(IItem item, float qty){ this(item.nameProperty().get(), item.taxProperty().get(), item.priceProperty().get(), item.storeProperty().get(), qty); }

    public ReceiptItem(String name, float tax, float price, String store, float qty){
        total.bind(new FloatBinding(){
            {
                super.bind(priceProperty());
                super.bind(quantity);
            }
            @Override
            protected float computeValue() {
                return priceProperty().get() * quantity.get();
            }
        });
        this.quantity.set(qty);
        this.name.set(name);
        this.tax.set(tax);
        this.price.set(price);
        this.store.set(store);
    }
}
