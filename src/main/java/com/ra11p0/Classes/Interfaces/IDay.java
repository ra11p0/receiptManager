package com.ra11p0.Classes.Interfaces;

import com.ra11p0.Classes.Receipt;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;

import java.util.Date;

public interface IDay {
    ObjectProperty<Date> date();

    ListProperty<Receipt> receipts();

    FloatProperty total();
}
