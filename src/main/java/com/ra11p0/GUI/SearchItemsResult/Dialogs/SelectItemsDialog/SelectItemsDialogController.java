package com.ra11p0.GUI.SearchItemsResult.Dialogs.SelectItemsDialog;


import com.ra11p0.App;
import com.ra11p0.Classes.SelectableItemNode;
import com.ra11p0.LocaleBundle;
import com.ra11p0.Models.ItemModel;
import com.ra11p0.SharedTypes.AutoCompleteTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SelectItemsDialogController extends SelectItemsDialogModel {
    @FXML
    VBox root;
    @FXML
    AutoCompleteTextField<String> searchField;
    @FXML
    ListView<SelectableItemNode<ItemModel>> itemsList;
    @FXML
    Button unselectAllButton;
    @FXML
    Button selectAllButton;

    @FXML
    void initialize(){
        searchField.setEntries(itemsNames);
        itemsList.getItems().addAll(selectableItems.stream().distinct().toList());
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY)) return selectableItems.stream().filter(SelectableItemNode::isSelected).map(e -> e.get().name().get()).toList();
            else return null;
        });
    }

    @FXML
    void selectAll() { itemsList.getItems().forEach(o->o.setSelected(true)); }

    @FXML
    void unselectAll() { itemsList.getItems().forEach(o->o.setSelected(false)); }

    @FXML
    void searchFieldOnKeyTyped(KeyEvent actionEvent) {
        String searchedPhrase = ((AutoCompleteTextField<?>)actionEvent.getSource()).getText();
        itemsList.getItems().clear();
        boolean isStoreBasedSearch = App.dataAccessObject.getStores().contains(searchedPhrase);
        if(isStoreBasedSearch) itemsList.getItems().addAll(selectableItems.stream().filter(e->e.get().store().get().contains(searchedPhrase)).toList());
        else itemsList.getItems().addAll(selectableItems.stream().filter(e->e.get().name().get().contains(searchedPhrase)).toList());
        itemsList.getItems().addAll(selectableItems.stream().filter(e->e.isSelected() & !itemsList.getItems().contains(e)).toList());
    }
}