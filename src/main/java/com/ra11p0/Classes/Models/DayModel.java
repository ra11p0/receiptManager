package com.ra11p0.Classes.Models;

import com.ra11p0.Classes.Receipt;
import com.ra11p0.Classes.Interfaces.IDay;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Date;

public abstract class DayModel implements IDay {
    protected ObjectProperty<Date> date;

    protected final ListProperty<Receipt> receipts = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected final FloatProperty total = new SimpleFloatProperty();
}
