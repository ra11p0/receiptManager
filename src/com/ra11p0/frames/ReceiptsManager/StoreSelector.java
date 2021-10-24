package com.ra11p0.frames.ReceiptsManager;

import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class StoreSelector{
    public static JFrame getStoreDialog(){
        JFrame frame = new JFrame("Select store.");
        JComboBox<String> stores = new JComboBox<>();
        ArrayList<String> storesList = new ArrayList<>();
        JButton addNew = new JButton("Add new store.");
        JButton confirm = new JButton ("Confirm");
        //Get stores from all receipts
        for(Receipt receipt : ReceiptsManager.getReceipts()) if (!storesList.contains(receipt.get_store())) storesList.add(receipt.get_store());
        storesList.sort(Comparator.naturalOrder());
        for(String store : storesList) stores.addItem(store);
        //Confirm button
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        //Add new store button
        addNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddNewStore.showDialog(storesList, stores);
            }
        });
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(4, 1));
        frame.add(new Label("Select store:"));
        frame.add(stores);
        frame.add(addNew);
        frame.add(confirm);
        frame.setVisible(true);
        return frame;
    }
}
