package com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog;

import com.ra11p0.Classes.Models.ItemModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;

import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class EditItemDialogModel extends Dialog<Object[]> {
    protected final ListProperty<ItemModel> items = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final TreeSet<String> itemsNames;
    public EditItemDialogModel(ListProperty<ItemModel> items){
        this.items.set(items);
        this.itemsNames = items.stream()
                .map(e -> e.nameProperty().get())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
