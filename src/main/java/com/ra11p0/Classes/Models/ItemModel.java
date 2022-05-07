package com.ra11p0.Classes.Models;

import com.ra11p0.Classes.Interfaces.IItem;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

public abstract class ItemModel implements IItem {
    @FXML
    protected final StringProperty name = new SimpleStringProperty();

    @FXML
    protected final StringProperty store = new SimpleStringProperty();

    @FXML
    protected final FloatProperty price = new SimpleFloatProperty();

    @FXML
    protected final FloatProperty tax = new SimpleFloatProperty();

    @Override
    public int hashCode() { return (storeProperty().get() + nameProperty().get() + priceProperty().get() + taxProperty().get()).hashCode(); }

    @Override
    public boolean equals(Object obj){
        try {
            if (obj == null) return false;
            ItemModel item = (ItemModel) obj;
            return item.hashCode() == this.hashCode();
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public String toString(){ return nameProperty().get() ; }
}
