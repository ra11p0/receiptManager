package com.ra11p0.frames;

import com.google.gson.Gson;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ManageReceiptFrame extends JFrame {

    public ManageReceiptFrame(ArrayList<Item> items){
        JFrame frame = new JFrame("Select store.");
        JComboBox<String> stores = new JComboBox<String>();
        ArrayList<String> storesList = new ArrayList<String>();
        //
        //Get stores from all receipts
        //
        String[] receiptFiles = new File("res/receipts/").list();
        for(String file : receiptFiles){
            Gson gson = new Gson();
            try {
                if (!file.substring(0, 1).equals(".")) {
                    Receipt receipt = gson.fromJson(new FileReader("res/receipts/" + file), Receipt.class);
                    if (!storesList.contains(receipt.get_store())) storesList.add(receipt.get_store());
                }
            } catch (Exception e) {
                new JOptionPane().showMessageDialog(this, e,"Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        for(String store : storesList) stores.addItem(store);
        Button confirm = new Button ("Confirm");
        //
        //Confirm button
        //
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ManageReceiptFrame(new Receipt(stores.getSelectedItem().toString(), new Date(System.currentTimeMillis())), items);
                frame.setVisible(false);
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
        Button addNew = new Button("Add new store.");
        //
        //Add new store button
        //
        addNew.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame addNewStoreFrame = new JFrame();
                TextField nameOfNewStore = new TextField();
                Button confirm = new Button("Confirm");
                confirm.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                         if (!storesList.contains(nameOfNewStore.getText())){
                             stores.removeAllItems();
                             storesList.add(nameOfNewStore.getText().toString());
                             for(String store : storesList) stores.addItem(store);
                         }
                         addNewStoreFrame.setVisible(false);

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
                addNewStoreFrame.setLayout(new GridLayout());
                addNewStoreFrame.setSize(400, 65);
                addNewStoreFrame.setTitle("Add new store.");
                addNewStoreFrame.add(new Label("Name the new store:"));
                addNewStoreFrame.add(nameOfNewStore);
                addNewStoreFrame.add(confirm);
                addNewStoreFrame.setVisible(true);
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
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(4, 1));
        frame.add(new Label("Select store:"));
        frame.add(stores);
        frame.add(addNew);
        frame.add(confirm);
        frame.setVisible(true);
    }
    public ManageReceiptFrame(Receipt receipt, ArrayList<Item> items){
        //
        //Save or discard changes
        //
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
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
        panel.setLayout(new GridLayout(6, 2));
        TextField storeName = new TextField(receipt.get_store());
        storeName.setEditable(false);
        TextField ID = new TextField(receipt.get_ID());
        ID.setEditable(false);
        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.setTimeInMillis(receipt.get_date().getTime());
        dateString += (calendar.get(Calendar.YEAR)-2000) + "-";
        if (calendar.get(Calendar.MONTH)+1 < 10) dateString += "0" + calendar.get(Calendar.MONTH)+1 + "-";
        else dateString += calendar.get(Calendar.MONTH)+1 + "-";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) dateString += "0" + calendar.get(Calendar.DAY_OF_MONTH);
        else dateString += calendar.get(Calendar.DAY_OF_MONTH);
        TextField date = new TextField(dateString);
        date.setEditable(false);
        TextField total = new TextField(String.format("%.2f", receipt.get_paid()));
        total.setEditable(false);
        Button addItem = new Button("Add item.");
        addItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame addItemFrame = new JFrame("Add item.");
                addItemFrame.setLayout(new GridLayout(5, 1));
                addItemFrame.setSize(350, 200);
                JComboBox<Item> itemsBox = new JComboBox<Item>();
                for(Item item : items) if (item.get_store().equals(receipt.get_store())) itemsBox.addItem(item);
                TextField qty = new TextField();
                qty.addTextListener(new TextListener() {
                    @Override
                    public void textValueChanged(TextEvent e) {
                        try{
                            Float.parseFloat(qty.getText());
                        }catch(Exception ex){
                            qty.setText("");
                        }
                    }
                });
                Button confirm = new Button("Confirm.");
                confirm.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        receipt.addItem(new ReceiptItem((Item)itemsBox.getSelectedItem(), Float.parseFloat(qty.getText())));
                        addItemFrame.setVisible(false);
                        repaintFrame(panel, tablePanel, receipt, items);
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
                //
                //Add item dialog
                //
                newItem.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JFrame createNewItemFrame = new JFrame("Create new item.");
                        createNewItemFrame.setSize(200, 180);
                        createNewItemFrame.setLayout(new FlowLayout());
                        TextField name = new TextField();
                        name.setPreferredSize(new Dimension(175, 25));
                        TextField taxRate = new TextField();
                        taxRate.addTextListener(new TextListener() {
                            @Override
                            public void textValueChanged(TextEvent e) {
                                try{
                                    Float.parseFloat(taxRate.getText());
                                }catch(Exception ex){
                                    taxRate.setText("");
                                }
                            }
                        });
                        taxRate.setPreferredSize(new Dimension(50, 25));
                        TextField cost = new TextField();
                        cost.addTextListener(new TextListener() {
                            @Override
                            public void textValueChanged(TextEvent e) {
                                try{
                                    Float.parseFloat(cost.getText());
                                }catch(Exception ex){
                                    cost.setText("");
                                }
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
        removeItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame removeItemFrame = new JFrame("Remove item.");
                JComboBox<ReceiptItem> itemsList = new JComboBox<>();
                for(ReceiptItem receiptItem:receipt.get_items()) itemsList.addItem(receiptItem);
                Button removeButton = new Button("Remove.");
                removeButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        receipt.removeItem((ReceiptItem) itemsList.getSelectedItem());
                        repaintFrame(panel, tablePanel, receipt, items);
                        removeItemFrame.setVisible(false);
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
                removeItemFrame.setLayout(new GridLayout(3, 1));
                removeItemFrame.setSize(200, 200);
                removeItemFrame.add(new Label("Select item to remove: "));
                removeItemFrame.add(itemsList);
                removeItemFrame.add(removeButton);
                removeItemFrame.setVisible(true);
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
        editDate.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame datePickerFrame = new JFrame("Date picker.");
                UtilDateModel model = new UtilDateModel();
                JDatePanelImpl datePanel = new JDatePanelImpl(model);
                JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
                Button confirm = new Button("Confirm.");
                confirm.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        receipt.setNewId(model.getValue());
                        datePickerFrame.setVisible(false);
                        repaintFrame(panel, tablePanel, receipt, items);
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
                datePickerFrame.setLayout(new GridLayout(3, 1));
                datePickerFrame.setSize(200, 200);
                datePickerFrame.add(new Label("Pick the date: "));
                datePickerFrame.add(datePicker);
                datePickerFrame.add(confirm);
                datePickerFrame.setVisible(true);
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
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    receipt.saveReceipt();
                    new JOptionPane().showMessageDialog(panel, receipt.get_ID() + " saved!","Saved!", JOptionPane.INFORMATION_MESSAGE);
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
    private void repaintFrame(JPanel panel, JPanel tablePanel, Receipt receipt, ArrayList<Item> items){
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
}
