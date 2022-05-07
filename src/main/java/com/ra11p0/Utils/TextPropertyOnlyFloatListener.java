package com.ra11p0.Utils;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Objects;

public class TextPropertyOnlyFloatListener implements ChangeListener<String> {
    StringProperty base;
    public TextPropertyOnlyFloatListener(StringProperty base) {this.base = base;}
    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (Objects.equals(newValue, "")) return;
        try {
            Float.parseFloat(newValue);
        } catch (Exception ex) {
            base.set(oldValue);
        }
    }
}
