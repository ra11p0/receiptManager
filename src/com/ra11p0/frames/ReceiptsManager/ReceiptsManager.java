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
    private static JFrame _frame;
    private final static ArrayList<Receipt> _receipts = new ArrayList<>();
    private final static ArrayList<Item> _items = new ArrayList<>();
    public static void showDialog(){
        _frame = new JFrame("Select or create new receipt.");
        JComboBox<Receipt> receiptSelector = new JComboBox<>();
        Button _new = new Button("Create new receipt.");
        Button remove = new Button("Remove selected receipt.");
        Button edit = new Button("Modify selected receipt.");
        //GET ITEMS AND RECEIPTS
        getItemsAndReceipts();
        for(Receipt receipt : _receipts) receiptSelector.addItem(receipt);
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
                _frame.setVisible(false);
                _frame.dispose();
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
                _frame.setVisible(false);
                _frame.dispose();
            }
        });
        //*********
        _frame.setLayout(new GridLayout(5, 1));
        _frame.setSize(350, 200);
        _frame.add(new Label("Select receipt you want to modify, or create new one:"));
        _frame.add(receiptSelector);
        _frame.add(edit);
        _frame.add(remove);
        _frame.add(_new);
        _frame.setVisible(true);
    }
    private static void getItemsAndReceipts(){
        _items.clear();
        _receipts.clear();
        String[] receiptFiles = new File("res/receipts/").list();
        assert receiptFiles != null;
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (file.charAt(0) != '.') {
                    FileReader gsonFileReader = new FileReader("res/receipts/" + file);
                    Receipt receipt = gson.fromJson(gsonFileReader, Receipt.class);
                    gsonFileReader.close();
                    _receipts.add(receipt);
                    for (ReceiptItem item : receipt.get_items())
                        if (!_items.contains(item.get_Item())) _items.add(item.get_Item());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(_frame, e + " in: " + file,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        _items.sort(Comparator.comparing(Item::get_name));
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
                    JOptionPane.showMessageDialog(_frame, "Receipt "+ selectedReceipt.get_ID() +" removed!", "Information", JOptionPane.INFORMATION_MESSAGE);}
                else JOptionPane.showMessageDialog(_frame, "Receipt "+ selectedReceipt.get_ID() + " could not be removed!", "Information", JOptionPane.ERROR_MESSAGE);
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
        getItemsAndReceipts();
        return _receipts;
    }
    public static ArrayList<Item> getItems() {
        getItemsAndReceipts();
        return _items;
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
    public static boolean checkIfChangesMade(){
        ArrayList<Receipt> oldReceipts = new ArrayList<>();
        String[] receiptFiles = new File("res/.temp/receipts/").list();
        assert receiptFiles != null;
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (file.charAt(0) != '.') {
                    FileReader gsonFileReader = new FileReader("res/.temp/receipts/" + file);
                    Receipt receipt = gson.fromJson(gsonFileReader, Receipt.class);
                    gsonFileReader.close();
                    oldReceipts.add(receipt);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e + " in: " + file,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(_receipts.size() != oldReceipts.size()) return true;
        for(Receipt receipt : oldReceipts){
            if(!_receipts.contains(receipt)) return true;
        }
        return false;
    }
}
