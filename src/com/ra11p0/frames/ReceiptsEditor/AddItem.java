package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AddItem {
    public static void showDialog(ArrayList<Item> items, Receipt receipt, JPanel panel, JPanel tablePanel, ReceiptEditor receiptEditor){
        JFrame addItemFrame = new JFrame("Add item.");
        addItemFrame.setLayout(new GridLayout(5, 1));
        addItemFrame.setSize(350, 250);
        JComboBox<Item> itemsBox = new JComboBox<>();
        for(Item item : items) if (item.get_store().equals(receipt.get_store())) itemsBox.addItem(item);
        TextField qty = new TextField();
        //PARSE CHECK
        qty.addTextListener(e1 -> {
            try{
                Float.parseFloat(qty.getText());
            }catch(Exception ex){
                qty.setText("");
            }
        });
        Button confirm = new Button("Confirm.");
        //CONFIRM
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                receipt.addItem(new ReceiptItem((Item)itemsBox.getSelectedItem(), Float.parseFloat(qty.getText())));
                addItemFrame.setVisible(false);
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
        //New item dialog
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
        addItemFrame.add(itemsBox);
        addItemFrame.add(qty);
        addItemFrame.add(newItem);
        addItemFrame.add(confirm);
        addItemFrame.setVisible(true);
    }
    private static void newItem(JComboBox<Item> itemsBox, Receipt receipt){
        JFrame createNewItemFrame = new JFrame("Create new item.");
        createNewItemFrame.setSize(200, 200);
        createNewItemFrame.setLayout(new FlowLayout());
        TextField name = new TextField();
        name.setPreferredSize(new Dimension(175, 25));
        TextField taxRate = new TextField();
        taxRate.addTextListener(e12 -> {
            try{
                Float.parseFloat(taxRate.getText());
            }catch(Exception ex){
                taxRate.setText("");
            }
        });
        taxRate.setPreferredSize(new Dimension(50, 25));
        TextField cost = new TextField();
        cost.addTextListener(e13 -> {
            try{
                Float.parseFloat(cost.getText());
            }catch(Exception ex){
                cost.setText("");
            }
        });
        cost.setPreferredSize(new Dimension(50, 25));
        Button confirm = new Button("Confirm.");
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                itemsBox.addItem(new Item(name.getText(), Float.parseFloat(taxRate.getText()), Float.parseFloat(cost.getText()), receipt.get_store()));
                createNewItemFrame.setVisible(false);
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
