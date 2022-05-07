package com.ra11p0.Classes.Interfaces;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public interface IItem {
    StringProperty nameProperty();

    StringProperty storeProperty();

    FloatProperty taxProperty();

    FloatProperty priceProperty();
}
