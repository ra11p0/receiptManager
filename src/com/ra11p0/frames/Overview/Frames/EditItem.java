package com.ra11p0.frames.Overview.Frames;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.frames.HomeFrame;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class EditItem extends JFrame {
    private final static ResourceBundle locale = HomeFrame.localeBundle;
    public boolean anyChange = false;
    public Item oldItem = null;
    public Item newItem = null;
    public EditItem(ArrayList<Item> items){
        JPanel main = new JPanel(new GridLayout(8, 1));
        JComboBox<Item> itemsCombo = new JComboBox<>();
        JComboBox<Object> nameCombo = new JComboBox<>();
        JLabel priceLabel = new JLabel(locale.getString("typeNewPriceOfProduct") + ":");
        JTextField price = new JTextField();
        JButton confirm = new JButton(locale.getString("confirm"));
        JCheckBox changeNameForAll = new JCheckBox();
        ArrayList<String> namesOfProducts = new ArrayList<>();
        changeNameForAll.setText(locale.getString("changeNameForAllMatchingItems"));
        changeNameForAll.addActionListener(e->{
            if(changeNameForAll.isSelected()){
                price.setVisible(false);
                priceLabel.setVisible(false);
            }
            else{
                price.setVisible(true);
                priceLabel.setVisible(true);
            }
        });
        //INITIALIZE ITEMS COMBO
        items.sort((o1, o2) -> Float.compare(o1.get_price(), o2.get_price()));
        for(Item item : items) itemsCombo.addItem(item);
        itemsCombo.addItemListener(e -> {
            if(itemsCombo.getSelectedItem() == null) return;
            price.setText(String.format("%.2f", ((Item)itemsCombo.getSelectedItem()).get_price()));
            price.setText(price.getText().replaceAll(",", "."));
            nameCombo.setSelectedItem(((Item)itemsCombo.getSelectedItem()).get_name());
        });
        //INITIALIZE NAME COMBO
        //STRING COMPARATOR -> SORT ITEMS BY NAME
        items.sort((o1, o2) -> {
            Comparator<String> comparator = Comparator.comparing((String s) -> s);
            return comparator.compare(o1.get_name().toUpperCase(Locale.ROOT), o2.get_name().toUpperCase(Locale.ROOT));
        });
        for(Item item : items) if(!namesOfProducts.contains(item.get_name())) namesOfProducts.add(item.get_name());
        AutoCompleteSupport.install(nameCombo,
                GlazedLists.eventListOf(namesOfProducts.toArray()));
        nameCombo.setSelectedIndex(namesOfProducts.indexOf(((Item) Objects.requireNonNull(itemsCombo.getSelectedItem())).get_name()));
        //INITIALIZE PRICE
        price.setText(String.format("%.2f", ((Item) Objects.requireNonNull(itemsCombo.getSelectedItem())).get_price()));
        price.setText(price.getText().replaceAll(",", "."));
        //PRICE PARSE CHECK
        price.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try{
                    Float.parseFloat(price.getText());
                }catch(Exception ex){
                    price.setText("");
                }
            }
        });
        //CONFIRM BUTTON BEHAVIOR
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(price.getText().length() == 0 || nameCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, locale.getString("wrongInput"), locale.getString("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                oldItem = (Item) itemsCombo.getSelectedItem();
                if(changeNameForAll.isSelected()){
                    newItem = new Item(Objects.requireNonNull(nameCombo.getSelectedItem()).toString(),
                            oldItem.get_taxRate(),
                            0.0F,
                            "");
                }
                else {
                    newItem = new Item(Objects.requireNonNull(nameCombo.getSelectedItem()).toString(),
                            oldItem.get_taxRate(),
                            Float.parseFloat(price.getText()),
                            "");
                }
                ArrayList<Receipt> receiptsToSave = new ArrayList<>();
                for(Receipt receipt : ReceiptsManager.getReceipts()){
                    ArrayList<ReceiptItem> receiptItems = receipt.get_items();
                    for(ReceiptItem receiptItem : receiptItems){
                        if(receiptItem.get_Item().equals(newItem)){
                            Item _newItem;
                            if(newItem.get_price() == 0.0F){
                                _newItem = new Item(Objects.requireNonNull(nameCombo.getSelectedItem()).toString(),
                                        receiptItem.get_Item().get_taxRate(),
                                        receiptItem.get_Item().get_price(),
                                        receiptItem.get_Item().get_store());
                            }
                            else {
                                _newItem = new Item(Objects.requireNonNull(nameCombo.getSelectedItem()).toString(),
                                        receiptItem.get_Item().get_taxRate(),
                                        Float.parseFloat(price.getText()),
                                        receiptItem.get_Item().get_store());
                            }
                            ReceiptItem newReceiptItem = new ReceiptItem(_newItem, receiptItem.get_qty());
                            receipt.removeItem(receiptItem);
                            receipt.addItem(newReceiptItem);
                            receiptsToSave.add(receipt);
                            break;
                        }
                    }
                }
                for(Receipt receipt : receiptsToSave) {
                    try {
                        receipt.saveReceipt();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                anyChange = !newItem.equals(oldItem);
                setVisible(false);
                dispose();
            }
        });
        //*****
        main.add(new JLabel(locale.getString("selectItemToEdit") + ":"));
        main.add(itemsCombo);
        main.add(new JLabel(locale.getString("typeNewNameOfProduct") + ":"));
        main.add(nameCombo);
        main.add(priceLabel);
        main.add(price);
        main.add(changeNameForAll);
        main.add(confirm);
        //*****
        setResizable(false);
        setSize(250, 220);
        add(main);
        setVisible(true);
    }
}
