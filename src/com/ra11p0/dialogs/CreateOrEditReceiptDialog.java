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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateOrEditReceiptDialog {
    public CreateOrEditReceiptDialog(){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        ArrayList<Item> items = new ArrayList<Item>();
        String[] receiptFiles = new File("res/receipts/").list();
        JFrame frame = new JFrame("Select or create new receipt.");
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (!file.substring(0, 1).equals(".")) {
                    FileReader gsonFileReader = new FileReader("res/receipts/" + file);
                    Receipt receipt = gson.fromJson(gsonFileReader, Receipt.class);
                    gsonFileReader.close();
                    receipts.add(receipt);
                    for (ReceiptItem item : receipt.get_items())
                        if (!items.contains(item.get_Item())) items.add(item.get_Item());
                }
            } catch (Exception e) {
                new JOptionPane().showMessageDialog(frame, e + " in: " + file,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        items.sort((o1, o2) -> o1.get_name().compareTo(o2.get_name()));
        JComboBox<Receipt> receiptSelector = new JComboBox<Receipt>();
        for(Receipt receipt : receipts) receiptSelector.addItem(receipt);
        Button _new = new Button("Create new receipt.");
        //NEW RECEIPT DIALOG
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
        Button remove = new Button("Remove selected receipt.");
        //REMOVE DIALOG
        remove.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Receipt selectedReceipt = (Receipt)receiptSelector.getSelectedItem();
                JFrame youSure = new JFrame("Are you sure?");
                youSure.setLayout(new GridLayout(1, 2));
                youSure.setSize(375, 75);
                Button imSure = new Button("Remove " +  selectedReceipt.get_ID() + ".");
                imSure.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        File receiptFile = new File("res/receipts/" + selectedReceipt.get_ID() + ".json");
                        boolean deleteStatus = receiptFile.delete();
                        if (deleteStatus){
                            receiptSelector.removeItem(selectedReceipt);
                            new JOptionPane().showMessageDialog(frame, "Receipt "+ selectedReceipt.get_ID() +" removed!", "Information", JOptionPane.INFORMATION_MESSAGE);}
                        else new JOptionPane().showMessageDialog(frame, "Receipt "+ selectedReceipt.get_ID() + " could not be removed!", "Information", JOptionPane.ERROR_MESSAGE);
                        youSure.setVisible(false);
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
                Button notSure = new Button("Do not remove.");
                notSure.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        youSure.setVisible(false);
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
                youSure.add(imSure);
                youSure.add(notSure);
                youSure.setVisible(true);

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
        Button edit = new Button("Modify selected receipt.");
        //EDIT DIALOG
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
        frame.setLayout(new GridLayout(5, 1));
        frame.setSize(350, 200);
        frame.add(new Label("Select receipt you want to modify, or create new one:"));
        frame.add(receiptSelector);
        frame.add(edit);
        frame.add(remove);
        frame.add(_new);
        frame.setVisible(true);
    }
}
