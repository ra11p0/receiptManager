package com.ra11p0.GUI.SearchItemsResult.Panes;


import com.ra11p0.App;
import com.ra11p0.LocaleBundle;
import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptsManagerController;
import com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog.EditItemDialogController;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.Models.ItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.util.*;


public class SearchItemsResultController extends SearchItemsResultModel{
    @FXML
    ListView<Receipt> receiptsList;
    @FXML
    ListView<Map.Entry<String, ObservableValue<String>>> infoList;
    @FXML
    ListView<ItemModel> pricesInStores;
    @FXML
    Button previewReceiptButton;
    @FXML
    Button editItemButton;
    @FXML
    Button goBackButton;

    public SearchItemsResultController(ArrayList<ReceiptItem> items) {
        super(items);
    }

    @FXML
    public void initialize(){
        Map.Entry<String, ObservableValue<String>> nameEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("name") + ": ", name);
        Map.Entry<String, ObservableValue<String>> totalEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("total") + ": ", total.asString().concat(" " + App.settings.currency));
        Map.Entry<String, ObservableValue<String>> taxEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("taxRate") + ": ", tax.multiply(100).asString());
        Map.Entry<String, ObservableValue<String>> totalTaxEntry = new AbstractMap.SimpleEntry<>(LocaleBundle.get("totalTax") + ": ", totalTax.asString().concat(" " + App.settings.currency));

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
        infoList.getItems().addAll(nameEntry, totalEntry, taxEntry, totalTaxEntry);


        receiptsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Receipt> call(ListView<Receipt> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Receipt item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) return;
                        FloatProperty sumProperty = new SimpleFloatProperty();
                        sumProperty.bind(new FloatBinding() {
                            {
                                item.items().stream()
                                        .filter(e -> items.contains(e)).toList()
                                        .forEach(e -> super.bind(e.total()));
                            }

                            @Override
                            protected float computeValue() {
                                return (float) item.items().stream()
                                        .filter(e -> items.contains(e))
                                        .mapToDouble(e -> e.total().get())
                                        .sum();
                            }
                        });
                        BorderPane parent = new BorderPane();
                        Label storeLabel = new Label(item.store().get());
                        Label dateLabel = new Label();
                        dateLabel.textProperty().bind(new StringBinding() {
                            {
                                this.bind(item.date());
                            }

                            @Override
                            protected String computeValue() {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(item.date().get());
                                return String.format("%s - %s - %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                            }
                        });
                        Label totalLabel = new Label();
                        totalLabel.textProperty().bind(sumProperty.asString().concat(" " + App.settings.currency));
                        parent.setLeft(storeLabel);
                        parent.setCenter(dateLabel);
                        parent.setRight(totalLabel);
                        setGraphic(parent);
                    }
                };
            }
        });
        receiptsList.itemsProperty().bind(receipts);


        pricesInStores.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ItemModel> call(ListView<ItemModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ItemModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) return;
                        BorderPane parent = new BorderPane();
                        parent.setLeft(new Label(item.store().get()));
                        Label priceLabel = new Label();
                        priceLabel.textProperty().bind(item.price().asString().concat(" " + App.settings.currency));
                        parent.setRight(priceLabel);
                        setGraphic(parent);
                    }
                };
            }
        });
        pricesInStores.itemsProperty().bind(items);
    }

    @FXML
    void backButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(ReceiptsManagerController.class.getResource("ReceiptsManagerComponent.fxml"));
        loader.setResources(LocaleBundle.languageBundle);
        App.root.setCenter(loader.load());
    }

    @FXML
    void previewButtonAction() throws Exception {
        throw new Exception("Not implemented yet!");
    }

    @FXML
    void editItemButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(EditItemDialogController.class.getResource("EditItemDialog.fxml"));
        loader.setControllerFactory(e-> new EditItemDialogController(items));
        loader.setResources(LocaleBundle.languageBundle);
        loader.load();
        EditItemDialogController editItemDialogFX = loader.getController();
        editItemDialogFX.showAndWait();
        Object[] result = editItemDialogFX.getResult();
        if(result == null) return;
        Item oldItem = (Item) result[1];
        Item newItem = (Item) result[2];
        if((Boolean) result[0]){
            int sureness = JOptionPane.showConfirmDialog(null,
                    LocaleBundle.get("sureYouWantToReplace") +
                            " " + oldItem.name().get() + " " +
                            LocaleBundle.get("with") + " " +
                            newItem.name().get() + "?");
            if(sureness == JOptionPane.YES_OPTION){
                App.dataAccessObject.getReceiptItems().stream().filter(o -> o.name().get().equals(oldItem.name().get())).toList().forEach(o->o.name().set(newItem.name().get()));
            }
        }
        else{
            int sureness = JOptionPane.showConfirmDialog(null,
                    LocaleBundle.get("sureYouWantToReplace") + " " + oldItem.toString() +
                            " " + LocaleBundle.get("with") + " " + newItem.toString() + "?");
            if(sureness == JOptionPane.YES_OPTION){
                App.dataAccessObject.getReceiptItems().stream().filter(o -> o.equals(oldItem)).toList().forEach(o->{o.name().set(newItem.name().get()); o.price().set(newItem.price().get());});
            }
        }
    }
}
