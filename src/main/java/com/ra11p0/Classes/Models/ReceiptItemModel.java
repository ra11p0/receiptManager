package com.ra11p0.Classes.Models;

import com.ra11p0.Classes.Interfaces.IReceiptItem;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class ReceiptItemModel extends ItemModel implements IReceiptItem {
    protected final FloatProperty quantity = new SimpleFloatProperty();

    protected final transient FloatProperty total = new SimpleFloatProperty();

}
