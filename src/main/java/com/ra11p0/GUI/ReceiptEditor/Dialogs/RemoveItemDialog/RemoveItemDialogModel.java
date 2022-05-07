package com.ra11p0.GUI.ReceiptEditor.Dialogs.RemoveItemDialog;

import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;

import java.util.List;

public abstract class RemoveItemDialogModel extends Dialog<ReceiptItem> {
    protected ListProperty<ReceiptItem> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected RemoveItemDialogModel(List<ReceiptItem> items){ this.items.setAll(items); }
}
