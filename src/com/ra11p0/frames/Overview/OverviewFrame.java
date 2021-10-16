package com.ra11p0.frames.Overview;

import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.util.ArrayList;

public class OverviewFrame extends JFrame {
    private final ArrayList<Receipt> receipts = ReceiptsManager.getReceipts();
    private final ArrayList<Item> items = ReceiptsManager.getItems();
    private final JPanel editorPanel = new JPanel();
    public OverviewFrame(){
        //setResizable(false);
        setSize(800, 650);
        add(new OverviewPanel());
        setVisible(true);
    }
    public void showEditor(){

    }
}
