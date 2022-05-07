package com.ra11p0.Models;

import com.ra11p0.Classes.Receipt;
import com.ra11p0.Interfaces.IDay;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Date;

public abstract class DayModel implements IDay {
    protected ObjectProperty<Date> date;

    protected ListProperty<Receipt> receipts = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected FloatProperty total = new SimpleFloatProperty();
}
