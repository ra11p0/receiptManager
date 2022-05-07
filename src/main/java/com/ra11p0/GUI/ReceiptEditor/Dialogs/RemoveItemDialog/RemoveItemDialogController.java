package com.ra11p0.GUI.ReceiptEditor.Dialogs.RemoveItemDialog;

import com.ra11p0.Classes.ReceiptItem;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class RemoveItemDialogController extends RemoveItemDialogModel{
    @FXML
    VBox root;
    @FXML
    ComboBox<ReceiptItem> itemsBox;

    public RemoveItemDialogController(List<ReceiptItem> items) { super(items); }

    @FXML
    void initialize(){
        itemsBox.itemsProperty().bind(items);
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY))
                return itemsBox.getSelectionModel().getSelectedItem();
            else return null;
        });
    }
}
