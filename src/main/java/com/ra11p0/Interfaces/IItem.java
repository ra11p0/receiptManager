package com.ra11p0.Interfaces;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.StringProperty;

public interface IItem {
    StringProperty name();

    StringProperty store();

    FloatProperty tax();

    FloatProperty price();
}
