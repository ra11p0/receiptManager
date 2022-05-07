package com.ra11p0.Classes;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SelectableItemNode<T> extends BorderPane {
    final CheckBox checkBox = new CheckBox();

    final T t;

    public boolean isSelected() { return checkBox.isSelected(); }

    public void setSelected(boolean isSelected) {checkBox.setSelected(isSelected);}

    public T get() { return t; }

    public SelectableItemNode(T t) {
        this.t = t;
        Label label = new Label(t.toString());
        setRight(checkBox);
        setLeft(label);
    }

    @Override
    public boolean equals(Object obj) { return this == obj; }

    @Override
    public String toString() {
        return t.toString();
    }
}
