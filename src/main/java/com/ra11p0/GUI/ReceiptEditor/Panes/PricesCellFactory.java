package com.ra11p0.GUI.ReceiptEditor.Panes;

import com.ra11p0.App;
import com.ra11p0.Classes.Models.ItemModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class PricesCellFactory implements Callback<ListView<ItemModel>, ListCell<ItemModel>> {
    @Override
    public ListCell<ItemModel> call(ListView<ItemModel> itemModelListView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(ItemModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) return;
                BorderPane parent = new BorderPane();
                parent.setLeft(new Label(item.storeProperty().get()));
                Label priceLabel = new Label();
                priceLabel.textProperty().bind(item.priceProperty().asString().concat(" " + App.settings.currency));
                parent.setRight(priceLabel);
                setGraphic(parent);
            }
        };
    }
}
