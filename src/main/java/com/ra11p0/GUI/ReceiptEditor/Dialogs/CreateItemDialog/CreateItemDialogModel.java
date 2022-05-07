package com.ra11p0.GUI.ReceiptEditor.Dialogs.CreateItemDialog;

import com.ra11p0.App;
import com.ra11p0.Classes.Item;
import javafx.scene.control.Dialog;

import java.util.TreeSet;

public abstract class CreateItemDialogModel extends Dialog<Item> {
    protected TreeSet<String> names;
    CreateItemDialogModel(){
        names = new TreeSet<>(App.dataAccessObject.getNamesOfItems());
    }
}
