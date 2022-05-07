package com.ra11p0.Models;

import com.ra11p0.App;
import com.ra11p0.Interfaces.ISavable;
import com.ra11p0.Classes.Receipt;
import javafx.beans.property.ObjectProperty;

public abstract class ReceiptEditorModel extends Receipt implements ISavable {
    Receipt originalReceipt = null;

    public ReceiptEditorModel(Receipt receipt){
        super(receipt);
        this.originalReceipt = receipt;
    }

    @Override
    public void save() {
        if(App.dataAccessObject.get().contains(originalReceipt)) {
            originalReceipt.items.set(items());
            originalReceipt.date().set(date().get());
        }
        else App.dataAccessObject.add(new Receipt(this));
    }

    @Override
    public void save(String path) {
        throw new UnsupportedOperationException();
    }
}
