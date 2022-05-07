package com.ra11p0.Classes.Interfaces;

import javafx.beans.property.FloatProperty;

public interface IReceiptItem {
    FloatProperty quantityProperty();

    FloatProperty totalProperty();

    void addQuantity(float quantity);
}
