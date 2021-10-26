package com.ra11p0.frames.Overview.Panels;

import com.ra11p0.frames.Overview.Frames.EditItem;
import com.ra11p0.frames.ReceiptsEditor.ReceiptEditor;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Item;
import com.ra11p0.structures.Receipt;
import com.ra11p0.structures.ReceiptItem;
import javafx.scene.layout.Border;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class SearchResultPanel extends JPanel {
    private final JPanel receiptsPanel = new JPanel(new BorderLayout());
    private final JPanel dataPanel = new JPanel(new GridLayout(5, 2));
    private final JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
    private ArrayList<Receipt> _receipts = new ArrayList<>();
    private ArrayList<Item> _items;
    private Date _fromDate = new Date(0);
    private Date _toDate = new Date(System.currentTimeMillis());
    public SearchResultPanel(ArrayList<Item> items){
        setVisible(false);
        removeAll();
        setLayout(new BorderLayout());
        initializeDateAndItems(items);
        generateReceiptsPanel();
        generateDataPanel();
        generateOptionsPanel();
        add(receiptsPanel, BorderLayout.LINE_START);
        add(dataPanel, BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.LINE_END);
        setVisible(true);
    }
    private void initializeDateAndItems(ArrayList<Item> items){
        _receipts.clear();
        for(Receipt receipt : ReceiptsManager.getReceiptsContaining(items))
            if(receipt.get_date().before(_toDate) && receipt.get_date().after(_fromDate))
                _receipts.add(receipt);
        _items = items;
    }
    private void generateReceiptsPanel(){
        receiptsPanel.setVisible(false);
        receiptsPanel.removeAll();

        DefaultListModel<Receipt> receiptListModel = new DefaultListModel<>();
        JList<Receipt> receiptList = new JList<>(receiptListModel);
        JScrollPane receiptListScrollPane = new JScrollPane(receiptList);

        _receipts.sort(Comparator.comparingLong(o -> o.get_date().getTime()));
        Collections.reverse(_receipts);
        for(Receipt receipt : _receipts) receiptListModel.addElement(receipt);
        receiptList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            float sum = 0F;
            JPanel renderer = new JPanel(new GridLayout(1, 3));
            for(ReceiptItem receiptItem : value.get_items())
                for(Item _item : _items)
                    if(receiptItem.get_Item().equals(_item)) sum += _item.get_price() * receiptItem.get_qty();
            JLabel store = new JLabel(value.get_store());
            JLabel date = new JLabel(value.get_dateString());
            JLabel sumLabel = new JLabel(String.format("%.2f", sum) + " PLN");
            store.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 15));
            date.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 15));
            sumLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            renderer.add(store);
            renderer.add(date);
            renderer.add(sumLabel);
            renderer.setBackground(isSelected ? Color.black : list.getBackground());
            return renderer;
        });
        receiptsPanel.add(receiptListScrollPane, BorderLayout.CENTER);
        receiptsPanel.setVisible(true);
    }
    private void generateDataPanel(){
        dataPanel.setVisible(false);
        dataPanel.removeAll();
        //*****
        float total = 0F;
        float tax = 0F;
        JLabel nameLabel = new JLabel();
        JLabel totalLabel = new JLabel();
        JLabel taxLabel = new JLabel();
        JLabel totalTaxLabel = new JLabel();
        JPanel pricesInStores;
        //TOTAL LABEL
        for(Receipt receipt : _receipts)
            for(ReceiptItem receiptItem : receipt.get_items())
                for(Item item : _items)
                    if(receiptItem.get_Item().equals(item)) total += (receiptItem.get_qty() * item.get_price());
        totalLabel.setText(String.format("%.2f", total) + " PLN");
        //TAX LABEL
        int counter = 0;
        for(Receipt receipt : _receipts)
            for(ReceiptItem receiptItem : receipt.get_items())
                for(Item item : _items)
                    if(receiptItem.get_Item().equals(item)) {
                        tax += item.get_taxRate();
                        counter++;
                    }
        tax = tax/counter;
        taxLabel.setText(String.format("%.0f", tax*100)+"%");
        //TOTAL TAX LABEL
        totalTaxLabel.setText(String.format("%.2f", tax*total) + " PLN");
        //NAME LABEL
        ArrayList<String> namesArray = new ArrayList<>();
        for(Item item : _items) namesArray.add(item.get_name());
        nameLabel.setText(findCommon(namesArray));
        //PRICES IN STORES
        int itemsCounter = 0;
        for(Item item : _items) {
            boolean contains = false;
            for(Receipt receipt : _receipts)
                for(ReceiptItem receiptItem : receipt.get_items())
                    if(receiptItem.get_Item().equals(item)) contains = true;
            if(contains) itemsCounter++;
        }
        pricesInStores = new JPanel(new GridLayout(itemsCounter, 2));
        JScrollPane pricesInStoresScrollPane = new JScrollPane(pricesInStores);
        _items.sort((o1, o2) -> Float.compare(o1.get_price(), o2.get_price()));
        for(Item item : _items) {
            boolean contains = false;
            for(Receipt receipt : _receipts)
                for(ReceiptItem receiptItem : receipt.get_items())
                    if(receiptItem.get_Item().equals(item)) contains = true;
            if(contains) {
                pricesInStores.add(new JLabel(item.get_store()));
                pricesInStores.add(new JLabel(String.format("%.2f", item.get_price()) + " PLN"));
                itemsCounter++;
            }
        }
        //*****
        dataPanel.add(new JLabel("Name: "));
        dataPanel.add(nameLabel);
        dataPanel.add(new JLabel("Total: "));
        dataPanel.add(totalLabel);
        dataPanel.add(new JLabel("Tax rate: "));
        dataPanel.add(taxLabel);
        dataPanel.add(new JLabel("Total tax: "));
        dataPanel.add(totalTaxLabel);
        dataPanel.add(new JLabel("Prices in stores:"));
        dataPanel.add(pricesInStoresScrollPane);
        dataPanel.setVisible(true);
    }
    private void generateOptionsPanel(){
        optionsPanel.setVisible(false);
        optionsPanel.removeAll();
        JButton setDateBounds = new JButton("Set date bounds");
        JButton previewReceipt = new JButton("Show receipt");
        JButton editItem = new JButton("Edit item");
        //SET DATE BOUNDS BUTTON BEHAVIOR
        setDateBounds.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setDateBoundsFrame();
            }
        });
        //PREVIEW BUTTON BEHAVIOR
        previewReceipt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showReceiptPreview(previewReceipt);
            }
        });
        //EDIT ITEM BUTTON BEHAVIOR
        editItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditItem editItem = new EditItem(_items);
                editItem.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        if(!editItem.anyChange || editItem.oldItem == null || editItem.newItem == null) return;
                        Object choice = JOptionPane.showOptionDialog(null,
                                "Are you sure you want to replace " + editItem.oldItem.toString() + " with " + editItem.newItem.toString() + "?" , "",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                null, new Object[]{"NO", "YES"},
                                "NO");
                        if((int)choice != 1) return;
                        _items.remove(editItem.oldItem);
                        _items.add(new Item(editItem.newItem.get_name(), editItem.oldItem.get_taxRate(), editItem.newItem.get_price(), editItem.oldItem.get_store()));
                        _receipts = ReceiptsManager.getReceiptsContaining(_items);
                        generateReceiptsPanel();
                        generateOptionsPanel();
                        generateDataPanel();
                    }
                });
            }
        });
        optionsPanel.add(setDateBounds);
        optionsPanel.add(previewReceipt);
        optionsPanel.add(editItem);
        optionsPanel.setVisible(true);
    }
    private void setDateBoundsFrame(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(1, 2));
        JPanel datePickerPanel = new JPanel(new GridLayout(1, 2));
        UtilDateModel fromModel = new UtilDateModel();
        UtilDateModel toModel = new UtilDateModel();
        JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromModel);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toModel);
        JButton confirm = new JButton("Confirm");
        JLabel toLabel = new JLabel("to:");
        fromModel.setDate(fromModel.getYear(), fromModel.getMonth(), 1);
        toModel.setDate(toModel.getYear(), toModel.getMonth(), Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        fromModel.setSelected(true);
        toModel.setSelected(true);
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _fromDate = fromModel.getValue();
                _toDate = toModel.getValue();
                initializeDateAndItems(_items);
                generateReceiptsPanel();
                generateOptionsPanel();
                generateDataPanel();
                frame.setVisible(false);
                frame.dispose();
            }
        });
        //*****
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        toLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        labelPanel.add(new JLabel("Date bounds since:"));
        labelPanel.add(toLabel);
        //*****
        fromDatePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
        toDatePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
        datePickerPanel.add(fromDatePanel);
        datePickerPanel.add(toDatePanel);
        //*****
        frame.setSize(400, 277);
        frame.add(labelPanel, BorderLayout.PAGE_START);
        frame.add(datePickerPanel, BorderLayout.CENTER);
        frame.add(confirm, BorderLayout.AFTER_LAST_LINE);
        frame.setVisible(true);
    }
    private void showReceiptPreview(JButton previewReceipt){
        if(previewReceipt.getText().equals("Show receipt")) {
            JList<Receipt> receiptList = (JList<Receipt>) (((JScrollPane) receiptsPanel.getComponents()[0]).getViewport().getView());
            if(receiptList.getSelectedValue() == null) return;
            receiptsPanel.setVisible(false);
            receiptsPanel.removeAll();
            ReceiptEditor editor = new ReceiptEditor(receiptList.getSelectedValue(), true);
            receiptsPanel.add(editor.get_receiptView());
            receiptsPanel.setVisible(true);
            optionsPanel.setVisible(false);
            previewReceipt.setText("Hide receipt");
            optionsPanel.setVisible(true);
        }else{
            generateReceiptsPanel();
            generateOptionsPanel();
        }
    }
    public static String findCommon(ArrayList<String> inputArray) {
        ArrayList<String> arr = new ArrayList<>();
        for(String string : inputArray) arr.add(string.replaceAll("-", " "));
        ArrayList<String> outputArray = new ArrayList<>();
        for(String string : arr) {
            StringBuilder result = new StringBuilder();
            String[] words = string.split(" ");
            for (String word : words) {
                if (word.length() > 1) {
                    boolean contains = true;
                    for (String name : arr) {
                        if (!name.toUpperCase(Locale.ROOT).contains(word.toUpperCase(Locale.ROOT))) contains = false;
                    }
                    if (contains) result.append(word).append(" ");
                }
            }
            outputArray.add(result.toString());
        }
        int length = 0;
        int index = 0;
        for(String string : outputArray){
            if(string.split(" ").length > length){
                index = outputArray.indexOf(string);
                length = string.split(" ").length;
            }
        }
        return outputArray.get(index);
    }
}
