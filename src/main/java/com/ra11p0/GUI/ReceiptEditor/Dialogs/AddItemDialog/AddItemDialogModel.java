package com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog;

import com.ra11p0.App;
import com.ra11p0.Classes.Interfaces.IItem;
import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;

import java.util.stream.Collectors;

public abstract class AddItemDialogModel extends Dialog<ReceiptItem> {
    protected ListProperty<IItem> items = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected String store;
    AddItemDialogModel(String store){
        this.store = store;
        items.setAll(App.dataAccessObject.getReceiptItems().stream()
                .filter(e -> e.storeProperty().get().equals(store))
                .collect(Collectors.toList()));
    }

}
