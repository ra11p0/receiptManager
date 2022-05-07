package com.ra11p0.GUI.ReceiptsManager.Dialogs.SelectStoreDialog;

import com.ra11p0.App;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;

public abstract class SelectStoreDialogModel extends Dialog<String> {
    protected ListProperty<String> stores = new SimpleListProperty<>(FXCollections.observableArrayList());
    public SelectStoreDialogModel(){ stores.setAll(App.dataAccessObject.getStores()); }
}
