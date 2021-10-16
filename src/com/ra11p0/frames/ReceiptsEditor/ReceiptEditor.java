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

    private JPanel _receiptView = new JPanel();
        public JPanel get_receiptView() {return _receiptView;}
    private JPanel _management = new JPanel();
        public JPanel get_management() {return _management;}
    private static Receipt _receipt;
        public static Receipt get_receipt() {return _receipt;}
    private static ArrayList<Item> _items;
        public static ArrayList<Item> get_items() {return _items;}

    public ReceiptEditor(Receipt receipt, ArrayList<Item> items){
        _receipt = receipt;
        _items = items;
        generateTools();
        //SAVE ON CLOSE
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveOnClose();
                dispose();
            }
        });
        setLayout(new GridLayout(1, 2));
        setTitle("Receipt manager.");
        setSize(800, 300);
        setResizable(false);
        add(_management);
        add(_receiptView);
        setVisible(true);
        generateTable();
    }
    public ReceiptEditor(Receipt receipt, ArrayList<Item> items, boolean background){
            if(!background) {
                new ReceiptEditor(receipt, items);
                return;
            }
        _receipt = receipt;
        _items = items;
        generateTools();
        _receiptView.setVisible(true);
        generateTable();
    }
    private void generateTable(){
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane tablePane = new JScrollPane(table);
        _receiptView.setLayout(new FlowLayout());
        _receiptView.setBorder(BorderFactory.createLoweredBevelBorder());
        tableModel.addColumn("Item");
        tableModel.addColumn("QTY");
        tableModel.addColumn("Price");
        tableModel.addColumn("Tax");
        tableModel.addColumn("Total");
        for(ReceiptItem item : _receipt.get_items())
            tableModel.addRow(new String[] {
                item.get_Item().get_name(),
                String.format("%.2f", item.get_qty()),
                String.format("%.2f", item.get_Item().get_price()),
                String.format("%.2f", item.get_Item().get_taxRate()),
                String.format("%.2f", item.get_Item().get_price() * item.get_qty())
            });
        tablePane.setPreferredSize(new Dimension(400, 295));
        _receiptView.add(tablePane);
    }
    private void generateTools(){
        ReceiptEditor currentReceiptEditor = this;
        _management.setLayout(new GridLayout(6, 2));
        TextField storeName = new TextField(_receipt.get_store());
        storeName.setEditable(false);
        TextField ID = new TextField(_receipt.get_ID());
        ID.setEditable(false);
        TextField date = new TextField(_receipt.get_dateString());
        date.setEditable(false);
        TextField total = new TextField(String.format("%.2f", _receipt.get_paid()));
        total.setEditable(false);
        Button addItem = new Button("Add item.");
        //ADD ITEM
        addItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddItem.showDialog(_items, _receipt, currentReceiptEditor);
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
                RemoveItem.showDialog(_receipt, currentReceiptEditor);
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
                EditDate.showDialog(_receipt, currentReceiptEditor);
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
                    _receipt.saveReceipt();
                    JOptionPane.showMessageDialog(_management, _receipt.get_ID() + " saved!","Saved!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ID.setText(_receipt.get_ID());
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
        _management.add(new Label("Store: "));
        _management.add(storeName);
        _management.add(new Label("ID: "));
        _management.add(ID);
        _management.add(new Label("Date (YY-MM-DD): "));
        _management.add(date);
        _management.add(new Label("Total: "));
        _management.add(total);
        _management.add(addItem);
        _management.add(removeItem);
        _management.add(editDate);
        _management.add(save);
    }
    public void repaintFrame(){
        remove(_management);
        remove(_receiptView);
        _management = new JPanel();
        _receiptView = new JPanel();
        generateTools();
        setVisible(false);
        add(_management);
        add(_receiptView);
        setVisible(true);
        generateTable();
    }
    private static void saveOnClose(){

        if(!_receipt._changesMade)return;
        JFrame youSure = new JFrame("Save changes before exiting?");
        youSure.setLayout(new GridLayout(1, 2));
        youSure.setSize(300, 75);
        Button save = new Button("Save.");
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    _receipt.saveReceipt();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                youSure.setVisible(false);
                youSure.dispose();
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
                youSure.dispose();
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
