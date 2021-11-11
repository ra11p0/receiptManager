package com.ra11p0.frames.ReceiptsEditor;

import com.ra11p0.frames.Init;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import com.ra11p0.utils.LangResource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ReceiptEditor extends JFrame {
    private final JPanel _receiptView = new JPanel();
        public JPanel get_receiptView() {return _receiptView;}
    private final JPanel _management = new JPanel();
        //public JPanel get_management() {return _management;}
    private final JPanel _editorPanel = new JPanel();
        public JPanel get_editorPanel() {return _editorPanel;}
    private static Receipt _receipt;
        //public static Receipt get_receipt() {return _receipt;}
    private static ArrayList<Item> _items;
        //public static ArrayList<Item> get_items() {return _items;}

    public ReceiptEditor(Receipt receipt){
        //SAVE ON CLOSE
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveOnClose();
            }
        });
        generateEditorPanel(receipt);
        add(_editorPanel);
        setTitle("Receipt manager.");
        setSize(800, 350);
        setResizable(false);
        setVisible(true);
    }
    public ReceiptEditor(Receipt receipt, boolean background){
            if(!background) {
                new ReceiptEditor(receipt);
                return;
            }
        generateEditorPanel(receipt);
    }
    private void generateEditorPanel(Receipt receipt){
        _editorPanel.setLayout(new GridLayout(1, 2));
        _receipt = receipt;
        _items = ReceiptsManager.getItems();
        generateTools();
        generateTable();
        _editorPanel.add(_management);
        _editorPanel.add(_receiptView);
    }
    private void generateTable(){
        DefaultTableModel tableModel = new DefaultTableModel(){
            //READ ONLY TABLE
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane tablePane = new JScrollPane(table);
        _receiptView.setLayout(new FlowLayout());
        _receiptView.setBorder(BorderFactory.createLoweredBevelBorder());
        tableModel.addColumn(LangResource.get("item"));
        tableModel.addColumn(LangResource.get("qty"));
        tableModel.addColumn(LangResource.get("price"));
        tableModel.addColumn(LangResource.get("tax"));
        tableModel.addColumn(LangResource.get("total"));
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
        JTextField storeName = new JTextField(_receipt.get_store());
        JTextField ID = new JTextField(_receipt.get_ID());
        JTextField date = new JTextField(_receipt.get_dateString());
        JTextField total = new JTextField(String.format("%.2f", _receipt.get_paid()));
        JButton addItem = new JButton(LangResource.get("addItem"));
        JButton editDate = new JButton(LangResource.get("editDate"));
        JButton save = new JButton(LangResource.get("save"));
        storeName.setEditable(false);
        ID.setEditable(false);
        date.setEditable(false);
        total.setEditable(false);
        //ADD ITEM
        addItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddItem.showDialog(_items, _receipt, currentReceiptEditor);
            }
        });
        JButton removeItem = new JButton(LangResource.get("removeItem"));
        //REMOVE ITEM
        removeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RemoveItem.showDialog(_receipt, currentReceiptEditor);
            }
        });
        //EDIT DATE
        editDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditDate.showDialog(_receipt, currentReceiptEditor);
            }
        });
        //SAVE
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    _receipt.saveReceipt();
                    JOptionPane.showMessageDialog(_management, _receipt.get_ID() + " " + LangResource.get("saved") + "!",LangResource.get("saved") + "!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ID.setText(_receipt.get_ID());
            }
        });
        //*****
        _management.setLayout(new GridLayout(6, 2));
        _management.add(new Label(  LangResource.get("store") + ": "));
        _management.add(storeName);
        _management.add(new Label("ID: "));
        _management.add(ID);
        _management.add(new Label(LangResource.get("date") + ": "));
        _management.add(date);
        _management.add(new Label(LangResource.get("total") + ": "));
        _management.add(total);
        _management.add(addItem);
        _management.add(removeItem);
        _management.add(editDate);
        _management.add(save);
    }
    public void repaintFrame(){
        _editorPanel.setVisible(false);
        _editorPanel.remove(_management);
        _editorPanel.remove(_receiptView);
        _management.removeAll();
        _receiptView.removeAll();
        generateTools();
        generateTable();
        _editorPanel.add(_management);
        _editorPanel.add(_receiptView);
        _editorPanel.setVisible(true);
    }
    public JFrame saveOnClose(){
        if(!_receipt._changesMade) return null;
        JFrame youSure = new JFrame(LangResource.get("doYouWantToSaveTheChanges") + "?");
        JButton save = new JButton(LangResource.get("yes"));
        JButton discard = new JButton(LangResource.get("no"));
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
        discard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                youSure.setVisible(false);
                youSure.dispose();
                dispose();
            }
        });
        youSure.setLayout(new GridLayout(1, 2));
        youSure.setSize(300, 75);
        youSure.add(save);
        youSure.add(discard);
        youSure.setVisible(true);
        return youSure;
    }
}
