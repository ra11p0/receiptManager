package com.ra11p0.GUI.SearchItemsResult.Panes;

import com.ra11p0.App;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.Classes.Models.ItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.stream.Collectors;

import static com.ra11p0.Utils.StringUtils.findCommon;

public abstract class SearchItemsResultModel {
    final StringProperty name = new SimpleStringProperty();

    final FloatProperty totalTax = new SimpleFloatProperty();

    final FloatProperty tax = new SimpleFloatProperty();

    final FloatProperty total = new SimpleFloatProperty();

    final ListProperty<ItemModel> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    final ListProperty<Receipt> receipts = new SimpleListProperty<>(FXCollections.observableArrayList());


    SearchItemsResultModel(List<ReceiptItem> items){
        this.items.setAll(items.stream().distinct().collect(Collectors.toList()));

        name.bind(new StringBinding() {
            {items.forEach(e->this.bind(e.nameProperty()));}
            @Override
            protected String computeValue() {
                return findCommon(SearchItemsResultModel.this.items.stream().map(e->e.nameProperty().get()).collect(Collectors.toList()));
            }
        });

        total.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.totalProperty()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e->e.totalProperty().get()).sum();
            }
        });

        totalTax.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.totalProperty(), e.taxProperty()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e-> e.totalProperty().get() * e.taxProperty().get()).sum();
            }
        });

        tax.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.taxProperty()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e->e.taxProperty().get()).sum() / items.size();
            }
        });

        receipts.setAll(App.dataAccessObject.get().stream().filter(e-> e.itemsProperty().stream().anyMatch(o -> this.items.contains(o))).collect(Collectors.toList()));
    }
}
