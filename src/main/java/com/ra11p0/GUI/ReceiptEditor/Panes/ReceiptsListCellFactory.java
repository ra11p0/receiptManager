package com.ra11p0.GUI.ReceiptEditor.Panes;

import com.ra11p0.App;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.Models.ItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.Calendar;

public class ReceiptsListCellFactory implements Callback<ListView<Receipt>, ListCell<Receipt>> {
    public final ListProperty<ItemModel> items = new SimpleListProperty<>(FXCollections.observableArrayList());
    @Override
    public ListCell<Receipt> call(ListView<Receipt> receiptListView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Receipt item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) return;
                FloatProperty sumProperty = new SimpleFloatProperty();
                sumProperty.bind(new FloatBinding() {
                    {item.itemsProperty().stream()
                            .filter(e -> items.contains(e)).toList()
                            .forEach(e -> super.bind(e.totalProperty()));}
                    @Override
                    protected float computeValue() {
                        return (float) item.itemsProperty().stream()
                                .filter(e -> items.contains(e))
                                .mapToDouble(e -> e.totalProperty().get())
                                .sum();
                    }
                });
                BorderPane parent = new BorderPane();
                Label storeLabel = new Label(item.storeProperty().get());
                Label dateLabel = new Label();
                dateLabel.textProperty().bind(new StringBinding() {
                    { this.bind(item.dateProperty()); }

                    @Override
                    protected String computeValue() {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(item.dateProperty().get());
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
}
