package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ReceiptEditor extends JFrame {
    public ReceiptEditor(Receipt receipt, ArrayList<Item> items){
        //SAVE ON CLOSE
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveOnClose(receipt);
            }
        });
        JPanel _receiptView = new JPanel();
        JPanel _management = new JPanel();
        generateTools(_management, _receiptView, receipt, items);
        setLayout(new GridLayout(1, 2));
        setTitle("Receipt manager.");
        setSize(800, 300);
        setResizable(false);
        add(_management);
        add(_receiptView);
        setVisible(true);
        generateTable(_receiptView, receipt);
    }
    private void generateTable(JPanel panel, Receipt receipt){
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Item");
        tableModel.addColumn("QTY");
        tableModel.addColumn("Price");
        tableModel.addColumn("Tax");
        tableModel.addColumn("Total");
        JTable table = new JTable(tableModel);
        JScrollPane tablePane = new JScrollPane(table);
        for(ReceiptItem item : receipt.get_items())
            tableModel.addRow(new String[] {
                item.get_Item().get_name(),
                String.format("%.2f", item.get_qty()),
                String.format("%.2f", item.get_Item().get_price()),
                String.format("%.2f", item.get_Item().get_taxRate()),
                String.format("%.2f", item.get_Item().get_price() * item.get_qty())
            });
        tablePane.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()-5));
        panel.add(tablePane);
    }
    private void generateTools(JPanel panel, JPanel tablePanel, Receipt receipt, ArrayList<Item> items){
        ReceiptEditor currentReceiptEditor = this;
        panel.setLayout(new GridLayout(6, 2));
        TextField storeName = new TextField(receipt.get_store());
        storeName.setEditable(false);
        TextField ID = new TextField(receipt.get_ID());
        ID.setEditable(false);
        TextField date = new TextField(receipt.get_dateString());
        date.setEditable(false);
        TextField total = new TextField(String.format("%.2f", receipt.get_paid()));
        total.setEditable(false);
        Button addItem = new Button("Add item.");
        //ADD ITEM
        addItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddItem.showDialog(items, receipt, panel, tablePanel, currentReceiptEditor);
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
        Button removeItem = new Button("Remove item.");
        //REMOVE ITEM
        removeItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RemoveItem.showDialog(items, receipt, panel, tablePanel, currentReceiptEditor);
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
        Button editDate = new Button("Edit date.");
        //EDIT DATE
        editDate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditDate.showDialog(items, receipt, panel, tablePanel, currentReceiptEditor);
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
        Button save = new Button("Save.");
        //SAVE
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    receipt.saveReceipt();
                    JOptionPane.showMessageDialog(panel, receipt.get_ID() + " saved!","Saved!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        //*****
        panel.add(new Label("Store: "));
        panel.add(storeName);
        panel.add(new Label("ID: "));
        panel.add(ID);
        panel.add(new Label("Date (YY-MM-DD): "));
        panel.add(date);
        panel.add(new Label("Total: "));
        panel.add(total);
        panel.add(addItem);
        panel.add(removeItem);
        panel.add(editDate);
        panel.add(save);
    }
    public void repaintFrame(JPanel panel, JPanel tablePanel, Receipt receipt, ArrayList<Item> items){
        remove(panel);
        remove(tablePanel);
        panel = new JPanel();
        tablePanel = new JPanel();
        generateTools(panel, tablePanel, receipt, items);
        setVisible(false);
        add(panel);
        add(tablePanel);
        setVisible(true);
        generateTable(tablePanel, receipt);
    }
    public static void saveOnClose(Receipt receipt){

        if(!receipt._changesMade)return;
        JFrame youSure = new JFrame("Save changes before exiting?");
        youSure.setLayout(new GridLayout(1, 2));
        youSure.setSize(300, 75);
        Button save = new Button("Save.");
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    receipt.saveReceipt();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        Button discard = new Button("Discard");
        discard.addMouseListener(new MouseListener() {
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
        youSure.add(save);
        youSure.add(discard);
        youSure.setVisible(true);
    }
}
