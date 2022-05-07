package com.ra11p0.Classes;

import com.ra11p0.App;
import com.ra11p0.Classes.Models.DayModel;
import com.ra11p0.Utils.DateUtils;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;

import java.util.Date;
import java.util.stream.Collectors;

public class Day extends DayModel {
    public ObjectProperty<Date> date(){return date;}

    public ListProperty<Receipt> receipts(){return receipts;}

    public FloatProperty total() { return total; }

    public Day(ObjectProperty<Date> date){
        this.date = date;
        this.date.addListener(e->{
            receipts.setAll(
                    App.dataAccessObject.get().stream()
                            .filter(o->DateUtils.equals(o.dateProperty().get(), date.get()))
                            .collect(Collectors.toList())
            );
            total.bind(new FloatBinding() {
                {
                    receipts.forEach(e->this.bind(e.totalProperty()));
                }
                @Override
                protected float computeValue() {
                    return (float) receipts.stream().mapToDouble(e->e.totalProperty().get()).sum();
                }
            });
        });

    }
}
