package com.ra11p0.Classes.Models;

import com.ra11p0.App;
import com.ra11p0.Classes.Interfaces.ISavable;
import com.ra11p0.Classes.Receipt;

public abstract class ReceiptEditorModel extends Receipt implements ISavable {
    Receipt originalReceipt;

    public ReceiptEditorModel(Receipt receipt){
        super(receipt);
        this.originalReceipt = receipt;
    }

    @Override
    public void save() {
        if(App.dataAccessObject.get().contains(originalReceipt)) {
            originalReceipt.items.set(itemsProperty());
            originalReceipt.dateProperty().set(dateProperty().get());
        }
        else App.dataAccessObject.add(new Receipt(this));
    }

    @Override
    public void save(String path) {
        throw new UnsupportedOperationException();
    }
}
