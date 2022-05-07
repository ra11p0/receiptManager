package com.ra11p0.Interfaces;

import javafx.beans.property.FloatProperty;

public interface IReceiptItem {
    FloatProperty quantity();

    FloatProperty total();

    void addQuantity(float quantity);
}
