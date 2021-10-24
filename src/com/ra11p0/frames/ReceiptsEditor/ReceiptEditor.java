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
        //public JPanel get_receiptView() {return _receiptView;}
    private JPanel _management = new JPanel();
        //public JPanel get_management() {return _management;}
    private final JPanel _editorPanel = new JPanel();
        public JPanel get_editorPanel() {return _editorPanel;}
    private static Receipt _receipt;
        //public static Receipt get_receipt() {return _receipt;}
    private static ArrayList<Item> _items;
        //public static ArrayList<Item> get_items() {return _items;}

    public ReceiptEditor(Receipt receipt, ArrayList<Item> items){
        _receipt = receipt;
        _items = items;
        generateTools();
        //SAVE ON CLOSE
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveOnClose();
            }
        });
        _editorPanel.setLayout(new GridLayout(1, 2));
        add(_editorPanel);
        setTitle("Receipt manager.");
        setSize(800, 350);
        setResizable(false);
        _editorPanel.add(_management);
        _editorPanel.add(_receiptView);
        setVisible(true);
        generateTable();
    }
    public ReceiptEditor(Receipt receipt, ArrayList<Item> items, boolean background){
            if(!background) {
                new ReceiptEditor(receipt, items);
                return;
            }
        _editorPanel.setLayout(new GridLayout(1, 2));
        _receipt = receipt;
        _items = items;
        generateTools();
        _receiptView.setVisible(true);
        generateTable();
        _editorPanel.add(_management);
        _editorPanel.add(_receiptView);
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
        tablePane.setPreferredSize(new Dimension(380, 295));
        _receiptView.add(tablePane);
    }
    private void generateTools(){
        ReceiptEditor currentReceiptEditor = this;
        _management.setLayout(new GridLayout(6, 2));
        JTextField storeName = new JTextField(_receipt.get_store());
        storeName.setEditable(false);
        JTextField ID = new JTextField(_receipt.get_ID());
        ID.setEditable(false);
        JTextField date = new JTextField(_receipt.get_dateString());
        date.setEditable(false);
        JTextField total = new JTextField(String.format("%.2f", _receipt.get_paid()));
        total.setEditable(false);
        JButton addItem = new JButton("Add item.");
        //ADD ITEM
        addItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddItem.showDialog(_items, _receipt, currentReceiptEditor);
            }
        });
        JButton removeItem = new JButton("Remove item.");
        //REMOVE ITEM
        removeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RemoveItem.showDialog(_receipt, currentReceiptEditor);
            }
        });
        JButton editDate = new JButton("Edit date.");
        //EDIT DATE
        editDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditDate.showDialog(_receipt, currentReceiptEditor);
            }
        });
        JButton save = new JButton("Save.");
        //SAVE
        save.addMouseListener(new MouseAdapter() {
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
        _editorPanel.remove(_management);
        _editorPanel.remove(_receiptView);
        _management = new JPanel();
        _receiptView = new JPanel();
        generateTools();
        _editorPanel.setVisible(false);
        _editorPanel.add(_management);
        _editorPanel.add(_receiptView);
        _editorPanel.setVisible(true);
        generateTable();
    }
    public JFrame saveOnClose(){
        if(!_receipt._changesMade) {
            dispose();
            return null;
        }
        JFrame youSure = new JFrame("Save changes before exiting?");
        youSure.setLayout(new GridLayout(1, 2));
        youSure.setSize(300, 75);
        JButton save = new JButton("Save.");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    _receipt.saveReceipt();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                youSure.setVisible(false);
                youSure.dispose();
                dispose();
            }
        });
        JButton discard = new JButton("Discard");
        discard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                youSure.setVisible(false);
                youSure.dispose();
                dispose();
            }
        });
        youSure.add(save);
        youSure.add(discard);
        youSure.setVisible(true);
        return youSure;
    }
}
