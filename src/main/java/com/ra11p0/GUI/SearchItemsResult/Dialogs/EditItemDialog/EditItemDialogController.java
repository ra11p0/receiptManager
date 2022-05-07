package com.ra11p0.GUI.SearchItemsResult.Dialogs.EditItemDialog;

import com.ra11p0.Classes.Interfaces.IItem;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.Models.ItemModel;
import com.ra11p0.Utils.AutoCompleteTextField;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Objects;

public class EditItemDialogController extends EditItemDialogModel {
    @FXML
    VBox root;
    @FXML
    Text titleLabel;
    @FXML
    ComboBox<ItemModel> itemsBox;
    @FXML
    Text nameOfProductLabel;
    @FXML
    AutoCompleteTextField<String> nameField;
    @FXML
    Text priceLabel;
    @FXML
    TextField priceField;
    @FXML
    CheckBox changeNameForAll;

    @FXML
    void initialize(){
        nameField.setEntries(itemsNames);
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, "")) return;
            try{
                Float.parseFloat(newValue);
            }catch(Exception ex){
                priceField.textProperty().set(oldValue);
            }
        });
        changeNameForAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                priceField.setVisible(false);
                priceLabel.setVisible(false);
            }
            else{
                priceField.setVisible(true);
                priceLabel.setVisible(true);
            }
        });
        itemsBox.itemsProperty().set(items);
        itemsBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ItemModel> call(ListView<ItemModel> itemModelListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ItemModel itemModel, boolean b) {
                        super.updateItem(itemModel, b);
                        if (itemModel == null) return;
                        setText(String.format("%s (%s) - %s", itemModel.nameProperty().get(), itemModel.storeProperty().get(), itemModel.priceProperty().get()));
                    }
                };
            }
        });
        itemsBox.getSelectionModel().selectedItemProperty().addListener((observableValue, itemModel, t1) -> {
            nameField.setText(observableValue.getValue().nameProperty().get());
            priceField.setText(String.valueOf(observableValue.getValue().priceProperty().get()));
        });

        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CLOSE);
        setResultConverter(buttonType -> {
            if(buttonType.equals(ButtonType.APPLY)) {
                IItem oldItem = itemsBox.getSelectionModel().getSelectedItem();
                String name = nameField.textProperty().get();
                float _price = Float.parseFloat(priceField.textProperty().get());
                return new Object[]{changeNameForAll.isSelected(), oldItem, new Item(name, oldItem.taxProperty().get(), _price, oldItem.storeProperty().get())};
            }
            else return null;
        });
    }

    public EditItemDialogController(ListProperty<ItemModel> items){ super(items); }
}
