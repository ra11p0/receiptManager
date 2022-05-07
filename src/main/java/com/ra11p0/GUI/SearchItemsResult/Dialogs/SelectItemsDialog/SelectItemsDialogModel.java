package com.ra11p0.GUI.SearchItemsResult.Dialogs.SelectItemsDialog;

import com.ra11p0.App;
import com.ra11p0.Classes.SelectableItemNode;
import com.ra11p0.Models.ItemModel;
import javafx.scene.control.Dialog;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.ra11p0.Utils.StreamUtils.distinctByKey;

public abstract class SelectItemsDialogModel extends Dialog<List<String>> {

    protected List<SelectableItemNode<ItemModel>> selectableItems;

    protected TreeSet<String> itemsNames;

    public SelectItemsDialogModel(){
        this.selectableItems = App.dataAccessObject.getReceiptItems().stream()
                .map(e->new SelectableItemNode<ItemModel>(e))
                .filter(distinctByKey(e->e.get().name().get()))
                .sorted(Comparator.comparing(e->e.get().name().get(), Comparator.naturalOrder()))
                .toList();

        this.itemsNames = new TreeSet<>(App.dataAccessObject.getNamesOfItems());
    }

}
