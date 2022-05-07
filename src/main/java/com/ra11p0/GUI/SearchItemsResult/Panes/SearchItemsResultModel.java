package com.ra11p0.GUI.SearchItemsResult.Panes;

import com.ra11p0.App;
import com.ra11p0.Classes.Item;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.ReceiptItem;
import com.ra11p0.Interfaces.IItem;
import com.ra11p0.Models.ItemModel;
import com.ra11p0.Models.ReceiptItemModel;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.stream.Collectors;

import static com.ra11p0.Utils.StringUtils.findCommon;

public abstract class SearchItemsResultModel {
    StringProperty name = new SimpleStringProperty();

    FloatProperty totalTax = new SimpleFloatProperty();

    FloatProperty tax = new SimpleFloatProperty();

    FloatProperty total = new SimpleFloatProperty();

    ListProperty<ItemModel> items = new SimpleListProperty<>(FXCollections.observableArrayList());

    ListProperty<Receipt> receipts = new SimpleListProperty<>(FXCollections.observableArrayList());


    SearchItemsResultModel(List<ReceiptItem> items){
        this.items.setAll(items.stream().distinct().collect(Collectors.toList()));

        name.bind(new StringBinding() {
            {items.forEach(e->this.bind(e.name()));}
            @Override
            protected String computeValue() {
                return findCommon(SearchItemsResultModel.this.items.stream().map(e->e.name().get()).collect(Collectors.toList()));
            }
        });

        total.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.total()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e->e.total().get()).sum();
            }
        });

        totalTax.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.total(), e.tax()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e-> e.total().get() * e.tax().get()).sum();
            }
        });

        tax.bind(new FloatBinding() {
            {items.forEach(e->this.bind(e.tax()));}
            @Override
            protected float computeValue() {
                return (float) items.stream().mapToDouble(e->e.tax().get()).sum() / items.size();
            }
        });

        receipts.setAll(App.dataAccessObject.get().stream().filter(e-> e.items().stream().anyMatch(o -> this.items.contains(o))).collect(Collectors.toList()));
    }
}
