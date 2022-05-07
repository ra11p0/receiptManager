package com.ra11p0.GUI.ReceiptEditor.Dialogs.CreateItemDialog;

import com.ra11p0.App;
import com.ra11p0.Classes.Interfaces.IItem;
import com.ra11p0.Classes.Item;
import com.ra11p0.Utils.AutoCompleteTextField;
import com.ra11p0.Utils.TextPropertyOnlyFloatListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CreateItemDialogController extends CreateItemDialogModel {

    @FXML
    VBox root;
    @FXML
    AutoCompleteTextField<String> nameField;
    @FXML
    TextField priceField;
    @FXML
    ComboBox<Float> taxBox;
    @FXML
    void initialize(){
        nameField.setEntries(names);
        priceField.textProperty().addListener(new TextPropertyOnlyFloatListener(priceField.textProperty()));
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY))
                return new Item(nameField.getText(), taxBox.getValue(), Float.parseFloat(priceField.getText()));

            else return null;
        });
    }
    @FXML
    void nameFieldKeyTyped(KeyEvent keyEvent) {
        String searchPhrase = ((AutoCompleteTextField<?>)keyEvent.getSource()).textProperty().get();
        List<IItem> matchedItems = App.dataAccessObject.getReceiptItems().stream()
                .filter(e->e.nameProperty().get().contains(searchPhrase))
                .map(e->(IItem)e)
                .toList();
        Optional<IItem> firstMatched = matchedItems.stream().findFirst();
        if(firstMatched.isEmpty()) return;
        priceField.setText(String.valueOf(firstMatched.get().priceProperty().get()));
        taxBox.getSelectionModel().select(firstMatched.get().taxProperty().get());
    }
}
