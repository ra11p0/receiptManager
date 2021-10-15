package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import javafx.scene.control.cell.ComboBoxListCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class AddItem {
    public static void showDialog(ArrayList<Item> items, Receipt receipt, JPanel panel, JPanel tablePanel, ReceiptEditor receiptEditor){
        JFrame addItemFrame = new JFrame("Add item.");
        addItemFrame.setLayout(new GridLayout(8, 1));
        addItemFrame.setSize(350, 250);
        JComboBox<Item> itemsBox = new JComboBox<>();
        ArrayList<Item> itemsAtThisStore = new ArrayList<>();
        for(Item item : items) if (item.get_store().equals(receipt.get_store())) itemsAtThisStore.add(item);
        for(Item item : itemsAtThisStore) itemsBox.addItem(item);
        TextField qty = new TextField();
        TextField searchBar = new TextField();
        //SEARCH BAR
        searchBar.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                int i = 0;
                itemsBox.removeAllItems();
                if (searchBar.getText().length()==0){ for(Item item : itemsAtThisStore) itemsBox.addItem(item); i++;}
                else for(Item item : itemsAtThisStore) if(item.get_name().contains(searchBar.getText())) {itemsBox.addItem(item); i++;}
                if(i>0) searchBar.setBackground(Color.WHITE);
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
        qty.addTextListener(e1 -> {
            try{
                Float.parseFloat(qty.getText());
            }catch(Exception ex){
                qty.setText("");
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
                    receiptEditor.repaintFrame(panel, tablePanel, receipt, items);
                }
            }
        });
        Button confirm = new Button("Confirm.");
        //CONFIRM
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.addItem(new ReceiptItem((Item)itemsBox.getSelectedItem(), Float.parseFloat(qty.getText())));
                addItemFrame.setVisible(false);
                addItemFrame.dispose();
                receiptEditor.repaintFrame(panel, tablePanel, receipt, items);
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
        Button newItem = new Button("Create new item.");
        //NEW ITEM DIALOG
        newItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newItem(itemsBox, receipt);
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
        addItemFrame.add(new Label("Select item or create new one, then type quantity:"));
        addItemFrame.add(new Label("Search:"));
        addItemFrame.add(searchBar);
        addItemFrame.add(itemsBox);
        addItemFrame.add(new Label("Quantity:"));
        addItemFrame.add(qty);
        addItemFrame.add(newItem);
        addItemFrame.add(confirm);
        addItemFrame.setVisible(true);
    }
    private static void newItem(JComboBox<Item> itemsBox, Receipt receipt){
        JFrame createNewItemFrame = new JFrame("Create new item.");
        createNewItemFrame.setAlwaysOnTop(true);
        createNewItemFrame.setSize(200, 200);
        createNewItemFrame.setLayout(new FlowLayout());
        TextField name = new TextField();
        name.setPreferredSize(new Dimension(175, 25));
        JComboBox<Float> taxRate = new JComboBox<>();
        taxRate.addItem(0.23F);
        taxRate.addItem(0.08F);
        taxRate.addItem(0.05F);
        taxRate.setPreferredSize(new Dimension(80, 25));
        TextField cost = new TextField();
        //PRICE PARSE CHECK
        cost.addTextListener(e13 -> {
            try{
                Float.parseFloat(cost.getText());
            }catch(Exception ex){
                cost.setText("");
            }
        });
        //ENTER -> CONFIRM
        cost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    itemsBox.addItem(new Item(name.getText(), (Float)taxRate.getSelectedItem(), Float.parseFloat(cost.getText()), receipt.get_store()));
                    createNewItemFrame.setVisible(false);
                    createNewItemFrame.dispose();
                    itemsBox.setSelectedIndex(itemsBox.getItemCount()-1);
                }
            }
        });
        cost.setPreferredSize(new Dimension(50, 25));
        Button confirm = new Button("Confirm.");
        //CONFIRM
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                itemsBox.addItem(new Item(name.getText(), (Float)taxRate.getSelectedItem(), Float.parseFloat(cost.getText()), receipt.get_store()));
                createNewItemFrame.setVisible(false);
                createNewItemFrame.dispose();
                itemsBox.setSelectedIndex(itemsBox.getItemCount()-1);
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
        createNewItemFrame.add(new Label("Name of new item:"));
        createNewItemFrame.add(name);
        createNewItemFrame.add(new Label("Cost of one unit:"));
        createNewItemFrame.add(cost);
        createNewItemFrame.add(new Label("Tax rate:"));
        createNewItemFrame.add(taxRate);
        createNewItemFrame.add(confirm);
        createNewItemFrame.setVisible(true);
    }
}
