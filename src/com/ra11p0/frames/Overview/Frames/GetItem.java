package com.ra11p0.frames.Overview.Frames;



import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.CheckListItem;
import com.ra11p0.structures.CheckListRenderer;
import com.ra11p0.structures.Item;
import com.ra11p0.utils.LangResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GetItem {
    public static ArrayList<Item> checkedItems = new ArrayList<>();
    @SuppressWarnings({"unchecked"})
    public static JFrame showDialog(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        checkedItems = new ArrayList<>();
        frame.add(panel);
        panel.add(new JLabel(LangResource.get("preparingWorkplace")));
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
            JButton selectAll = new JButton(LangResource.get("selectAll"));
            JButton unselectAll = new JButton(LangResource.get("unselectAll"));
            JButton confirm = new JButton(LangResource.get("confirm"));
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
            //*****
            itemList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(int i = 0; i < itemListModel.getSize(); i++){
                        if(itemListModel.getElementAt(i).isSelected() && !checkedItems.contains(itemListModel.getElementAt(i).getObject())) checkedItems.add((Item) itemListModel.getElementAt(i).getObject());
                        else if(!itemListModel.getElementAt(i).isSelected() && checkedItems.contains(itemListModel.getElementAt(i).getObject())) checkedItems.remove((Item) itemListModel.getElementAt(i).getObject());
                    }
                }
            });
            //SEARCH FIELD BEHAVIOR
            searchField.addActionListener(e -> {
                boolean storeBasedSearchCheck = false;
                ArrayList<Item> tempCheckedItems = (ArrayList<Item>) checkedItems.clone();
                if(searchField.getSelectedItem() == null) {
                    itemListModel.removeAllElements();
                    for(Item item : items) {
                        tempCheckedItems.remove(item);
                        itemListModel.addElement(new CheckListItem(item));
                    }
                    searchField.setBackground(null);
                    itemList.setCellRenderer(nonStoreBasedSearch);
                }
                else if (Objects.requireNonNull(searchField.getSelectedItem()).toString().equals("")){
                    itemListModel.removeAllElements();
                    for(Item item : items) {
                        tempCheckedItems.remove(item);
                        itemListModel.addElement(new CheckListItem(item));
                    }
                    searchField.setBackground(null);
                    itemList.setCellRenderer(nonStoreBasedSearch);
                }
                else{
                    itemListModel.removeAllElements();
                    for(Item item : items) {
                        if(item.toString().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT)) ||
                                item.get_store().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT))) {
                            if(item.get_store().toUpperCase(Locale.ROOT).contains(searchField.getSelectedItem().toString().toUpperCase(Locale.ROOT))) storeBasedSearchCheck = true;
                            tempCheckedItems.remove(item);
                            CheckListItem checkListItem = new CheckListItem(item);
                            itemListModel.addElement(checkListItem);
                            searchField.setBackground(null);
                        }
                    }
                }
                if(itemListModel.isEmpty()) searchField.setBackground(Color.RED);
                if(storeBasedSearchCheck) itemList.setCellRenderer(storeBasedSearch);
                else itemList.setCellRenderer(nonStoreBasedSearch);
                for(Item item : tempCheckedItems) itemListModel.addElement(new CheckListItem(item));
                for(int i = 0; i < itemListModel.getSize(); i++){
                    if(checkedItems.contains(itemListModel.getElementAt(i).getObject())) itemListModel.getElementAt(i).setSelected(true);
                }
            });
            //SELECT ALL BUTTON BEHAVIOR
            selectAll.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(Object item : itemListModel.toArray()){
                        ((CheckListItem) item).setSelected(true);
                        checkedItems.add(((Item) ((CheckListItem) item).getObject()));
                    }
                    itemList.repaint();
                }
            });
            //UNSELECT ALL BUTTON BEHAVIOR
            unselectAll.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(Object item : itemListModel.toArray()){
                        ((CheckListItem) item).setSelected(false);
                        checkedItems.remove(((Item) ((CheckListItem) item).getObject()));
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
            panel.add(new JLabel(LangResource.get("searchProductOrStoreThenHitEnter") + ":"));
            panel.add(searchField);
            panel.add(selectAll);
            panel.add(unselectAll);
            panel.add(itemListScrollPane);
            panel.add(confirm);
            panel.repaint();
            panel.setVisible(true);
        });
        generateUIThread.start();
        return frame;
    }
}