package com.ra11p0.frames.ReceiptsEditor;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import com.ra11p0.utils.LangResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AddItem {
    public static void showDialog(ArrayList<Item> items, Receipt receipt, ReceiptEditor receiptEditor){
        JFrame addItemFrame = new JFrame(LangResource.get("addItem"));
        JComboBox<Item> itemsBox = new JComboBox<>();
        ArrayList<Item> itemsAtThisStore = new ArrayList<>();
        JTextField qty = new JTextField();
        JTextField searchBar = new JTextField();
        JButton confirm = new JButton(LangResource.get("confirm"));
        addItemFrame.setLayout(new GridLayout(8, 1));
        addItemFrame.setSize(350, 250);
        for(Item item : items) if (item.get_store().equals(receipt.get_store())) itemsAtThisStore.add(item);
        for(Item item : itemsAtThisStore) itemsBox.addItem(item);
        //SEARCH BAR BEHAVIOR
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int i = 0;
                itemsBox.removeAllItems();
                if (searchBar.getText().length()==0){ for(Item item : itemsAtThisStore) itemsBox.addItem(item); i++;}
                else for(Item item : itemsAtThisStore) if(item.get_name().toUpperCase(Locale.ROOT).contains(searchBar.getText().toUpperCase(Locale.ROOT))) {itemsBox.addItem(item); i++;}
                if(i>0) searchBar.setBackground(null);
                else searchBar.setBackground(Color.RED);
                itemsBox.setPopupVisible(false);
                itemsBox.showPopup();
                itemsBox.setPopupVisible(true);
            }
        });
        //ENTER -> SELECT ITEM FROM COMBO BOX
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    itemsBox.hidePopup();
                    qty.requestFocus();
                }
            }
        });
        //QTY PARSE CHECK
        qty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try{
                    Float.parseFloat(qty.getText());
                }catch(Exception ex){
                    qty.setText("");
                }
            }
        });
        //ENTER -> CONFIRM
        qty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    receipt.addItem(new ReceiptItem((Item)itemsBox.getSelectedItem(), Float.parseFloat(qty.getText())));
                    addItemFrame.setVisible(false);
                    addItemFrame.dispose();
                    receiptEditor.repaintFrame();
                }
            }
        });
        //CONFIRM
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.addItem(new ReceiptItem((Item)itemsBox.getSelectedItem(), Float.parseFloat(qty.getText())));
                addItemFrame.setVisible(false);
                addItemFrame.dispose();
                receiptEditor.repaintFrame();
            }
        });
        JButton newItem = new JButton(LangResource.get("createNewItem"));
        //NEW ITEM DIALOG
        newItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newItem(itemsBox, receipt, items);
            }
        });
        addItemFrame.add(new Label(LangResource.get("selectItemOrCreateNewOneThenTypeQty") + ":"));
        addItemFrame.add(new Label(LangResource.get("search") + ":"));
        addItemFrame.add(searchBar);
        addItemFrame.add(itemsBox);
        addItemFrame.add(new Label(LangResource.get("qty") + ":"));
        addItemFrame.add(qty);
        addItemFrame.add(newItem);
        addItemFrame.add(confirm);
        addItemFrame.setVisible(true);
    }
    private static void newItem(JComboBox<Item> itemsBox, Receipt receipt, ArrayList<Item> items) throws NullPointerException{
        JFrame createNewItemFrame = new JFrame(LangResource.get("createNewItem"));
        ArrayList<String> namesOfProducts = new ArrayList<>();
        JComboBox<Object> name = new JComboBox<>();
        JComboBox<Float> taxRate = new JComboBox<>();
        JTextField cost = new JTextField();
        JButton confirm = new JButton(LangResource.get("confirm"));
        //*****
        createNewItemFrame.setAlwaysOnTop(true);
        createNewItemFrame.setSize(220, 200);
        createNewItemFrame.setLayout(new FlowLayout());
        //NAME COMBO BEHAVIOR
        for(Item item : items)
            if(!namesOfProducts.contains(item.get_name()))
                namesOfProducts.add(item.get_name());
        AutoCompleteSupport.install(name,
                GlazedLists.eventListOf(namesOfProducts.toArray()));
        name.setPreferredSize(new Dimension(175, 25));
        name.addItemListener(e -> {
            if(name.getSelectedItem() == null) return;
            for(Item _item : ReceiptsManager._items)
                if(_item.get_name().equals(name.getSelectedItem())){
                    taxRate.setSelectedItem(_item.get_taxRate());
                    break;
                }
        });
        //TAX RATE BEHAVIOR
        taxRate.addItem(0.23F);
        taxRate.addItem(0.08F);
        taxRate.addItem(0.05F);
        taxRate.addItem(0F);
        taxRate.setPreferredSize(new Dimension(80, 25));
        taxRate.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            Component component = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            renderer.setText(String.format("%.0f", value*100) + "%");
            return component;
        });
        //PRICE PARSE CHECK
        cost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try{
                    Float.parseFloat(cost.getText());
                }catch(Exception ex){
                    cost.setText("");
                }
            }
        });
        //ENTER -> CONFIRM
        cost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    itemsBox.addItem(new Item(Objects.requireNonNull(name.getSelectedItem()).toString(),
                            Objects.requireNonNull((Float)taxRate.getSelectedItem()),
                            Float.parseFloat(cost.getText()),
                            receipt.get_store()));
                    createNewItemFrame.setVisible(false);
                    createNewItemFrame.dispose();
                    itemsBox.setSelectedIndex(itemsBox.getItemCount()-1);
                }
            }
        });
        cost.setPreferredSize(new Dimension(50, 25));
        //CONFIRM
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                itemsBox.addItem(new Item(Objects.requireNonNull(name.getSelectedItem()).toString(),
                        Objects.requireNonNull((Float)taxRate.getSelectedItem()),
                        Float.parseFloat(cost.getText()),
                        receipt.get_store()));
                createNewItemFrame.setVisible(false);
                createNewItemFrame.dispose();
                itemsBox.setSelectedIndex(itemsBox.getItemCount()-1);
            }
        });
        //*****
        createNewItemFrame.add(new Label(LangResource.get("nameOfNewItem") + ":"));
        createNewItemFrame.add(name);
        createNewItemFrame.add(new Label(LangResource.get("costOfOneUnit") + ":"));
        createNewItemFrame.add(cost);
        createNewItemFrame.add(new Label(LangResource.get("taxRate") + ":"));
        createNewItemFrame.add(taxRate);
        createNewItemFrame.add(confirm);
        createNewItemFrame.setVisible(true);
    }
}
