package com.ra11p0.GUI.ReceiptsManager.Panes;

import com.ra11p0.App;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.GUI.ReceiptEditor.Panes.ReceiptEditorController;
import com.ra11p0.GUI.ReceiptsManager.Dialogs.SelectStoreDialog.SelectStoreDialogController;
import com.ra11p0.GUI.ReceiptsManager.Dialogs.SelectStoreDialog.SelectStoreDialogModel;
import com.ra11p0.GUI.SearchItemsResult.Dialogs.SelectItemsDialog.SelectItemsDialogController;
import com.ra11p0.GUI.SearchItemsResult.Panes.SearchItemsResultController;
import com.ra11p0.LocaleBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptsManagerController extends ReceiptsManagerModel {

    @FXML
    Button previousButton;
    @FXML
    Button nextButton;
    @FXML
    Button searchButton;
    @FXML
    Button removeButton;
    @FXML
    Button editButton;
    @FXML
    Button newReceiptButton;
    @FXML
    Label titleLabel;
    @FXML
    Label aTaxLabel;
    @FXML
    Label bTaxLabel;
    @FXML
    Label cTaxLabel;
    @FXML
    Label noTaxLabel;
    @FXML
    Label totalLabel;
    @FXML
    Label aTaxHeader;
    @FXML
    Label bTaxHeader;
    @FXML
    Label cTaxHeader;
    @FXML
    Label noTaxHeader;
    @FXML
    Label totalHeader;
    @FXML
    HBox daysContainer;

    public ReceiptsManagerController() throws IOException {
    }

    @FXML
    void initialize() {
        daysContainer.getChildren().addAll(dayFields);
        titleLabel.textProperty().bind(getMonth());
        aTaxLabel.textProperty().bind(getTotalByTax(.23f).asString().concat(" " + App.settings.currency));
        bTaxLabel.textProperty().bind(getTotalByTax(.08f).asString().concat(" " + App.settings.currency));
        cTaxLabel.textProperty().bind(getTotalByTax(.05f).asString().concat(" " + App.settings.currency));
        noTaxLabel.textProperty().bind(getTotalByTax(.0f).asString().concat(" " + App.settings.currency));
        totalLabel.textProperty().bind(getTotal().asString().concat(" " + App.settings.currency));

        aTaxHeader.setText("23% " + LocaleBundle.get("tax"));
        bTaxHeader.setText("8% " + LocaleBundle.get("tax"));
        cTaxHeader.setText("5% " + LocaleBundle.get("tax"));
        noTaxHeader.setText("0% " + LocaleBundle.get("tax"));
    }

    @FXML
    void previousButtonHandler() {
        Date since = new Date(days.get(0).date().get().getTime()-(7*24 * 60 * 60 * 1000));
        setDateSince(since);
    }

    @FXML
    void nextButtonHandler() {
        Date since = new Date(days.get(6).date().get().getTime() + (24 * 60 * 60 * 1000));
        setDateSince(since);
    }

    @FXML
    void newReceiptButtonHandler() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(SelectStoreDialogController.class.getResource("SelectStoreDialog.fxml"));
        dialogLoader.setResources(LocaleBundle.languageBundle);
        dialogLoader.load();
        SelectStoreDialogModel dialogModel = dialogLoader.getController();
        dialogModel.showAndWait();
        String store = dialogModel.getResult();
        if(store == null) return;
        FXMLLoader parentLoader = new FXMLLoader(ReceiptEditorController.class.getResource("ReceiptEditorComponent.fxml"));
        parentLoader.setControllerFactory(f-> new ReceiptEditorController(new Receipt(store, new Date())));
        parentLoader.setResources(LocaleBundle.languageBundle);
        Parent parent = parentLoader.load();
        App.root.setCenter(parent);
    }

    @FXML
    void editButtonHandler() throws IOException {
        if(selected == null) return;
        FXMLLoader loader = new FXMLLoader(ReceiptEditorController.class.getResource("ReceiptEditorComponent.fxml"));
        loader.setControllerFactory(f-> new ReceiptEditorController(selected));
        loader.setResources(LocaleBundle.languageBundle);
        Parent parent = loader.load();
        App.root.setCenter(parent);
    }

    @FXML
    void removeButtonHandler() {
        int choice = JOptionPane.showOptionDialog(null,
                LocaleBundle.get("youSureYouWantToRemoveReceipt") , "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{LocaleBundle.get("no"), LocaleBundle.get("yes")},
                LocaleBundle.get("no"));
        if(choice != 1) return;
        App.dataAccessObject.remove(selected);
    }

    @FXML
    void searchButtonHandler() throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader(SelectItemsDialogController.class.getResource("SelectItemsDialog.fxml"));
        dialogLoader.setResources(LocaleBundle.languageBundle);
        dialogLoader.load();
        SelectItemsDialogController selectItemsDialogFX = dialogLoader.getController();
        selectItemsDialogFX.showAndWait();

        List<String> namesOfItems = selectItemsDialogFX.getResult();
        if(namesOfItems == null | namesOfItems.isEmpty()) return;
        ArrayList<ReceiptItem> matchedItems = (ArrayList<ReceiptItem>) App.dataAccessObject.getReceiptItems().stream().filter(o->namesOfItems.contains(o.nameProperty().get())).collect(Collectors.toList());
        FXMLLoader parentLoader = new FXMLLoader(SearchItemsResultController.class.getResource("SearchItemsResultComponent.fxml"));
        parentLoader.setControllerFactory(e-> new SearchItemsResultController(matchedItems));
        parentLoader.setResources(LocaleBundle.languageBundle);
        Parent parent = parentLoader.load();
        App.root.setCenter(parent);
    }
}