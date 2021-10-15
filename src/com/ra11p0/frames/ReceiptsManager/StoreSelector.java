package com.ra11p0.frames.ReceiptsManager;

import com.google.gson.Gson;
import com.ra11p0.frames.ReceiptsEditor.ReceiptEditor;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class StoreSelector{
    public static void showDialog(ArrayList<Item> items){
        JFrame frame = new JFrame("Select store.");
        JComboBox<String> stores = new JComboBox<>();
        ArrayList<String> storesList = new ArrayList<>();
        //Get stores from all receipts
        String[] receiptFiles = new File("res/receipts/").list();
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (file.charAt(0) != '.') {
                    FileReader gsonFileReader = new FileReader("res/receipts/" + file);
                    Receipt receipt = gson.fromJson(gsonFileReader, Receipt.class);
                    gsonFileReader.close();
                    if (!storesList.contains(receipt.get_store())) storesList.add(receipt.get_store());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        for(String store : storesList) stores.addItem(store);
        Button confirm = new Button ("Confirm");
        //Confirm button
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ReceiptEditor(new Receipt(stores.getSelectedItem().toString(), new Date(System.currentTimeMillis())), items);
                frame.setVisible(false);
                frame.dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        Button addNew = new Button("Add new store.");
        //Add new store button
        addNew.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddNewStore.showDialog(storesList, stores);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(4, 1));
        frame.add(new Label("Select store:"));
        frame.add(stores);
        frame.add(addNew);
        frame.add(confirm);
        frame.setVisible(true);
    }
}
