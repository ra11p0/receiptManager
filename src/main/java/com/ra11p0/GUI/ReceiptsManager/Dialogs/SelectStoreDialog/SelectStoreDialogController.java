package com.ra11p0.GUI.ReceiptsManager.Dialogs.SelectStoreDialog;

import com.ra11p0.LocaleBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class SelectStoreDialogController extends SelectStoreDialogModel{
    @FXML
    VBox root;

    @FXML
    ComboBox<String> storesBox;

    @FXML
    void initialize(){
     storesBox.itemsProperty().bind(stores);
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY)) return storesBox.getSelectionModel().getSelectedItem();
            else return null;
        });
    }

    @FXML
    void addNewStoreAction() {
        TextInputDialog newStoreDialog = new TextInputDialog();
        newStoreDialog.setHeaderText(LocaleBundle.get("nameTheNewStore"));
        newStoreDialog.showAndWait();
        String result = newStoreDialog.getResult() == null ? "": newStoreDialog.getResult();
        if(result.isBlank()) return;
        stores.add(result);
        storesBox.getSelectionModel().select(result);
    }
}
