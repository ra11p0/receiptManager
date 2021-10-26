package com.ra11p0.frames.ReceiptsManager;

import com.google.gson.Gson;
import com.ra11p0.frames.ReceiptsEditor.ReceiptEditor;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class ReceiptsManager {

    private static JFrame frame;
    private final static ArrayList<Receipt> receipts = new ArrayList<>();
    private final static ArrayList<Item> items = new ArrayList<>();
    public static void showDialog(){
        frame = new JFrame("Select or create new receipt.");
        JComboBox<Receipt> receiptSelector = new JComboBox<>();
        Button _new = new Button("Create new receipt.");
        Button remove = new Button("Remove selected receipt.");
        Button edit = new Button("Modify selected receipt.");
        //GET ITEMS AND RECEIPTS
        getItemsAndReceipts();
        for(Receipt receipt : receipts) receiptSelector.addItem(receipt);
        //STORE SELECTOR DIALOG
        _new.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame storeSelectorFrame = StoreSelector.getStoreDialog();
                storeSelectorFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Object stores = null;
                        for(Component component : storeSelectorFrame.getRootPane().getContentPane().getComponents()) if (component.getClass() == JComboBox.class) stores = component;
                        assert stores != null;
                        @SuppressWarnings("unchecked")
                        String selectedStore = Objects.requireNonNull(((JComboBox<String>) stores).getSelectedItem()).toString();
                        new ReceiptEditor(new Receipt(selectedStore, new Date(System.currentTimeMillis())));
                    }
                });
                frame.setVisible(false);
                frame.dispose();
            }
        });
        //REMOVE RECEIPT DIALOG
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeReceipt(receiptSelector);
            }
        });
        //EDIT RECEIPT DIALOG
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ReceiptEditor((Receipt)receiptSelector.getSelectedItem());
                frame.setVisible(false);
                frame.dispose();
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
    private static void getItemsAndReceipts(){
        items.clear();
        receipts.clear();
        String[] receiptFiles = new File("res/receipts/").list();
        assert receiptFiles != null;
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (file.charAt(0) != '.') {
                    FileReader gsonFileReader = new FileReader("res/receipts/" + file);
                    Receipt receipt = gson.fromJson(gsonFileReader, Receipt.class);
                    gsonFileReader.close();
                    receipts.add(receipt);
                    for (ReceiptItem item : receipt.get_items())
                        if (!items.contains(item.get_Item())) items.add(item.get_Item());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e + " in: " + file,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        items.sort(Comparator.comparing(Item::get_name));
    }
    private static void removeReceipt(JComboBox<Receipt> receiptSelector){
        Receipt selectedReceipt = (Receipt)receiptSelector.getSelectedItem();
        JFrame youSure = new JFrame("Are you sure?");
        youSure.setLayout(new GridLayout(1, 2));
        youSure.setSize(375, 75);
        assert selectedReceipt != null;
        Button imSure = new Button("Remove " +  selectedReceipt.get_ID() + ".");
        imSure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean deleteStatus = selectedReceipt.deleteReceipt();
                if (deleteStatus){
                    receiptSelector.removeItem(selectedReceipt);
                    JOptionPane.showMessageDialog(frame, "Receipt "+ selectedReceipt.get_ID() +" removed!", "Information", JOptionPane.INFORMATION_MESSAGE);}
                else JOptionPane.showMessageDialog(frame, "Receipt "+ selectedReceipt.get_ID() + " could not be removed!", "Information", JOptionPane.ERROR_MESSAGE);
                youSure.setVisible(false);
                youSure.dispose();
            }
        });
        Button notSure = new Button("Do not remove.");
        notSure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                youSure.setVisible(false);
                youSure.dispose();
            }
        });
        youSure.add(imSure);
        youSure.add(notSure);
        youSure.setVisible(true);
    }
    public static ArrayList<Receipt> getReceipts() {
        items.clear();
        getItemsAndReceipts();
        return receipts;
    }
    public static ArrayList<Item> getItems() {
        items.clear();
        getItemsAndReceipts();
        return items;
    }
    public static ArrayList<Receipt> getReceiptsContaining(ArrayList<Item> items){
        ArrayList<Receipt> receipts = new ArrayList<>();
        for(Receipt receipt : ReceiptsManager.getReceipts()){
            for(Item item : items){
                for(ReceiptItem receiptItem : receipt.get_items())
                {
                    if(receiptItem.get_Item().get_name().equals(item.get_name()) && !receipts.contains(receipt)) receipts.add(receipt);
                }
            }
        }
        return receipts;
    }
}
