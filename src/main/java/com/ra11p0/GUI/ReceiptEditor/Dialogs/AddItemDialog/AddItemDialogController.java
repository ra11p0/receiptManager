package com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog;

import com.ra11p0.Classes.Interfaces.IItem;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.CreateItemDialog.CreateItemDialogController;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.CreateItemDialog.CreateItemDialogModel;
import com.ra11p0.LocaleBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddItemDialogController extends AddItemDialogModel {
    @FXML
    VBox root;

    @FXML
    ComboBox<IItem> itemsBox;

    @FXML
    TextField quantityField;

    public AddItemDialogController(String store) { super(store); }

    @FXML
    void initialize(){
        itemsBox.getItems().setAll(items);
        quantityField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (Objects.equals(newValue, "")) return;
            try{ Float.parseFloat(newValue); }
            catch(Exception ex){ quantityField.textProperty().set(oldValue); }
        });
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY)) {
                return new ReceiptItem(itemsBox.getSelectionModel().getSelectedItem(), Float.parseFloat(quantityField.getText()));
            }
            else return null;
        });
    }

    @FXML
    void searchBarKeyTyped(KeyEvent keyEvent) {
        StringProperty searchPhrase = ((TextField)keyEvent.getSource()).textProperty();
        List<IItem> matchedItems = items.stream().filter(e->e.nameProperty().get().contains(searchPhrase.get())).toList();
        itemsBox.getItems().setAll(matchedItems);
        itemsBox.show();
    }

    public void newItemButtonAction() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(CreateItemDialogController.class.getResource("CreateItemDialog.fxml"));
        dialogLoader.setResources(LocaleBundle.languageBundle);
        dialogLoader.load();
        CreateItemDialogModel dialogModel = dialogLoader.getController();
        dialogModel.showAndWait();
        Item item = dialogModel.getResult();
        if(item == null) return;
        item.storeProperty().set(store);
        items.add(item);
        itemsBox.getSelectionModel().select(item);
    }
}
