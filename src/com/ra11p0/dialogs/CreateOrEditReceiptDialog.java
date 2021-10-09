package com.ra11p0.dialogs;

import com.google.gson.Gson;
import com.ra11p0.frames.ManageReceiptFrame;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class CreateOrEditReceiptDialog {
    public CreateOrEditReceiptDialog(){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        ArrayList<Item> items = new ArrayList<Item>();
        String[] receiptFiles = new File("res/receipts/").list();
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                Receipt receipt = gson.fromJson(new FileReader("res/receipts/" + file), Receipt.class);
                receipts.add(receipt);
                for(ReceiptItem item : receipt.get_items()) if (!items.contains(item.get_Item())) items.add(item.get_Item());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        JFrame frame = new JFrame("Select or create new receipt.");
        JComboBox<Receipt> receiptSelector = new JComboBox<Receipt>();
        for(Receipt receipt : receipts) receiptSelector.addItem(receipt);
        Button _new = new Button("Create new receipt.");
        _new.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ManageReceiptFrame(items);
                frame.setVisible(false);
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
        Button edit = new Button("Modify existing receipt.");
        edit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ManageReceiptFrame((Receipt)receiptSelector.getSelectedItem(), items);
                frame.setVisible(false);
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
        //*********
        frame.setLayout(new GridLayout(4, 1));
        frame.setSize(350, 200);
        frame.add(new Label("Select receipt you want to modify, or create new one:"));
        frame.add(receiptSelector);
        frame.add(edit);
        frame.add(_new);
        frame.setVisible(true);
    }
}
