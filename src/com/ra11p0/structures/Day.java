package com.ra11p0.structures;

import java.util.ArrayList;
import java.util.Date;

public class Day {
    private final Date _date;
    public Date get_date() {
        return _date;
    }
    private final ArrayList<Receipt> _receipts;
    public ArrayList<Receipt> get_receipts() {
        return _receipts;
    }
    private float _total = 0F;
    public float get_total() {
        return _total;
    }
    public Day(Date date, ArrayList<Receipt> receipts){
        _date = date;
        _receipts = receipts;
        if(receipts == null) return;
        for(Receipt receipt : receipts){
            _total+=receipt.get_paid();
        }
    }
}
