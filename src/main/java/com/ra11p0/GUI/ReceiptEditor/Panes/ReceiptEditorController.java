package com.ra11p0.GUI.ReceiptEditor.Panes;

import com.ra11p0.App;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog.AddItemDialogController;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog.AddItemDialogModel;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.PickDateDialog.PickDateDialogController;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.PickDateDialog.PickDateDialogModel;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.RemoveItemDialog.RemoveItemDialogController;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.RemoveItemDialog.RemoveItemDialogModel;
import com.ra11p0.LocaleBundle;
import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptsManagerController;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.Models.ReceiptEditorModel;
import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class ReceiptEditorController extends ReceiptEditorModel {
    @FXML
    TextField storeField;
    @FXML
    TextField dateField;
    @FXML
    TextField totalField;
    @FXML
    TableView<ReceiptItem> table;
    @FXML
    Button addItem;
    @FXML
    Button removeItem;
    @FXML
    Button editDate;
    @FXML
    Button save;
    @FXML
    Button goBackButton;

    public ReceiptEditorController(Receipt receipt) { super(receipt); }

    @FXML
    void initialize(){
        storeField.textProperty().bind(store);
        dateField.textProperty().bind(new StringBinding() {
            {this.bind(dateProperty());}
            @Override
            protected String computeValue() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateProperty().get());
                return String.format("%s - %s - %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });
        totalField.textProperty().bind(total.asString());
        table.itemsProperty().bind(items);
    }

    @FXML
    void addItemButtonHandler() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(AddItemDialogController.class.getResource("AddItemDialog.fxml"));
        dialogLoader.setControllerFactory(e->new AddItemDialogController(storeProperty().get()));
        dialogLoader.setResources(LocaleBundle.languageBundle);
        dialogLoader.load();
        AddItemDialogModel dialogModel = dialogLoader.getController();
        dialogModel.showAndWait();
        ReceiptItem receiptItem = dialogModel.getResult();
        if(receiptItem == null)  return;
        addItem(receiptItem);
    }

    @FXML
    void removeItemButtonHandler() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(RemoveItemDialogController.class.getResource("RemoveItemDialog.fxml"));
        dialogLoader.setControllerFactory(e->new RemoveItemDialogController(items));
        dialogLoader.load();
        RemoveItemDialogModel dialogModel = dialogLoader.getController();
        dialogModel.showAndWait();
        ReceiptItem receiptItem = dialogModel.getResult();
        if(receiptItem == null) return;
        removeItem(receiptItem);
    }

    @FXML
    void editDateButtonHandler() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(PickDateDialogController.class.getResource("PickDateDialog.fxml"));
        dialogLoader.load();
        PickDateDialogModel dialogModel = dialogLoader.getController();
        dialogModel.showAndWait();
        Date date = dialogModel.getResult();
        if(date == null) return;
        dateProperty().set(date);
    }

    @FXML
    void saveButtonHandler() {
        save();
        JOptionPane.showMessageDialog(null,  LocaleBundle.get("saved") + "!",LocaleBundle.get("saved") + "!", JOptionPane.INFORMATION_MESSAGE);
    }

    @FXML
    void goBackButtonHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(ReceiptsManagerController.class.getResource("ReceiptsManagerComponent.fxml"));
        loader.setResources(LocaleBundle.languageBundle);
        Parent parent = loader.load();
        App.root.setCenter(parent);
    }
}
