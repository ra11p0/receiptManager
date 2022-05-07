package com.ra11p0.Models;

import com.ra11p0.Interfaces.IReceiptItem;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class ReceiptItemModel extends ItemModel implements IReceiptItem {
    protected FloatProperty quantity = new SimpleFloatProperty();

    protected transient FloatProperty total = new SimpleFloatProperty();

}
