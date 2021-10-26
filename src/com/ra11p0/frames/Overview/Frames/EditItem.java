package com.ra11p0.frames.Overview.Frames;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class EditItem extends JFrame {
    public boolean anyChange = false;
    public Item oldItem = null;
    public Item newItem = null;
    public EditItem(ArrayList<Item> items){
        JPanel main = new JPanel(new GridLayout(7, 1));
        JComboBox<Item> itemsCombo = new JComboBox<>();
        JComboBox<Object> nameCombo = new JComboBox<>();
        JTextField price = new JTextField();
        JButton confirm = new JButton("Confirm");
        ArrayList<String> namesOfProducts = new ArrayList<>();
        //INITIALIZE ITEMS COMBO
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Float.compare(o1.get_price(), o2.get_price());
            }
        });
        for(Item item : items) itemsCombo.addItem(item);
        itemsCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(itemsCombo.getSelectedItem() == null) return;
                price.setText(String.format("%.2f", ((Item)itemsCombo.getSelectedItem()).get_price()));
                price.setText(price.getText().replaceAll(",", "."));
                nameCombo.setSelectedItem(((Item)itemsCombo.getSelectedItem()).get_name());
            }
        });
        //INITIALIZE NAME COMBO
        //STRING COMPARATOR -> SORT ITEMS BY NAME
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                Comparator<String> comparator = Comparator.comparing((String s) -> s);
                return comparator.compare(o1.get_name().toUpperCase(Locale.ROOT), o2.get_name().toUpperCase(Locale.ROOT));
            }
        });
        for(Item item : items) if(!namesOfProducts.contains(item.get_name())) namesOfProducts.add(item.get_name());
        AutoCompleteSupport.install(nameCombo,
                GlazedLists.eventListOf(namesOfProducts.toArray()));
        nameCombo.setSelectedIndex(0);
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
                    JOptionPane.showMessageDialog(null, "Wrong input!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                for(Receipt receipt : ReceiptsManager.getReceipts()){
                    ArrayList<ReceiptItem> receiptItems = receipt.get_items();
                    for(ReceiptItem receiptItem : receiptItems){
                        if(receiptItem.get_Item().equals(itemsCombo.getSelectedItem())){
                            Item _newItem = new Item(Objects.requireNonNull(nameCombo.getSelectedItem()).toString(),
                                    receiptItem.get_Item().get_taxRate(),
                                    Float.parseFloat(price.getText()),
                                    receiptItem.get_Item().get_store());
                            ReceiptItem newReceiptItem = new ReceiptItem(_newItem, receiptItem.get_qty());
                            oldItem = receiptItem.get_Item();
                            newItem = _newItem;
                            receipt.removeItem(receiptItem);
                            receipt.addItem(newReceiptItem);
                            try {
                                receipt.saveReceipt();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                anyChange = true;
                setVisible(false);
                dispose();
            }
        });
        //*****
        main.add(new JLabel("Select item to edit:"));
        main.add(itemsCombo);
        main.add(new JLabel("Type new name of product:"));
        main.add(nameCombo);
        main.add(new JLabel("Type new price of product:"));
        main.add(price);
        main.add(confirm);
        //*****
        setResizable(false);
        setSize(250, 220);
        add(main);
        setVisible(true);
    }
}
