package com.ra11p0.Interfaces;

import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.*;

import java.util.Date;

public interface IReceipt {
    ReadOnlyStringProperty store();

    FloatProperty total();

    ObjectProperty<Date> date();

    ReadOnlyListProperty<ReceiptItem> items();

    StringProperty dateString();

    Boolean isChanged();

    void addItem(ReceiptItem item);

    void removeItem(ReceiptItem item);
}
