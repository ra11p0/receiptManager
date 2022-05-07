package com.ra11p0.SharedTypes;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.util.Map;

public class MapProperty3<T1, T2, T3> extends SimpleMapProperty<T1, Map.Entry<T2, T3>> {

    public MapProperty3() { super(FXCollections.observableHashMap()); }
}
