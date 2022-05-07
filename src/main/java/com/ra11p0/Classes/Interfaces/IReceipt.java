package com.ra11p0.Classes.Interfaces;

import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.*;

import java.util.Date;

public interface IReceipt {
    ReadOnlyStringProperty storeProperty();

    FloatProperty totalProperty();

    ObjectProperty<Date> dateProperty();

    ReadOnlyListProperty<ReceiptItem> itemsProperty();

    void addItem(ReceiptItem item);

    void removeItem(ReceiptItem item);
}
