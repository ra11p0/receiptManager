package com.ra11p0.GUI.ReceiptsManager.Panes.ReceiptField;

import com.ra11p0.App;
import com.ra11p0.Classes.Day;
import com.ra11p0.Classes.Receipt;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.util.Calendar;
import java.util.Date;

public abstract class ReceiptFieldModel {
    StringProperty titleLabelText = new SimpleStringProperty();

    ListProperty<Receipt> receipts = new SimpleListProperty<>(FXCollections.observableArrayList());

    StringProperty descriptionLabelText = new SimpleStringProperty();

    public ReceiptFieldModel(Day day) {
        this.titleLabelText.bind(new StringBinding() {
            {this.bind(day.date());}
            @Override
            protected String computeValue() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(day.date().get());
                return String.format("%s - %s - %s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });
        this.receipts.bind(day.receipts());
        this.descriptionLabelText.bind(day.total().asString().concat(" " + App.settings.currency));
    }
}
