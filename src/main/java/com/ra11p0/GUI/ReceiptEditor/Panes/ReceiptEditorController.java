package com.ra11p0.GUI.ReceiptEditor.Panes;

import com.ra11p0.App;
import com.ra11p0.LocaleBundle;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.AddItemDialog;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.PickDateDialog;
import com.ra11p0.GUI.ReceiptEditor.Dialogs.SelectItemDialog;
import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptsManagerController;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Models.ReceiptEditorModel;
import com.ra11p0.Classes.ReceiptItem;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class ReceiptEditorController extends ReceiptEditorModel {
    @FXML
    TableView<ReceiptItem> table;
    @FXML
    ListView<Map.Entry<String, ObservableValue<String>>> infoList;
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
        AbstractMap.SimpleEntry<String, ObservableValue<String>> storeEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("store") + ": ", store());
        AbstractMap.SimpleEntry<String, ObservableValue<String>> dateEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("date") + ": ", new StringBinding() {
            {this.bind(date());}
            @Override
            protected String computeValue() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date().get());
                return String.format("%s - %s - %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });
        AbstractMap.SimpleEntry<String, ObservableValue<String>> totalEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("total") + ": ", total().asString());
        infoList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Map.Entry<String, ObservableValue<String>>> call(ListView<Map.Entry<String, ObservableValue<String>>> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Map.Entry<String, ObservableValue<String>> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) return;
                        BorderPane parent = new BorderPane();
                        Label label = new Label(item.getKey());
                        TextField textField = new TextField();
                        textField.textProperty().bind(item.getValue());
                        textField.setEditable(false);
                        parent.setLeft(label);
                        parent.setRight(textField);
                        setGraphic(parent);
                    }
                };
            }
        });
        //noinspection unchecked
        infoList.getItems().addAll(storeEntry, dateEntry, totalEntry);
        TableColumn<ReceiptItem, String> name = new TableColumn<>(LocaleBundle.get("item"));
        TableColumn<ReceiptItem, Float> qty = new TableColumn<>(LocaleBundle.get("qty"));
        TableColumn<ReceiptItem, Float> price = new TableColumn<>(LocaleBundle.get("price"));
        TableColumn<ReceiptItem, Float> tax = new TableColumn<>(LocaleBundle.get("tax"));
        TableColumn<ReceiptItem, Float> total = new TableColumn<>(LocaleBundle.get("total"));
        name.setCellValueFactory(item -> item.getValue().name());
        qty.setCellValueFactory(item -> item.getValue().quantity().asObject());
        price.setCellValueFactory(item -> item.getValue().price().asObject());
        tax.setCellValueFactory(item -> item.getValue().tax().asObject());
        total.setCellValueFactory(item -> item.getValue().total().asObject());
        //noinspection unchecked
        table.getColumns().addAll(name, qty, price, tax, total);
        Bindings.bindContent(table.getItems(), items());
    }

    @FXML
    void addItemButtonHandler() {
        ReceiptItem receiptItem = AddItemDialog.ShowInputDialog(store().get());
        if(receiptItem == null)  return;
        addItem(receiptItem);
    }

    @FXML
    void removeItemButtonHandler() {
        ReceiptItem receiptItem = SelectItemDialog.ShowInputDialog(new ArrayList<>(items()));
        if(receiptItem == null) return;
        removeItem(receiptItem);
    }

    @FXML
    void editDateButtonHandler() {
        Date date = PickDateDialog.ShowInputDialog();
        if(date == null) return;
        date().set(date);
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
