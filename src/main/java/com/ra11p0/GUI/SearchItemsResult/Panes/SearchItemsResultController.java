package com.ra11p0.GUI.SearchItemsResult.Panes;


import com.ra11p0.App;
import com.ra11p0.GUI.ReceiptEditor.Panes.ReceiptsListCellFactory;
import com.ra11p0.LocaleBundle;
import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptsManagerController;
import com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog.EditItemDialogController;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.Classes.Models.ItemModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;


public class SearchItemsResultController extends SearchItemsResultModel{
    @FXML
    TextField taxField;
    @FXML
    TextField totalTaxField;
    @FXML
    TextField totalField;
    @FXML
    TextField nameField;
    @FXML
    ListView<Receipt> receiptsList;
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
        nameField.textProperty().bind(name);
        totalField.textProperty().bind(total.asString().concat(" " + App.settings.currency));
        taxField.textProperty().bind(tax.multiply(100).asString().concat(" " + App.settings.currency));
        totalTaxField.textProperty().bind(totalTax.asString().concat(" " + App.settings.currency));
        pricesInStores.itemsProperty().bind(items);
        receiptsList.itemsProperty().bind(receipts);
        ((ReceiptsListCellFactory)receiptsList.getCellFactory()).items.bind(items);
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
                            " " + oldItem.nameProperty().get() + " " +
                            LocaleBundle.get("with") + " " +
                            newItem.nameProperty().get() + "?");
            if(sureness == JOptionPane.YES_OPTION){
                App.dataAccessObject.getReceiptItems().stream().filter(o -> o.nameProperty().get().equals(oldItem.nameProperty().get())).toList().forEach(o->o.nameProperty().set(newItem.nameProperty().get()));
            }
        }
        else{
            int sureness = JOptionPane.showConfirmDialog(null,
                    LocaleBundle.get("sureYouWantToReplace") + " " + oldItem.toString() +
                            " " + LocaleBundle.get("with") + " " + newItem.toString() + "?");
            if(sureness == JOptionPane.YES_OPTION){
                App.dataAccessObject.getReceiptItems().stream().filter(o -> o.equals(oldItem)).toList().forEach(o->{o.nameProperty().set(newItem.nameProperty().get()); o.priceProperty().set(newItem.priceProperty().get());});
            }
        }
    }
}
