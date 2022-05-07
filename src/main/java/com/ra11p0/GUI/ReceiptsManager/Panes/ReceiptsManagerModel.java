package com.ra11p0.GUI.ReceiptsManager.Panes;


import com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptField.ReceiptFieldController;
import com.ra11p0.Classes.Day;
import com.ra11p0.Classes.Receipt;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ReceiptsManagerModel {
    final List<Day> days = new ArrayList<>();

    final ObjectProperty<Date> dateSince = new SimpleObjectProperty<>(new Date());

    final List<ListView<Receipt>> dayLists = new ArrayList<>();

    final List<Node> dayFields = new ArrayList<>();

    Receipt selected;

    FloatProperty getTotalByTax(float tax){
        FloatProperty property = new SimpleFloatProperty();
        days.forEach(e->property.bind(new FloatBinding() {
            {this.bind(e.date());}
            @Override
            protected float computeValue() {
                return (float) days.stream()
                        .flatMap(u->u.receipts().stream())
                        .flatMap(u->u.itemsProperty().stream())
                        .filter(u->u.taxProperty().get() == tax)
                        .mapToDouble(e->e.totalProperty().get())
                        .sum();
            }
        }));
        return property;
    }

    FloatProperty getTotal(){
        FloatProperty property = new SimpleFloatProperty();
        days.forEach(e->property.bind(new FloatBinding() {
            {this.bind(e.date());}
            @Override
            protected float computeValue() {
                return (float) days.stream()
                        .flatMap(u->u.receipts().stream())
                        .flatMap(u->u.itemsProperty().stream())
                        .mapToDouble(e->e.totalProperty().get())
                        .sum();
            }
        }));
        return property;
    }

    StringProperty getMonth(){
        StringProperty property = new SimpleStringProperty();
        property.bind(new StringBinding() {
            {this.bind(dateSince);}
            @Override
            protected String computeValue() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                calendar.setTimeInMillis(days.get(0).date().get().getTime());
                return new SimpleDateFormat("MMMMMMMMMMM").format(calendar.getTime());
            }
        });
        return property;
    }

    void setDateSince(Date dateSince){ this.dateSince.set(dateSince); }

    public ReceiptsManagerModel() throws IOException {
        for(int i = 0; i < 7; i++){
            ObjectProperty<Date> currDateProp = new SimpleObjectProperty<>();
            int finalI = i;
            currDateProp.bind(new ObjectBinding<>() {
                {this.bind(dateSince);}
                @Override
                protected Date computeValue() {
                    return new Date((dateSince.get().getTime() + ((long) finalI * 24 * 60 * 60 * 1000)));
                }
            });
            days.add(new Day(currDateProp));
        }
        for(Day day : days){
            FXMLLoader loader = new FXMLLoader(ReceiptFieldController.class.getResource("ReceiptField.fxml"));
            loader.setControllerFactory(e->new ReceiptFieldController(day));
            dayFields.add(loader.load());
            ReceiptFieldController receiptFieldController = loader.getController();
            receiptFieldController.listListView.setOnMouseClicked(event -> {
                for(ListView<Receipt> listView : dayLists){
                    if(!listView.equals(receiptFieldController.listListView)) listView.getSelectionModel().clearSelection();
                }
                Receipt receipt = null;
                for(ListView<Receipt> listView : dayLists)
                    if (listView.getSelectionModel().getSelectedItem() != null)
                        receipt = listView.getSelectionModel().getSelectedItem();
                selected = receipt;
            });

            dayLists.add(receiptFieldController.listListView);
        }
        dateSince.set(new Date(System.currentTimeMillis()-(6*24 * 60 * 60 * 1000)));
    }

    public ReceiptsManagerModel(Date dateSince) throws IOException {
        this();
        this.dateSince.set(dateSince);
    }
}
