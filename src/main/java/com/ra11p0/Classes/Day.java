package com.ra11p0.Classes;

import com.ra11p0.App;
import com.ra11p0.Classes.Receipt;
import com.ra11p0.Models.DayModel;
import com.ra11p0.Utils.DateUtils;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

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
                            .filter(o->DateUtils.equals(o.date().get(), date.get()))
                            .collect(Collectors.toList())
            );
            total.bind(new FloatBinding() {
                {
                    receipts.forEach(e->this.bind(e.total()));
                }
                @Override
                protected float computeValue() {
                    return (float) receipts.stream().mapToDouble(e->e.total().get()).sum();
                }
            });
        });

    }
}
