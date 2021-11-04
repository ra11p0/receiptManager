package com.ra11p0.frames.Overview.Frames;



import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.CheckListItem;
import com.ra11p0.structures.CheckListRenderer;
import com.ra11p0.structures.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class GetItem {
    public JFrame showDialog(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.add(new JLabel("Preparing workplace..."));
        frame.setSize(new Dimension(350, 400));
        frame.setResizable(false);
        frame.setVisible(true);
        JComboBox<Object> searchField = new JComboBox<>();
        AutoCompleteSupport.install(searchField,
                GlazedLists.eventListOf(ReceiptsManager.getNamesOfProducts().toArray()));
        Thread generateUIThread = new Thread(() -> {
            DefaultListModel<CheckListItem> itemListModel = new DefaultListModel<>();
            JList<CheckListItem> itemList = new JList<>(itemListModel);
            JScrollPane itemListScrollPane = new JScrollPane(itemList);
            ListCellRenderer<CheckListItem> nonStoreBasedSearch = (list, value, index, isSelected, cellHasFocus) -> {
                CheckListRenderer renderer = new CheckListRenderer();
                Component component = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setText(((Item)value.getObject()).get_name());
                return component;
            };
            ListCellRenderer<CheckListItem> storeBasedSearch = (list, value, index, isSelected, cellHasFocus) -> {
                CheckListRenderer renderer = new CheckListRenderer();
                Component component = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setText( "("+ ((Item)value.getObject()).get_store() +")" + ((Item)value.getObject()).get_name());
                return component;
            };
            JButton selectAll = new JButton("Select all");
            JButton confirm = new JButton("Confirm");
            //*****

            searchField.setPreferredSize(new Dimension(300, 25));
            //INITIALIZING ITEMS LIST
            ArrayList<Item> items = new ArrayList<>();
            for(Item item : ReceiptsManager.getItems())  if(items.stream().noneMatch(o->o.get_name().equals(item.get_name()))) items.add(item);
            //STRING COMPARATOR -> SORT ITEMS BY NAME
            items.sort((o1, o2) -> {
                Comparator<String> comparator = Comparator.comparing((String s) -> s);
                return comparator.compare(o1.get_name().toUpperCase(Locale.ROOT), o2.get_name().toUpperCase(Locale.ROOT));
            });
            for(Item item : items) itemListModel.addElement(new CheckListItem(item));
            itemList.setCellRenderer(nonStoreBasedSearch);
            itemList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    int index = itemList.locationToIndex(event.getPoint());
                    CheckListItem item = itemList.getModel()
                            .getElementAt(index);
                    item.setSelected(!item.isSelected());
                    itemList.repaint(itemList.getCellBounds(index, index));
                }
            });
            itemListScrollPane.setPreferredSize(new Dimension(300, 240));
            //SEARCH FIELD BEHAVIOR
            searchField.addActionListener(e -> {
                boolean storeBasedSearchCheck = false;
                if(searchField.getSelectedItem() == null) {
                    itemListModel.removeAllElements();
                    for(Item item : items) itemListModel.addElement(new CheckListItem(item));
                    searchField.setBackground(null);
                    itemList.setCellRenderer(nonStoreBasedSearch);
                    return;
                }
                if (Objects.requireNonNull(searchField.getSelectedItem()).toString().equals("")){
                    itemListModel.removeAllElements();
                    for(Item item : items) itemListModel.addElement(new CheckListItem(item));
                    searchField.setBackground(null);
                    itemList.setCellRenderer(nonStoreBasedSearch);
                }
                else{
                    itemListModel.removeAllElements();
                    for(Item item : items) {
                        if(item.toString().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT)) ||
                                item.get_store().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT))) {
                            if(item.get_store().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT))) storeBasedSearchCheck = true;
                            itemListModel.addElement(new CheckListItem(item));
                            searchField.setBackground(null);
                        }
                    }
                }
                if(itemListModel.isEmpty()) searchField.setBackground(Color.RED);
                if(storeBasedSearchCheck) itemList.setCellRenderer(storeBasedSearch);
                else itemList.setCellRenderer(nonStoreBasedSearch);
            });
            //SELECT ALL BUTTON BEHAVIOR
            selectAll.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(Object item : itemListModel.toArray()){
                        ((CheckListItem) item).setSelected(true);
                    }
                    itemList.repaint();
                }
            });
            //CONFIRM BUTTON BEHAVIOR
            confirm.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.dispose();
                }
            });
            //*****
            panel.removeAll();
            panel.setVisible(false);
            panel.setLayout(new FlowLayout());
            panel.add(new JLabel("Search product or store, then hit ENTER:"));
            panel.add(searchField);
            panel.add(selectAll);
            panel.add(itemListScrollPane);
            panel.add(confirm);
            panel.repaint();
            panel.setVisible(true);
        });
        generateUIThread.start();
        return frame;
    }
}