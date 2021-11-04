package com.ra11p0.structures;

import java.util.ArrayList;

public class ReceiptsPacket {
    public ArrayList<Receipt> receipts = new ArrayList<>();
    public ReceiptsPacket(ArrayList<Receipt> _receipts){
        receipts = _receipts;
    }
}
